package hexlet.code;

import hexlet.code.controller.UrlsController;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.ConfigRepository;
import hexlet.code.util.NamedRoutes;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AppTest {
    private static Javalin app;
    private static DataSource dataSource;
    private final Context ctx = mock(Context.class);


    @BeforeAll
    public static final void configureApp() {
        dataSource = ConfigRepository.configureRepository("jdbc:h2:mem:hexlet_project;DB_CLOSE_DELAY=-1;");
    }

    @BeforeEach
    public final void updateSchemaDataBase() {
        ConfigRepository.createSchemaDataBase(dataSource);
        app = App.getApp();
    }

    @Test
    public void mainPageTest() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.mainPath());
            assertEquals(200, response.code());
        });
    }

    @Test
    public void urlAddSuccessTest() throws SQLException {
        when(ctx.formParam("url")).thenReturn("https://github.com/hexlet-components/java-javalin-example");
        UrlsController.create(ctx);
        verify(ctx).sessionAttribute("textFlash", "Страница успешно добавлена");
        verify(ctx).redirect(NamedRoutes.urlsPath());
        assertTrue(UrlRepository.existNameAccurate("https://github.com"));
    }

    @Test
    public void urlAddUncorrectUrlTest() throws SQLException {
        when(ctx.formParam("url")).thenReturn("ttps://github.com");
        UrlsController.create(ctx);
        verify(ctx).sessionAttribute("textFlash", "Некорректный URL");
        verify(ctx).redirect(NamedRoutes.mainPath());
    }

    @Test
    public void urlAddRetryErrorTest() throws SQLException {
        Url url = new Url("https://github.com", Timestamp.valueOf(LocalDateTime.now()));
        UrlRepository.save(url);

        when(ctx.formParam("url")).thenReturn("https://github.com");
        UrlsController.create(ctx);
        verify(ctx).sessionAttribute("textFlash", "Страница уже существует");
        verify(ctx).redirect(NamedRoutes.urlsPath());
    }

    @Test
    public void urlsPageTest() throws SQLException {
        when(ctx.formParam("url")).thenReturn("https://github.com/hexlet-components/java-javalin-example");
        UrlsController.create(ctx);

        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlsPath());
            assertEquals(200, response.code());
            assertTrue(response.body().string().contains("https://github.com"));
        });
    }

    @Test
    public void urlShowTest() throws SQLException {
        Url url = new Url("https://github.com", Timestamp.valueOf(LocalDateTime.now()));
        UrlRepository.save(url);

        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath("1"));
            String responseBody = response.body().string();
            assertTrue(responseBody.contains("1"));
            assertTrue(responseBody.contains("https://github.com"));
            assertEquals(200, response.code());

            response = client.get(NamedRoutes.urlPath("2"));
            assertTrue(response.body().string().contains("User with id = 2 not found"));
        });
    }
}

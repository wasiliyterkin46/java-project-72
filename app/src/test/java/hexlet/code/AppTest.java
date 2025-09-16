package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.controller.UrlChecksController;
import hexlet.code.controller.UrlsController;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.testtools.JavalinTest;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class AppTest {
    private static Javalin app;
    private static Context ctx;
    private static MockWebServer mockServer;
    private static HikariDataSource dataSource;
    private static String sqlSchema;

    @BeforeAll
    public static void beforeAll() {
        setDataSource();
        setSqlSchema();
    }

    private static void setDataSource() {
        String dataBaseUrl = "jdbc:h2:mem:hexlet_project;DB_CLOSE_DELAY=-1;";
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dataBaseUrl);
        dataSource = new HikariDataSource(hikariConfig);
    }

    private static void setSqlSchema() {
        var url = App.class.getClassLoader().getResourceAsStream("schema.sql");
        sqlSchema =  new BufferedReader(new InputStreamReader(url))
                .lines().collect(Collectors.joining("\n"));
    }

    @BeforeEach
    public final void updateApp() {
        app = App.getApp();
        ctx = mock(Context.class);
        createSchemaDataBase();
    }

    private static void createSchemaDataBase() {
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sqlSchema);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    public static void afterAll() throws IOException {
        app.stop();
        if (mockServer != null) {
            mockServer.close();
        }
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
            var responseBody = response.body().string();
            assertEquals(200, response.code());
            assertTrue(responseBody.contains("https://github.com"));
            assertFalse(responseBody.contains("/hexlet-components/java-javalin-example"));
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
            assertEquals(404, response.code());
            assertTrue(response.body().string().contains("Url with id = 2 not found"));
        });
    }

    @Test
    public void urlWithChecksShowTest() throws SQLException, IOException {
        mockServer = new MockWebServer();
        mockServer.enqueue(new MockResponse()
                .setBody(getContentFile("src/test/resources/url0.html"))
                .setResponseCode(200));
        mockServer.enqueue(new MockResponse()
                .setBody(getContentFile("src/test/resources/url1.html"))
                .setResponseCode(777));
        mockServer.enqueue(new MockResponse()
                .setBody("")
                .setResponseCode(404));

        mockServer.start();

        HttpUrl baseUrl = mockServer.url("");

        when(ctx.formParam("url")).thenReturn(baseUrl.toString());
        UrlsController.create(ctx);
        Long idUrl = 1L;

        JavalinTest.test(app, (server, client) -> {
            var response = client.post(NamedRoutes.urlCheckPath(idUrl));
            String responseBody = response.body().string();
            String[] checkValues = getContentFile("src/test/resources/url0CheckValues.txt")
                    .split("\n");
            for (String checkValue : checkValues) {
                assertTrue(responseBody.contains(checkValue));
            }

            var response2 = client.post(NamedRoutes.urlCheckPath(idUrl));
            String responseBody2 = response2.body().string();
            String[] checkValues2 = getContentFile("src/test/resources/url1CheckValues.txt")
                    .split("\n");
            for (String checkValue : checkValues2) {
                assertTrue(responseBody2.contains(checkValue));
            }
        });
    }

    @Test
    public void urlWithChecksBadServerTest() throws SQLException, IOException {
        mockServer = new MockWebServer();
        mockServer.start();

        HttpUrl baseUrl = mockServer.url("");

        when(ctx.formParam("url")).thenReturn(baseUrl.toString());
        UrlsController.create(ctx);
        Long idUrl = 1L;
        mockServer.shutdown();
        when(ctx.pathParam("id")).thenReturn(String.valueOf(idUrl));
        UrlChecksController.create(ctx);

        verify(ctx).sessionAttribute("textFlash", "Некорректный адрес");
        verify(ctx).redirect(NamedRoutes.urlPath(idUrl));

        JavalinTest.test(app, (server, client) -> {
            var response = client.post(NamedRoutes.urlCheckPath(idUrl));
            assertEquals(200, response.code());
        });
    }

    private static String getContentFile(String filePath) throws IllegalArgumentException, IOException {
        Path pathToFile = Paths.get(filePath).toAbsolutePath().normalize();
        if (!Files.exists(pathToFile)) {
            throw new FileNotFoundException(String.format("File %s does not exist", pathToFile));
        }

        return Files.readString(pathToFile);
    }
}

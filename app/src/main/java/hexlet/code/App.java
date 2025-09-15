package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.controller.UrlChecksController;
import hexlet.code.controller.UrlsController;
import hexlet.code.repository.BaseRepository;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import lombok.extern.slf4j.Slf4j;

import hexlet.code.controller.MainController;
import hexlet.code.util.NamedRoutes;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.stream.Collectors;

@Slf4j
public class App {
    public static void main(String[] args) throws IOException, SQLException {
        var app = getApp();
        app.start(getPort());
    }

    public static Javalin getApp() {
        configureRepository();
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
            config.http.disableCompression();
        });

        addHandlers(app);

        return app;
    }

    private static Javalin addHandlers(Javalin app) {
        app.get(NamedRoutes.mainPath(), MainController::index);
        app.get(NamedRoutes.urlsPath(), UrlsController::index);
        app.post(NamedRoutes.urlsPath(), UrlsController::create);
        app.get(NamedRoutes.urlPath("{id}"), UrlsController::show);
        app.post(NamedRoutes.urlCheckPath("{id}"), UrlChecksController::create);

        return app;
    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
        return templateEngine;
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }

    private static DataSource configureRepository() {
        String dataBaseUrl = getDatabaseUrl();
        DataSource dataSource = getDataSource(dataBaseUrl);
        new BaseRepository(dataSource);
        createSchemaDataBase(dataSource);
        return dataSource;
    }

    private static void createSchemaDataBase(DataSource dataSource) {
        var sql = getSqlShemaDataBase();

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static DataSource getDataSource(String dataBaseUrl) {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dataBaseUrl);
        return new HikariDataSource(hikariConfig);
    }

    private static String getSqlShemaDataBase() {
        var url = App.class.getClassLoader().getResourceAsStream("schema.sql");
        return new BufferedReader(new InputStreamReader(url))
                .lines().collect(Collectors.joining("\n"));
    }

    private static String getDatabaseUrl() {
        return System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:hexlet_project;DB_CLOSE_DELAY=-1;");
    }
}

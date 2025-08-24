package hexlet.code;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import lombok.extern.slf4j.Slf4j;

import hexlet.code.controller.MainController;
import hexlet.code.util.NamedRoutes;
import hexlet.code.util.ConfigRepository;

import java.io.IOException;
import java.sql.SQLException;

@Slf4j
public class App {
    public static void main(String[] args) throws IOException, SQLException {
        var app = getApp();
        app.start(ConfigRepository.getPort());
    }

    public static Javalin getApp() {
        ConfigRepository.configureRepository();

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        addHandlers(app);

        return app;
    }

    private static Javalin addHandlers(Javalin app) {
        app.get(NamedRoutes.mainPath(), MainController::index);

        return app;
    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
        return templateEngine;
    }
}

package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {
    public static void create(Context ctx) throws SQLException {
        String name = ctx.formParam("url");
        if (!formatUrlIsCorrect(name)) {
            ctx.sessionAttribute("textFlash", "Некорректный URL");
            ctx.sessionAttribute("typeFlash", "alert-danger");
            ctx.sessionAttribute("textUrl", name);
            ctx.redirect(NamedRoutes.mainPath());
        } else {
            String urlNormalize = normalizeUrl(name);

            if (urlExist(urlNormalize)) {
                ctx.sessionAttribute("textFlash", "Страница уже существует");
                ctx.sessionAttribute("typeFlash", "alert-secondary");
                ctx.redirect(NamedRoutes.urlsPath());
            } else {
                Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());
                Url url = new Url(urlNormalize, createdAt);
                UrlRepository.save(url);
                ctx.sessionAttribute("textFlash", "Страница успешно добавлена");
                ctx.sessionAttribute("typeFlash", "alert-success");
                ctx.redirect(NamedRoutes.urlsPath());
            }
        }
    }

    public static void index(Context ctx) throws SQLException {
        List<UrlsPage.UrlInfo> urls = UrlRepository.getEntityInfo();
        UrlsPage page = new UrlsPage(urls);
        page.setTextFlash(ctx.consumeSessionAttribute("textFlash"));
        page.setTypeFlash(ctx.consumeSessionAttribute("typeFlash"));
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParam("id");
        var url = UrlRepository.findId(id)
                .orElseThrow(() -> new NotFoundResponse("Url with id = " + id + " not found"));
        var urlCheck = UrlCheckRepository.selectEntity(url.getId());
        var page = new UrlPage(url, urlCheck);
        page.setTextFlash(ctx.consumeSessionAttribute("textFlash"));
        page.setTypeFlash(ctx.consumeSessionAttribute("typeFlash"));

        ctx.render("urls/show.jte", model("page", page));
    }

    private static Boolean formatUrlIsCorrect(String textUrl) {
        try {
            getUrl(textUrl);
            return true;
        } catch (URISyntaxException | MalformedURLException e) {
            return false;
        }
    }

    private static String normalizeUrl(String textUrl) {
        try {
            URL url = getUrl(textUrl);
            String protocol = url.getProtocol();
            String host = url.getAuthority();

            return protocol + "://" + host;
        } catch (URISyntaxException | MalformedURLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static URL getUrl(String textUrl) throws URISyntaxException, MalformedURLException {
        URI uri = new URI(textUrl);
        URL url = uri.toURL();
        return url;
    }

    private static Boolean urlExist(String urlNormalize) throws SQLException {
        return UrlRepository.existNameAccurate(urlNormalize);
    }
}

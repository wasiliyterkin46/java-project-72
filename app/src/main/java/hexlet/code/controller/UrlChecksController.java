package hexlet.code.controller;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.function.Function;

public class UrlChecksController {
    public static void create(Context ctx) throws SQLException {
        String idUrl = ctx.pathParam("id");
        UrlCheck checkedUrl = checkUrl(idUrl);
        UrlCheckRepository.save(checkedUrl);
        ctx.redirect(NamedRoutes.urlPath(idUrl));
    }

    private static UrlCheck checkUrl(String idUrl) throws SQLException {
        Url urlForVerification = UrlRepository.findId(idUrl)
                .orElseThrow(() -> new NotFoundResponse("Url with id = " + idUrl + " not found"));
        HttpResponse<String> response = Unirest.get(urlForVerification.getName())
                .asString();

        String html = response.getBody();

        var statusCode = response.getStatus();
        var createdAt = Timestamp.valueOf(LocalDateTime.now());
        Document document = Jsoup.parse(html, urlForVerification.getName());
        var h1 = getValueFromHtml(document, "h1", Element::text);
        var title = getValueFromHtml(document, "title", Element::text);
        var description = getValueFromHtml(document, "meta[name=\"description\"][content]", e -> e.attr("content"));
        return new UrlCheck(statusCode, title, h1, description, createdAt, Long.parseLong(idUrl));
    }

    private static String getValueFromHtml(Document document, String selector, Function<Element, String> function) {
        Elements elements = document.select(selector);
        if (!elements.isEmpty()) {
            return function.apply(elements.getFirst());
        }
        return "";
    }
}

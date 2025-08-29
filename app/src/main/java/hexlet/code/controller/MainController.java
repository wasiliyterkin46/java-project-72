package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.BuildUrlPage;
import io.javalin.http.Context;

import static io.javalin.rendering.template.TemplateUtil.model;

public class MainController {
    public static void index(Context ctx) {
        String enteredUrl = ctx.consumeSessionAttribute("textUrl");
        String textUrl = enteredUrl == null ? "" : enteredUrl;

        BuildUrlPage page = new BuildUrlPage(textUrl);
        page.setTextFlash(ctx.consumeSessionAttribute("textFlash"));
        page.setTypeFlash(ctx.consumeSessionAttribute("typeFlash"));

        ctx.render("index.jte", model("page", page));
    }
}

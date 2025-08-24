package hexlet.code.controller;

import io.javalin.http.Context;

public class MainController {
    public static void index(Context ctx) {
        ctx.render("index.jte");
    }
}

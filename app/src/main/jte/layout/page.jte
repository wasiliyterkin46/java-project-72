@import gg.jte.Content

@param Content content = null

<!doctype html>
<html lang="ru">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Page analyzer</title>
    </head>

    <body>
        @if (content != null)
            ${content}
        @endif
    </body>
</html>
@import hexlet.code.dto.BuildUrlPage
@import hexlet.code.util.NamedRoutes

@param BuildUrlPage page = null

@template.layout.page(
    page = page,
    content = @`
        <form action="${NamedRoutes.urlsPath()}" method="post">
            <div>
                <label>
                    URL
                    <input type="text" name="url" value="${page.getTextUrl()}" placeholder="Ссылка" />
                </label>
            </div>
            <input type="submit" value="Проверить" />
        </form>
    `
)

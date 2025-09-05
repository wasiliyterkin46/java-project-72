@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.util.NamedRoutes

@param UrlPage page = null

@template.layout.page(
    page = page,
    content = @`
        <div>
            <h1>Сайт: ${page.getUrl().getName()}</h1>
            <div>
                <table>
                    <tbody>
                        <tr>
                            <td>ID</td>
                            <td>${page.getUrl().getId()}</td>
                        </tr>
                        <tr>
                            <td>Имя</td>
                            <td>${page.getUrl().getName()}</td>
                        </tr>
                        <tr>
                            <td>Дата создания</td>
                            <td>${page.getUrl().getCreatedAtAsString()}</td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <h1>Проверки</h1>
            <div>
                <form action="${NamedRoutes.urlCheckPath(page.getUrl().getId())}" method="post">
                    <input type="submit" value="Запустить проверку" />
                </form>
            </div>

            @if(page.containsChecks())
                <div>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Код ответа</th>
                                <th>title</th>
                                <th>h1</th>
                                <th>description</th>
                                <th>Дата проверки</th>
                            </tr>
                        </thead>
                        <tbody>
                            @for(var check : page.getUrlChecks())
                                <tr>
                                    <td>${check.getId()}</td>
                                    <td>${check.getStatusCode()}</td>
                                    <td>${check.getTitle()}</td>
                                    <td>${check.getH1()}</td>
                                    <td>${check.getDescription()}</td>
                                    <td>${check.getCreatedAtAsString()}</td>
                                </tr>
                            @endfor
                        </tbody>
                    </table>
                </div>
            @endif
        </div>
    `
)

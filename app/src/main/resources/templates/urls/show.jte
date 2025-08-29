@import hexlet.code.dto.urls.UrlPage
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
                            <td>${page.getUrl().getCreatedAt().toString()}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    `
)

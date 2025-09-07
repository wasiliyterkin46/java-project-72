@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.util.NamedRoutes

@param UrlPage page = null

@template.layout.page(
    page = page,
    content = @`
    <main class="flex-grow-1">
        <section>
            <div class="container-lg mt-5">
                <h1>Сайт: ${page.getUrl().getName()}</h1>
                <table class="table table-bordered table-hover mt-3">
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

                <h2 class="mt-5">Проверки</h2>
                <form action="${NamedRoutes.urlCheckPath(page.getUrl().getId())}" method="post">
                    <input type="submit" value="Запустить проверку" />
                </form>

                @if(page.containsChecks())
                    <table class="table table-bordered table-hover mt-3">
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
                @endif
            </div>
        </section>
    </main>
    `
)

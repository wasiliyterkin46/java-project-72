@import hexlet.code.dto.urls.UrlsPage
@import hexlet.code.util.NamedRoutes

@param UrlsPage page = null

@template.layout.page(
    page = page,
    content = @`
        <main>
        	<section>
        		<div>
        			<h1>Сайты</h1>
        			<table>
        				<thead>
        					<tr>
        						<th>ID</th>
        						<th>Имя</th>
        						<th>Последняя проверка</th>
        						<th>Код ответа</th>
        					</tr>
        				</thead>
        				<tbody>
                            @if(!page.getUrls().isEmpty())
                                @for(var url : page.getUrls())
                                    <tr>
                                        <td>${url.getId()}</td>
                                        <td><a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a></td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                @endfor
                            @endif
        				</tbody>
        			</table>
        		</div>
        	</section>
        </main>
    `
)

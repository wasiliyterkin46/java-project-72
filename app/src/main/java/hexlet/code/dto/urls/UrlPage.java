package hexlet.code.dto.urls;

import hexlet.code.dto.BasePage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import lombok.Getter;
import lombok.AllArgsConstructor;
import java.util.List;

@AllArgsConstructor
@Getter
public class UrlPage extends BasePage {
    Url url;
    List<UrlCheck> urlChecks;

    public boolean containsChecks() {
        return urlChecks != null && !urlChecks.isEmpty();
    }
}

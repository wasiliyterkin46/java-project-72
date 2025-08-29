package hexlet.code.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Getter
public class BuildUrlPage extends BasePage {
    private String textUrl;
}

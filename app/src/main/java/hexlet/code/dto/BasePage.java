package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasePage {
    private String textFlash;
    private String typeFlash = "alert-light";
}

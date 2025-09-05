package hexlet.code.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import lombok.Getter;

@Getter
public class UrlCheck {
    Long id;
    Integer statusCode;
    String title;
    String h1;
    String description;
    Timestamp createdAt;
    Long urlId;

    public UrlCheck(Integer statusCode, String title, String h1, String description, Timestamp createdAt, Long urlId) {
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
        this.createdAt = createdAt;
        this.urlId = urlId;
    }

    public void setUrlId(Long urlId) {
        this.urlId = urlId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setId(String idString) {
        Long idLong = Long.parseLong(idString);
        setId(idLong);
    }

    public String getCreatedAtAsString() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(createdAt);
    }

}

package hexlet.code.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Url {
    Long id;
    String name;
    Timestamp createdAt;
    List<UrlCheck> urlChecks = new ArrayList<>();

    public Url(String name, Timestamp createdAt) {
        this.name = name;
        this.createdAt = createdAt;
    }
    public Url(Long id, String name, Timestamp createdAt) {
        this(name, createdAt);
        this.id = id;
    }

    public final String getCreatedAtAsString() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(createdAt);
    }
}

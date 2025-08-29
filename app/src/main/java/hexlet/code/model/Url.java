package hexlet.code.model;

import java.security.PublicKey;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Getter
@Setter
public class Url {
    Long id;
    String name;
    Timestamp createdAt;

    public Url(String name, Timestamp createdAt) {
        this.name = name;
        this.createdAt = createdAt;
    }
}

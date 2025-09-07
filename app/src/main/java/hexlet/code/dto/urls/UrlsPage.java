package hexlet.code.dto.urls;

import hexlet.code.dto.BasePage;
import hexlet.code.model.Url;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public final class UrlsPage extends BasePage {
    private List<UrlInfo> urls;

    public final static class UrlInfo extends Url {
        private Timestamp lastCheck;
        private Integer lastStatusCode;

        public UrlInfo(Long id, String name, Timestamp createdAt, Timestamp lastCheck, Integer lastStatusCode) {
            super(id, name, createdAt);
            this.lastCheck = lastCheck;
            this.lastStatusCode = lastStatusCode;
        }

        public String getLastCheck() {
            return lastCheck == null ? "" : new SimpleDateFormat("dd/MM/yyyy HH:mm").format(lastCheck);
        }

        public String getLastStatusCode() {
            return lastStatusCode == 0 ? "" : String.valueOf(lastStatusCode);
        }
    }
}

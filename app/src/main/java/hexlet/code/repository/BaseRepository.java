package hexlet.code.repository;

import javax.sql.DataSource;

public class BaseRepository {
    protected static DataSource dataSource;

    public BaseRepository(DataSource dataSrc) {
        dataSource = dataSrc;
    }

    protected BaseRepository() {
    }
}

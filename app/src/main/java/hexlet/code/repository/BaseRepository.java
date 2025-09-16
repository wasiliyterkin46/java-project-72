package hexlet.code.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.App;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class BaseRepository {
    protected static final DataSource DATA_SOURCE;

    static {
        DATA_SOURCE = getDataSource();
        createSchemaDataBase();
    }

    protected BaseRepository() { }

    private static DataSource getDataSource() {
        String dataBaseUrl = getDatabaseUrl();
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dataBaseUrl);
        DataSource dataSource = new HikariDataSource(hikariConfig);
        return dataSource;
    }

    private static String getDatabaseUrl() {
        return System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:hexlet_project;DB_CLOSE_DELAY=-1;");
    }

    public static void createSchemaDataBase() {
        var sql = getSqlShemaDataBase();

        try (var connection = DATA_SOURCE.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getSqlShemaDataBase() {
        var url = App.class.getClassLoader().getResourceAsStream("schema.sql");
        return new BufferedReader(new InputStreamReader(url))
                .lines().collect(Collectors.joining("\n"));
    }
}

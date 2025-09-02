package hexlet.code.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.App;
import hexlet.code.repository.BaseRepository;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class ConfigRepository {
    public static DataSource configureRepository() {
        String dataBaseUrl = getDatabaseUrl();
        return configureRepository(dataBaseUrl);
    }

    public static DataSource configureRepository(String dataBaseUrl) {
        DataSource dataSource = getDataSource(dataBaseUrl);
        BaseRepository.setDataSource(dataSource);
        createSchemaDataBase(dataSource);
        return dataSource;
    }

    public static void createSchemaDataBase(DataSource dataSource) {
        var sql = getSqlShemaDataBase();

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }

    private static DataSource getDataSource(String dataBaseUrl) {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dataBaseUrl);
        return new HikariDataSource(hikariConfig);
    }

    private static String getSqlShemaDataBase() {
        var url = App.class.getClassLoader().getResourceAsStream("schema.sql");
        return new BufferedReader(new InputStreamReader(url))
                .lines().collect(Collectors.joining("\n"));
    }

    private static String getDatabaseUrl() {
        return System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:hexlet_project;DB_CLOSE_DELAY=-1;");
    }
}

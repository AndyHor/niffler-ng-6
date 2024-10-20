package guru.qa.niffler.mydata;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Databases {
    private Databases(){
    }

    private static final Map<String, DataSource> datasources = new ConcurrentHashMap<>();

    private static DataSource dataSource(String jdbcUrl){
        return datasources.computeIfAbsent(
                jdbcUrl,
                key -> {
                    PGSimpleDataSource dataSource = new PGSimpleDataSource();
                    dataSource.setUser("postgres");
                    dataSource.setPassword("secret");
                    dataSource.setURL(key);
                    return dataSource;
                }
        );
    }

    public static Connection connection(String jdbcUrl) throws SQLException {
        return dataSource(jdbcUrl).getConnection();
    }
}

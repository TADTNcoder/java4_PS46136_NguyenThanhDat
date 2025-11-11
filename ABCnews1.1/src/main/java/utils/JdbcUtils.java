package utils;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Utility class for managing SQL Server database connections with pooling.
 * Uses HikariCP for efficient connection pooling.
 */
public class JdbcUtils {

    private static final String DEFAULT_URL =
            "jdbc:sqlserver://localhost:1433;databaseName=NewsPortal;encrypt=true;trustServerCertificate=true";
    private static final String DEFAULT_USER = "sa";
    private static final String DEFAULT_PASSWORD = "";

    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();

        // Load from environment variables if available, otherwise use defaults
        String url = System.getenv().getOrDefault("DB_URL", DEFAULT_URL);
        String user = System.getenv().getOrDefault("DB_USER", DEFAULT_USER);
        String password = System.getenv().getOrDefault("DB_PASSWORD", DEFAULT_PASSWORD);

        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);

        // Pool settings
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);      // 30s
        config.setMaxLifetime(1800000);    // 30m
        config.setConnectionTimeout(10000);// 10s

        // SQL Server driver
        config.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        dataSource = new HikariDataSource(config);
    }

    private JdbcUtils() {
        // Prevent instantiation
    }

    /**
     * Get a pooled database connection.
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Close the pool when shutting down the application.
     */
    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}


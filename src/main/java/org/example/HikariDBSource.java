package org.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class HikariDBSource {
    private HikariDBSource() {}

    private static final HikariDataSource ds;

    static {
        final Properties prop = new Properties();
        try {
            prop.load(HikariDBSource.class.getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }

        createDatabaseIfNotExists(prop);

        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/" + prop.getProperty("db.name"));
        config.setUsername(prop.getProperty("db.username"));
        config.setPassword(prop.getProperty("db.password"));

        ds = new HikariDataSource(config);

        runSqlScript("/schema.sql");
    }

    private static void createDatabaseIfNotExists(Properties prop) {
        HikariConfig tempConfig = new HikariConfig();
        tempConfig.setJdbcUrl("jdbc:mysql://localhost:3306/mysql");
        tempConfig.setUsername(prop.getProperty("db.username"));
        tempConfig.setPassword(prop.getProperty("db.password"));
        try (HikariDataSource tempDs = new HikariDataSource(tempConfig);
             Connection conn = tempDs.getConnection();
             Statement stmt = conn.createStatement()) {

            String dbName = prop.getProperty("db.name");
            ResultSet resultSet = conn.getMetaData().getCatalogs();
            boolean dbExists = false;
            while (resultSet.next()) {
                String databaseName = resultSet.getString(1);
                if (databaseName.equals(dbName)) {
                    dbExists = true;
                    break;
                }
            }
            resultSet.close();
            if (!dbExists) {
                stmt.executeUpdate("CREATE DATABASE " + dbName);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create database", e);
        }
    }

    private static void runSqlScript(String scriptPath) {
        try (Connection conn = ds.getConnection();
             BufferedReader reader = new BufferedReader(new InputStreamReader(HikariDBSource.class.getResourceAsStream(scriptPath)));
             Statement stmt = conn.createStatement()) {
            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line);
                if (line.trim().endsWith(";")) {
                    stmt.execute(sql.toString());
                    sql.setLength(0);  // Clear the buffer
                }
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Failed to execute SQL script", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}

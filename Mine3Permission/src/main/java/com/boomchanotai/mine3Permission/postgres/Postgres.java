package com.boomchanotai.mine3Permission.postgres;

import org.postgresql.Driver;

import com.boomchanotai.mine3Permission.logger.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.boomchanotai.mine3Permission.config.Config.*;

public class Postgres {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect();
        }

        return connection;
    }

    public static void connect() {
        try {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection(POSTGRES_HOST, POSTGRES_USERNAME, POSTGRES_PASSWORD);
            initializeDatabase();
        } catch (SQLException e) {
            Logger.warning(e.getMessage(), "Fail to connect database.");
        }

        Logger.info("Connected to database!");
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            Logger.warning(e.getMessage(), "Fail to disconnect database.");
        }

        Logger.info("Disconnected to database!");
    }

    public static void initializeDatabase() throws SQLException {
        Statement statement = getConnection().createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS \"groups\" (\n" +
                "\t\"address\" TEXT PRIMARY KEY,\n" +
                "\t\"group\" TEXT NOT NULL,\n" +
                "\t\"metadata\" JSONB NOT NULL DEFAULT '{}',\n" +
                "\t\"created_at\" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "\t\"updated_at\" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP\n" +
                ");");
    }
}

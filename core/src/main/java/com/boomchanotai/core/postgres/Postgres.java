package com.boomchanotai.core.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.postgresql.Driver;

import com.boomchanotai.core.logger.Logger;

public class Postgres {
    private static Connection connection;

    private static String host;
    private static String username;
    private static String password;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            Logger.getLogger().warning(e.getMessage(), "Fail to check connection status.");
        }

        return connection;
    }

    public static void init(String host, String username, String password) {
        Postgres.host = host;
        Postgres.username = username;
        Postgres.password = password;
        connect();
    }

    public static void connect() {
        try {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection(host, username, password);
        } catch (SQLException e) {
            Logger.getLogger().warning(e.getMessage(), "Fail to connect database.");
        }

        Logger.getLogger().info("Connected to database!");
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            Logger.getLogger().warning(e.getMessage(), "Fail to disconnect database.");
        }

        Logger.getLogger().info("Disconnected to database!");
    }
}

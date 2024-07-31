package com.boomchanotai.mine3Lib.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.postgresql.Driver;

import com.boomchanotai.mine3Lib.logger.Logger;

import static com.boomchanotai.mine3Lib.config.Config.*;

public class Postgres {
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect();
        }

        return connection;
    }

    public static void connect() {
        if (!POSTGRES_ENABLE)
            return;

        try {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection(POSTGRES_HOST, POSTGRES_USERNAME, POSTGRES_PASSWORD);
        } catch (SQLException e) {
            Logger.warning(e.getMessage(), "Fail to connect database.");
        }

        Logger.info("Connected to database!");
    }

    public static void disconnect() {
        if (!POSTGRES_ENABLE)
            return;

        try {
            connection.close();
        } catch (SQLException e) {
            Logger.warning(e.getMessage(), "Fail to disconnect database.");
        }

        Logger.info("Disconnected to database!");
    }
}

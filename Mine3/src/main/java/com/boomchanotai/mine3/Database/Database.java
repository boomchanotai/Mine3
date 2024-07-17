package com.boomchanotai.mine3.Database;

import com.boomchanotai.mine3.Mine3;
import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.boomchanotai.mine3.Config.Config.*;

public class Database {
    public static Connection connection;
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect();
        }

        return connection;
    }

    public static void connect() throws SQLException {
        DriverManager.registerDriver(new Driver());
        connection = DriverManager.getConnection(POSTGRES_HOST, POSTGRES_USERNAME, POSTGRES_PASSWORD);

        Mine3.getInstance().getLogger().info(LOG_TITLE + "Connected to database!");
    }

    public static void disconnect() throws SQLException {
        connection.close();

        Mine3.getInstance().getLogger().info(LOG_TITLE + "Disconnected to database!");
    }
}

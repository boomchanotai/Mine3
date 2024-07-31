package com.boomchanotai.mine3Auth.postgres;

import org.postgresql.Driver;

import com.boomchanotai.mine3Lib.logger.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.boomchanotai.mine3Auth.config.Config.*;

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
        statement.execute("CREATE TABLE IF NOT EXISTS \"users\" (\n" +
                "\t\"address\" TEXT PRIMARY KEY,\n" +
                "\t\"is_logged_in\" BOOLEAN NOT NULL DEFAULT FALSE,\n" +
                "\t\"xp_level\" INTEGER NOT NULL DEFAULT 0,\n" +
                "\t\"xp_exp\" FLOAT NOT NULL DEFAULT 0,\n" +
                "\t\"health\" DECIMAL NOT NULL DEFAULT 20,\n" +
                "\t\"food_level\" INTEGER NOT NULL DEFAULT 20,\n" +
                "\t\"game_mode\" TEXT NOT NULL DEFAULT 'SURVIVAL',\n" +
                "\t\"fly_speed\" FLOAT NOT NULL DEFAULT 0.1,\n" +
                "\t\"walk_speed\" FLOAT NOT NULL DEFAULT 0.2,\n" +
                "\t\"is_flying\" BOOLEAN NOT NULL DEFAULT FALSE,\n" +
                "\t\"is_op\" BOOLEAN NOT NULL DEFAULT FALSE,\n" +
                "\t\"potion_effects\" JSONB NOT NULL DEFAULT '[]',\n" +
                "\t\"inventory\" JSONB NOT NULL DEFAULT '[]',\n" +
                "\t\"ender_chest\" JSONB NOT NULL DEFAULT '[]',\n" +
                "\t\"last_location_x\" DECIMAL NOT NULL DEFAULT 0,\n" +
                "\t\"last_location_y\" DECIMAL NOT NULL DEFAULT 0,\n" +
                "\t\"last_location_z\" DECIMAL NOT NULL DEFAULT 0,\n" +
                "\t\"last_location_yaw\" FLOAT NOT NULL DEFAULT 0,\n" +
                "\t\"last_location_pitch\" FLOAT NOT NULL DEFAULT 0,\n" +
                "\t\"last_location_world\" TEXT NOT NULL DEFAULT 'world',\n" +
                "\t\"metadata\" JSONB NOT NULL DEFAULT '{}',\n" +
                "\t\"created_at\" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "\t\"last_login\" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP\n" +
                ");");
    }
}

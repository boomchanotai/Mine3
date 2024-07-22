package com.boomchanotai.mine3.Database;

import com.boomchanotai.mine3.Logger.Logger;
import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.boomchanotai.mine3.Config.Config.*;

public class Database {
    private static Connection connection;
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect();
        }

        return connection;
    }

    public static void connect() throws SQLException {
        DriverManager.registerDriver(new Driver());
        connection = DriverManager.getConnection(POSTGRES_HOST, POSTGRES_USERNAME, POSTGRES_PASSWORD);

        initializeDatabase();

        Logger.info("Connected to database!");
    }

    public static void disconnect() throws SQLException {
        connection.close();

        Logger.info("Disconnected to database!");
    }

    public static void initializeDatabase() throws SQLException {
        Statement statement = getConnection().createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS \"users\" (\n" +
                "\t\"address\" VARCHAR PRIMARY KEY,\n" +
                "\t\"is_logged_in\" BOOLEAN NOT NULL DEFAULT FALSE,\n" +
                "\t\"xp_level\" INTEGER NOT NULL DEFAULT 0,\n" +
                "\t\"xp_exp\" FLOAT NOT NULL DEFAULT 0,\n" +
                "\t\"health\" DECIMAL NOT NULL DEFAULT 20,\n" +
                "\t\"food_level\" INTEGER NOT NULL DEFAULT 20,\n" +
                "\t\"armor\" BYTEA,\n" +
                "\t\"inventory\" BYTEA,\n" +
                "\t\"ender_chest\" BYTEA,\n" +
                "\t\"last_location_x\" DECIMAL NOT NULL DEFAULT 0,\n" +
                "\t\"last_location_y\" DECIMAL NOT NULL DEFAULT 0,\n" +
                "\t\"last_location_z\" DECIMAL NOT NULL DEFAULT 0,\n" +
                "\t\"last_location_yaw\" FLOAT NOT NULL DEFAULT 0,\n" +
                "\t\"last_location_pitch\" FLOAT NOT NULL DEFAULT 0,\n" +
                "\t\"last_location_world\" VARCHAR NOT NULL DEFAULT 'world',\n" +
                "\t\"metadata\" JSONB NOT NULL DEFAULT '{}',\n" +
                "\t\"created_at\" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "\t\"last_login\" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP\n" +
                ");");
    }
}

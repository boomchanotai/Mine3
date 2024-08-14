package com.boomchanotai.core.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.postgresql.Driver;

public class Postgres {
    private String host;
    private String username;
    private String password;
    private Connection connection;

    public Postgres(String host, String username, String password) throws SQLException {
        this.host = host;
        this.username = username;
        this.password = password;
        connect();
    }

    public Connection getConnection() throws SQLException {
        if (connection == null) {
            connect();
        }

        return connection;
    }

    public void connect() throws SQLException {
        DriverManager.registerDriver(new Driver());
        connection = DriverManager.getConnection(host, username, password);
    }

    public void disconnect() throws SQLException {
        connection.close();
    }
}

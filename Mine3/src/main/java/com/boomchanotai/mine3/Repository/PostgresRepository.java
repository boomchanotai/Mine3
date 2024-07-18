package com.boomchanotai.mine3.Repository;

import com.boomchanotai.mine3.Database.Database;
import com.boomchanotai.mine3.Logger.Logger;
import org.web3j.crypto.Keys;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostgresRepository {
    public static void createNewPlayer(String address, double last_location_x, double last_location_y, double last_location_z, float last_location_yaw, float last_location_pitch, String  last_location_world) throws SQLException {
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement("INSERT INTO users(address, is_logged_in, last_location_x, last_location_y, last_location_z, last_location_yaw, last_location_pitch, last_location_world) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT DO NOTHING");

        // set address
        String parsedAddress = Keys.toChecksumAddress(address);
        preparedStatement.setString(1, parsedAddress);
        // set is_logged_in
        preparedStatement.setBoolean(2, true);
        // set last_location_x
        preparedStatement.setDouble(3, last_location_x);
        // set last_location_y
        preparedStatement.setDouble(4, last_location_y);
        // set last_location_z
        preparedStatement.setDouble(5, last_location_z);
        // set last_location_yaw
        preparedStatement.setFloat(6, last_location_yaw);
        // set last_location_pitch
        preparedStatement.setFloat(7, last_location_pitch);
        // set last_location_world
        preparedStatement.setString(8, last_location_world);
        preparedStatement.executeUpdate();
    }
}

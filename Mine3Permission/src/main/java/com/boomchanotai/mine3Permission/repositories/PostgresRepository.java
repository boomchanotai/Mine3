package com.boomchanotai.mine3Permission.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.boomchanotai.mine3Lib.address.Address;
import com.boomchanotai.mine3Permission.logger.Logger;
import com.boomchanotai.mine3Permission.postgres.Postgres;

public class PostgresRepository {
    public void createGroup(Address address, String group) {
        String query = "INSERT INTO groups (address, \"group\") \n" +
                "VALUES (?, ?) \n" +
                "ON CONFLICT (address) \n" +
                "DO UPDATE SET \"group\" = EXCLUDED.\"group\";";

        try {
            PreparedStatement preparedStatement = Postgres.getConnection().prepareStatement(query);
            preparedStatement.setString(1, address.getValue());
            preparedStatement.setString(2, group);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.warning(e.getMessage(), "failed to create group");
        }
    }

    public String getGroup(Address address) {
        String query = "SELECT * FROM groups WHERE address = ?";

        try {
            PreparedStatement preparedStatement = Postgres.getConnection().prepareStatement(query);
            preparedStatement.setString(1, address.getValue());
            ResultSet res = preparedStatement.executeQuery();

            if (res.next()) {
                return res.getString("group");
            }
        } catch (SQLException e) {
            Logger.warning(e.getMessage(), "failed to get group");
        }

        return null;
    }
}

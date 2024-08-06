package com.boomchanotai.mine3Permission.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.boomchanotai.mine3Lib.address.Address;
import com.boomchanotai.mine3Permission.logger.Logger;
import com.boomchanotai.mine3Permission.postgres.Postgres;

public class PostgresRepository {
    public void createGroup(Address address, String group) {
        String query = "INSERT INTO groups (address, \"group\") VALUES (?, ?) ON CONFLICT DO NOTHING";

        try {
            PreparedStatement preparedStatement = Postgres.getConnection().prepareStatement(query);
            preparedStatement.setString(1, address.getValue());
            preparedStatement.setString(2, group);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.warning(e.getMessage(), "failed to create group");
        }
    }

    public void updateGroup(Address address, String group) {
        String query = "UPDATE groups SET group = ? WHERE address = ?";

        try {
            PreparedStatement preparedStatement = Postgres.getConnection().prepareStatement(query);
            preparedStatement.setString(1, group);
            preparedStatement.setString(2, address.getValue());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.warning(e.getMessage(), "failed to update group");
        }
    }

    public String getGroup() {
        String query = "SELECT * FROM groups WHERE address = ?";

        try {
            PreparedStatement preparedStatement = Postgres.getConnection().prepareStatement(query);
            preparedStatement.setString(1, "address");
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

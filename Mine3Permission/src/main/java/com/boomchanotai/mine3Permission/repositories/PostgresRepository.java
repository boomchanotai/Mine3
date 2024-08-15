package com.boomchanotai.mine3Permission.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.boomchanotai.mine3Lib.core.entities.Address;
import com.boomchanotai.core.logger.Logger;
import com.boomchanotai.core.postgres.Postgres;

import static com.boomchanotai.mine3Permission.config.Config.POSTGRES_GROUP_TABLE;

public class PostgresRepository {
    public void createGroup(Address address, String group) {
        String query = "INSERT INTO " + POSTGRES_GROUP_TABLE + " (address, \"group\") \n" +
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
        String query = "SELECT * FROM " + POSTGRES_GROUP_TABLE + " WHERE address = ?";

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

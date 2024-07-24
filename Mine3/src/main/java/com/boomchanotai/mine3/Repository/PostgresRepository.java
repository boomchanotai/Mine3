package com.boomchanotai.mine3.Repository;

import com.boomchanotai.mine3.Database.Database;
import com.boomchanotai.mine3.Entity.PlayerData;
import com.boomchanotai.mine3.Entity.PlayerLocation;
import com.boomchanotai.mine3.Logger.Logger;
import com.boomchanotai.mine3.Mine3;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.web3j.crypto.Keys;

import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

public class PostgresRepository {
    public PlayerData getPlayerData(String address) {
        String parsedAddress = Keys.toChecksumAddress(address);

        try {
            PreparedStatement preparedStatement = Database.getConnection()
                    .prepareStatement("SELECT * FROM users WHERE address = ?");
            preparedStatement.setString(1, parsedAddress);
            ResultSet res = preparedStatement.executeQuery();

            if (res.next()) {
                double lastLocationX = res.getDouble("last_location_x");
                double lastLocationY = res.getDouble("last_location_y");
                double lastLocationZ = res.getDouble("last_location_z");
                float lastLocationYaw = res.getFloat("last_location_yaw");
                float lastLocationPitch = res.getFloat("last_location_pitch");
                String lastLocationWorld = res.getString("last_location_world");
                World world = Mine3.getInstance().getServer().getWorld(lastLocationWorld);

                PlayerLocation playerLocation = new PlayerLocation(lastLocationX, lastLocationY, lastLocationZ,
                        lastLocationYaw, lastLocationPitch, world);

                boolean isLoggedIn = res.getBoolean("is_logged_in");
                int xpLevel = res.getInt("xp_level");
                float xpExp = res.getFloat("xp_exp");
                int health = res.getInt("health");
                int foodLevel = res.getInt("food_level");

                PlayerData playerData = new PlayerData(parsedAddress, isLoggedIn, xpLevel, xpExp, health, foodLevel,
                        playerLocation);

                return playerData;
            }
        } catch (Exception e) {
            Logger.warning(e.getMessage());
        }

        return null;
    }

    // TODO: Use playerData to create player
    public void createNewPlayer(String address, double lastLocationX, double lastLocationY, double lastLocationZ,
            float lastLocationYaw, float lastLocationPitch, String lastLocationWorld) throws SQLException {
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement(
                "INSERT INTO users(address, is_logged_in, last_location_x, last_location_y, last_location_z, last_location_yaw, last_location_pitch, last_location_world) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT DO NOTHING");

        // set address
        String parsedAddress = Keys.toChecksumAddress(address);
        preparedStatement.setString(1, parsedAddress);
        // set is_logged_in
        preparedStatement.setBoolean(2, true);
        // set last_location_x
        preparedStatement.setDouble(3, lastLocationX);
        // set last_location_y
        preparedStatement.setDouble(4, lastLocationY);
        // set last_location_z
        preparedStatement.setDouble(5, lastLocationZ);
        // set last_location_yaw
        preparedStatement.setFloat(6, lastLocationYaw);
        // set last_location_pitch
        preparedStatement.setFloat(7, lastLocationPitch);
        // set last_location_world
        preparedStatement.setString(8, lastLocationWorld);
        preparedStatement.executeUpdate();
    }

    // TODO: Use playerData to update player
    public void updateUserInventory(String address, int xpLevel, float xpExp, double health, int foodLevel,
            ItemStack[] armor, ItemStack[] inventory, ItemStack[] enderChest, double lastLocationX,
            double lastLocationY, double lastLocationZ, float lastLocationYaw, float lastLocationPitch,
            String lastLocationWorld) throws SQLException {
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement(
                "UPDATE users SET is_logged_in = ?, xp_level = ?, xp_exp = ?, health = ?, food_level = ?, armor = ?, inventory = ?, ender_chest = ?, last_location_x = ?, last_location_y = ?, last_location_z = ?, last_location_yaw = ?, last_location_pitch = ?, last_location_world = ? WHERE address = ?");

        ArrayList<ItemStack> armorArr = new ArrayList<>(Arrays.asList(armor));
        ArrayList<ItemStack> invArr = new ArrayList<>(Arrays.asList(inventory));
        ArrayList<ItemStack> enderChestArr = new ArrayList<>(Arrays.asList(enderChest));

        // set is_logged_in
        preparedStatement.setBoolean(1, false);
        // set xp level
        preparedStatement.setInt(2, xpLevel);
        // set xp exp
        preparedStatement.setFloat(3, xpExp);
        // set health
        preparedStatement.setDouble(4, health);
        // set food level
        preparedStatement.setInt(5, foodLevel);
        // set armor
        preparedStatement.setBytes(6, Base64.getEncoder().encode(armorArr.toString().getBytes(StandardCharsets.UTF_8)));
        // set inventory
        preparedStatement.setBytes(7, Base64.getEncoder().encode(invArr.toString().getBytes(StandardCharsets.UTF_8)));
        // set ender chest
        preparedStatement.setBytes(8,
                Base64.getEncoder().encode(enderChestArr.toString().getBytes(StandardCharsets.UTF_8)));
        // set last_location_x
        preparedStatement.setDouble(9, lastLocationX);
        // set last_location_y
        preparedStatement.setDouble(10, lastLocationY);
        // set last_location_z
        preparedStatement.setDouble(11, lastLocationZ);
        // set last_location_yaw
        preparedStatement.setFloat(12, lastLocationYaw);
        // set last_location_pitch
        preparedStatement.setFloat(13, lastLocationPitch);
        // set last_location_world
        preparedStatement.setString(14, lastLocationWorld);
        // set address
        String parsedAddress = Keys.toChecksumAddress(address);
        preparedStatement.setString(15, parsedAddress);
        preparedStatement.executeUpdate();
    }

    public void setUserLoggedIn(String address) throws SQLException {
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement(
                "UPDATE users SET is_logged_in = true, last_login = CURRENT_TIMESTAMP WHERE address = ?");

        // set address
        String parsedAddress = Keys.toChecksumAddress(address);
        preparedStatement.setString(1, parsedAddress);
        preparedStatement.executeUpdate();
    }

}

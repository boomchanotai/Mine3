package com.boomchanotai.mine3.Repository;

import com.boomchanotai.mine3.Database.Database;
import com.boomchanotai.mine3.Entity.PlayerData;
import com.boomchanotai.mine3.Entity.PlayerLocation;
import com.boomchanotai.mine3.Logger.Logger;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.boomchanotai.mine3.Mine3;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.postgresql.util.PGobject;
import org.web3j.crypto.Keys;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class PostgresRepository {
    public boolean isPlayerExist(String address) {
        String parsedAddress = Keys.toChecksumAddress(address);

        try {
            PreparedStatement preparedStatement = Database.getConnection()
                    .prepareStatement("SELECT * FROM users WHERE address = ?");
            preparedStatement.setString(1, parsedAddress);
            ResultSet res = preparedStatement.executeQuery();

            return res.next();
        } catch (Exception e) {
            Logger.warning(e.getMessage());
        }

        return false;
    }

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

                ObjectMapper objectMapper = new ObjectMapper();

                ArrayList<ItemStack> inventoryList = new ArrayList<>();
                PGobject inventoryObject = (PGobject) res.getObject("inventory");
                if (inventoryObject != null) {
                    String inventoryString = inventoryObject.getValue();
                    JsonNode inventoryNode = objectMapper.readTree(inventoryString);

                    if (!inventoryNode.isArray()) {
                        Logger.warning("Inventory is not an array");
                    }

                    for (JsonNode itemNode : inventoryNode) {
                        if (itemNode.isNull()) {
                            inventoryList.add(null);
                            continue;
                        }

                        TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {
                        };
                        Map<String, Object> itemMap = objectMapper.convertValue(itemNode, typeRef);
                        inventoryList.add(ItemStack.deserialize(itemMap));
                    }
                }

                ItemStack[] inventory = inventoryList.toArray(new ItemStack[inventoryList.size()]);

                PlayerData playerData = new PlayerData(parsedAddress, isLoggedIn, xpLevel, xpExp, health, foodLevel,
                        inventory, new ItemStack[0], playerLocation);

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
                "UPDATE users SET is_logged_in = ?, xp_level = ?, xp_exp = ?, health = ?, food_level = ?, inventory = ?, ender_chest = ?, last_location_x = ?, last_location_y = ?, last_location_z = ?, last_location_yaw = ?, last_location_pitch = ?, last_location_world = ? WHERE address = ?");

        ArrayList<String> inventoryList = new ArrayList<>();
        for (ItemStack item : inventory) {
            if (item == null) {
                inventoryList.add(null);
                continue;
            }

            String itemString = ItemStackAdapter.serializeToJsonString(item);
            inventoryList.add(itemString);
        }

        ArrayList<String> enderchestList = new ArrayList<>();
        for (ItemStack item : enderChest) {
            if (item == null) {
                enderchestList.add(null);
                continue;
            }

            String itemString = ItemStackAdapter.serializeToJsonString(item);
            enderchestList.add(itemString);
        }

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
        // set inventory
        PGobject inventoryObject = new PGobject();
        inventoryObject.setType("json");
        inventoryObject.setValue(inventoryList.toString());
        preparedStatement.setObject(6, inventoryObject);
        // set ender chest
        PGobject enderchestObject = new PGobject();
        enderchestObject.setType("json");
        enderchestObject.setValue(enderchestList.toString());
        preparedStatement.setObject(7, enderchestObject);
        // set last_location_x
        preparedStatement.setDouble(8, lastLocationX);
        // set last_location_y
        preparedStatement.setDouble(9, lastLocationY);
        // set last_location_z
        preparedStatement.setDouble(10, lastLocationZ);
        // set last_location_yaw
        preparedStatement.setFloat(11, lastLocationYaw);
        // set last_location_pitch
        preparedStatement.setFloat(12, lastLocationPitch);
        // set last_location_world
        preparedStatement.setString(13, lastLocationWorld);
        // set address
        String parsedAddress = Keys.toChecksumAddress(address);
        preparedStatement.setString(14, parsedAddress);
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

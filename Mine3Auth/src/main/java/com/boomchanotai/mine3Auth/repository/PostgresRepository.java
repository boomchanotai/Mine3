package com.boomchanotai.mine3Auth.repository;

import com.boomchanotai.mine3Auth.entity.PlayerData;
import com.boomchanotai.mine3Auth.entity.PlayerLocation;
import com.boomchanotai.mine3Auth.postgres.Postgres;
import com.boomchanotai.mine3Auth.logger.Logger;
import com.boomchanotai.mine3Auth.Mine3Auth;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.postgresql.util.PGobject;
import org.web3j.crypto.Keys;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class PostgresRepository {
    private ItemStackAdapter itemStackAdapter;
    private PotionEffectAdapter potionEffectAdapter;

    public PostgresRepository(ItemStackAdapter itemStackAdapter, PotionEffectAdapter potionEffectAdapter) {
        this.itemStackAdapter = itemStackAdapter;
        this.potionEffectAdapter = potionEffectAdapter;
    }

    public boolean isAddressExist(String address) {
        String parsedAddress = Keys.toChecksumAddress(address);

        try {
            PreparedStatement preparedStatement = Postgres.getConnection()
                    .prepareStatement("SELECT * FROM users WHERE address = ?");
            preparedStatement.setString(1, parsedAddress);
            ResultSet res = preparedStatement.executeQuery();

            return res.next();
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "failed to check if player exist");
        }

        return false;
    }

    public PlayerData getPlayerData(String address) {
        String parsedAddress = Keys.toChecksumAddress(address);

        try {
            PreparedStatement preparedStatement = Postgres.getConnection()
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
                World world = Mine3Auth.getInstance().getServer().getWorld(lastLocationWorld);

                PlayerLocation playerLocation = new PlayerLocation(lastLocationX, lastLocationY, lastLocationZ,
                        lastLocationYaw, lastLocationPitch, world);

                boolean isLoggedIn = res.getBoolean("is_logged_in");
                int xpLevel = res.getInt("xp_level");
                float xpExp = res.getFloat("xp_exp");
                int health = res.getInt("health");
                int foodLevel = res.getInt("food_level");
                GameMode gameMode = GameMode.valueOf(res.getString("game_mode"));
                float flySpeed = res.getFloat("fly_speed");
                float walkSpeed = res.getFloat("walk_speed");
                boolean isFlying = res.getBoolean("is_flying");
                boolean isOp = res.getBoolean("is_op");

                // Potion Effects
                PGobject potionEffectsObject = (PGobject) res.getObject("potion_effects");
                Collection<PotionEffect> potionEffects = potionEffectAdapter
                        .ParsePotionEffectsFromPGObject(potionEffectsObject);

                // Inventory List
                PGobject inventoryObject = (PGobject) res.getObject("inventory");
                ItemStack[] inventory = itemStackAdapter.ParseItemStackListFromPGObject(inventoryObject);

                // Ender Chest List
                PGobject enderChestObject = (PGobject) res.getObject("ender_chest");
                ItemStack[] enderChest = itemStackAdapter.ParseItemStackListFromPGObject(enderChestObject);

                PlayerData playerData = new PlayerData(parsedAddress, isLoggedIn, xpLevel, xpExp, health, foodLevel,
                        gameMode, flySpeed, walkSpeed, isFlying, isOp, potionEffects, inventory, enderChest,
                        playerLocation);

                return playerData;
            }
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "failed to get player data");
        }

        return null;
    }

    public void createNewPlayer(PlayerData playerData) throws SQLException {
        PreparedStatement preparedStatement = Postgres.getConnection().prepareStatement(
                "INSERT INTO users(address, is_logged_in, last_location_x, last_location_y, last_location_z, last_location_yaw, last_location_pitch, last_location_world) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT DO NOTHING");

        // set address
        preparedStatement.setString(1, playerData.getAddress());
        // set is_logged_in
        preparedStatement.setBoolean(2, true);
        // set last_location_x
        preparedStatement.setDouble(3, playerData.getPlayerLocation().getX());
        // set last_location_y
        preparedStatement.setDouble(4, playerData.getPlayerLocation().getY());
        // set last_location_z
        preparedStatement.setDouble(5, playerData.getPlayerLocation().getZ());
        // set last_location_yaw
        preparedStatement.setFloat(6, playerData.getPlayerLocation().getYaw());
        // set last_location_pitch
        preparedStatement.setFloat(7, playerData.getPlayerLocation().getPitch());
        // set last_location_world
        preparedStatement.setString(8, playerData.getPlayerLocation().getWorld().getName());
        preparedStatement.executeUpdate();
    }

    public void updateUserInventory(PlayerData playerData) throws SQLException {
        PreparedStatement preparedStatement = Postgres.getConnection().prepareStatement(
                "UPDATE users SET is_logged_in = ?, xp_level = ?, xp_exp = ?, health = ?, food_level = ?, game_mode = ?, fly_speed = ?, walk_speed = ?, is_flying = ?, is_op = ?, potion_effects = ?, inventory = ?, ender_chest = ?, last_location_x = ?, last_location_y = ?, last_location_z = ?, last_location_yaw = ?, last_location_pitch = ?, last_location_world = ? WHERE address = ?");

        // set is_logged_in
        preparedStatement.setBoolean(1, false);
        // set xp level
        preparedStatement.setInt(2, playerData.getXpLevel());
        // set xp exp
        preparedStatement.setFloat(3, playerData.getXpExp());
        // set health
        preparedStatement.setDouble(4, playerData.getHealth());
        // set food level
        preparedStatement.setInt(5, playerData.getFoodLevel());
        // set game mode
        preparedStatement.setString(6, playerData.getGameMode().toString());
        // set fly speed
        preparedStatement.setFloat(7, playerData.getFlySpeed());
        // set walk speed
        preparedStatement.setFloat(8, playerData.getWalkSpeed());
        // set is flying
        preparedStatement.setBoolean(9, playerData.isFlying());
        // set is op
        preparedStatement.setBoolean(10, playerData.isOp());
        // set potion effects
        PGobject potionEffectsObject = potionEffectAdapter
                .ParsePGObjectFromPotionEffects(playerData.getPotionEffects());
        preparedStatement.setObject(11, potionEffectsObject);
        // set inventory
        PGobject inventoryObject = itemStackAdapter.ParsePGObjectFromItemStackList(playerData.getInventory());
        preparedStatement.setObject(12, inventoryObject);
        // set ender chest
        PGobject enderchestObject = itemStackAdapter.ParsePGObjectFromItemStackList(playerData.getEnderchest());
        preparedStatement.setObject(13, enderchestObject);
        // set last_location_x
        preparedStatement.setDouble(14, playerData.getPlayerLocation().getX());
        // set last_location_y
        preparedStatement.setDouble(15, playerData.getPlayerLocation().getY());
        // set last_location_z
        preparedStatement.setDouble(16, playerData.getPlayerLocation().getZ());
        // set last_location_yaw
        preparedStatement.setFloat(17, playerData.getPlayerLocation().getYaw());
        // set last_location_pitch
        preparedStatement.setFloat(18, playerData.getPlayerLocation().getPitch());
        // set last_location_world
        preparedStatement.setString(19, playerData.getPlayerLocation().getWorld().getName());
        // set address
        String parsedAddress = Keys.toChecksumAddress(playerData.getAddress());
        preparedStatement.setString(20, parsedAddress);
        preparedStatement.executeUpdate();
    }

    public void setUserLoggedIn(String address) throws SQLException {
        PreparedStatement preparedStatement = Postgres.getConnection().prepareStatement(
                "UPDATE users SET is_logged_in = true, last_login = CURRENT_TIMESTAMP WHERE address = ?");

        // set address
        String parsedAddress = Keys.toChecksumAddress(address);
        preparedStatement.setString(1, parsedAddress);
        preparedStatement.executeUpdate();
    }

}

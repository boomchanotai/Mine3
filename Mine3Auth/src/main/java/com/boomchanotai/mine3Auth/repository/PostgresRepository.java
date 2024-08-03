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
import java.util.ArrayList;
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
        String query = "SELECT * FROM users WHERE address = ?";

        try {
            PreparedStatement preparedStatement = Postgres.getConnection()
                    .prepareStatement(query);
            preparedStatement.setString(1, parsedAddress);
            ResultSet res = preparedStatement.executeQuery();

            if (res.next()) {
                double lastLocationX = res.getDouble("last_location_x");
                double lastLocationY = res.getDouble("last_location_y");
                double lastLocationZ = res.getDouble("last_location_z");
                float lastLocationYaw = res.getFloat("last_location_yaw");
                float lastLocationPitch = res.getFloat("last_location_pitch");
                String lastLocationWorld = res.getString("last_location_world");
                World world = Mine3Auth.getPlugin().getServer().getWorld(lastLocationWorld);

                PlayerLocation playerLocation = new PlayerLocation(lastLocationX, lastLocationY, lastLocationZ,
                        lastLocationYaw, lastLocationPitch, world);

                String ensDomain = res.getString("ens_domain");
                boolean isLoggedIn = res.getBoolean("is_logged_in");
                int xpLevel = res.getInt("xp_level");
                float xpExp = res.getFloat("xp_exp");
                int health = res.getInt("health");
                int foodLevel = res.getInt("food_level");
                GameMode gameMode = GameMode.valueOf(res.getString("game_mode"));
                float flySpeed = res.getFloat("fly_speed");
                float walkSpeed = res.getFloat("walk_speed");
                boolean allowFlight = res.getBoolean("allow_flight");
                boolean isFlying = res.getBoolean("is_flying");
                boolean isOp = res.getBoolean("is_op");
                boolean isBanned = res.getBoolean("is_banned");

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

                PlayerData playerData = new PlayerData(parsedAddress, ensDomain, isLoggedIn, xpLevel, xpExp, health,
                        foodLevel,
                        gameMode, flySpeed, walkSpeed, allowFlight, isFlying, isOp, isBanned, potionEffects, inventory,
                        enderChest,
                        playerLocation);

                return playerData;
            }
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "failed to get player data");
        }

        return null;
    }

    public void createNewPlayer(PlayerData playerData) {
        String query = "INSERT INTO users(address, ens_domain, is_logged_in, last_location_x, last_location_y, last_location_z, last_location_yaw, last_location_pitch, last_location_world) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT DO NOTHING";

        try {
            PreparedStatement preparedStatement = Postgres.getConnection().prepareStatement(query);

            // set address
            preparedStatement.setString(1, playerData.getAddress());
            // set ens_domain
            preparedStatement.setString(2, playerData.getEnsDomain());
            // set is_logged_in
            preparedStatement.setBoolean(3, true);
            // set last_location_x
            preparedStatement.setDouble(4, playerData.getPlayerLocation().getX());
            // set last_location_y
            preparedStatement.setDouble(5, playerData.getPlayerLocation().getY());
            // set last_location_z
            preparedStatement.setDouble(6, playerData.getPlayerLocation().getZ());
            // set last_location_yaw
            preparedStatement.setFloat(7, playerData.getPlayerLocation().getYaw());
            // set last_location_pitch
            preparedStatement.setFloat(8, playerData.getPlayerLocation().getPitch());
            // set last_location_world
            preparedStatement.setString(9, playerData.getPlayerLocation().getWorld().getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.warning(e.getMessage(), "failed to create new player");
        }

    }

    public void updateUserInventory(PlayerData playerData) {
        String query = "UPDATE users SET ens_domain = ?, is_logged_in = ?, xp_level = ?, xp_exp = ?, health = ?, food_level = ?, game_mode = ?, fly_speed = ?, walk_speed = ?, allow_flight = ?, is_flying = ?, is_op = ?, is_banned = ?, potion_effects = ?, inventory = ?, ender_chest = ?, last_location_x = ?, last_location_y = ?, last_location_z = ?, last_location_yaw = ?, last_location_pitch = ?, last_location_world = ? WHERE address = ?";

        try {
            PreparedStatement preparedStatement = Postgres.getConnection().prepareStatement(query);

            // set ens_domain
            preparedStatement.setString(1, playerData.getEnsDomain());
            // set is_logged_in
            preparedStatement.setBoolean(2, false);
            // set xp level
            preparedStatement.setInt(3, playerData.getXpLevel());
            // set xp exp
            preparedStatement.setFloat(4, playerData.getXpExp());
            // set health
            preparedStatement.setDouble(5, playerData.getHealth());
            // set food level
            preparedStatement.setInt(6, playerData.getFoodLevel());
            // set game mode
            preparedStatement.setString(7, playerData.getGameMode().toString());
            // set fly speed
            preparedStatement.setFloat(8, playerData.getFlySpeed());
            // set walk speed
            preparedStatement.setFloat(9, playerData.getWalkSpeed());
            // set allow flight
            preparedStatement.setBoolean(10, playerData.getAllowFlight());
            // set is flying
            preparedStatement.setBoolean(11, playerData.isFlying());
            // set is op
            preparedStatement.setBoolean(12, playerData.isOp());
            // set is banned
            preparedStatement.setBoolean(13, playerData.isBanned());
            // set potion effects
            PGobject potionEffectsObject = potionEffectAdapter
                    .ParsePGObjectFromPotionEffects(playerData.getPotionEffects());
            preparedStatement.setObject(14, potionEffectsObject);
            // set inventory
            PGobject inventoryObject = itemStackAdapter.ParsePGObjectFromItemStackList(playerData.getInventory());
            preparedStatement.setObject(15, inventoryObject);
            // set ender chest
            PGobject enderchestObject = itemStackAdapter.ParsePGObjectFromItemStackList(playerData.getEnderchest());
            preparedStatement.setObject(16, enderchestObject);
            // set last_location_x
            preparedStatement.setDouble(17, playerData.getPlayerLocation().getX());
            // set last_location_y
            preparedStatement.setDouble(18, playerData.getPlayerLocation().getY());
            // set last_location_z
            preparedStatement.setDouble(19, playerData.getPlayerLocation().getZ());
            // set last_location_yaw
            preparedStatement.setFloat(20, playerData.getPlayerLocation().getYaw());
            // set last_location_pitch
            preparedStatement.setFloat(21, playerData.getPlayerLocation().getPitch());
            // set last_location_world
            preparedStatement.setString(22, playerData.getPlayerLocation().getWorld().getName());
            // set address
            String parsedAddress = Keys.toChecksumAddress(playerData.getAddress());
            preparedStatement.setString(23, parsedAddress);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.warning(e.getMessage(), "failed to update user inventory");
        }
    }

    public void setUserLoggedIn(String address) {
        String query = "UPDATE users SET is_logged_in = true, last_login = CURRENT_TIMESTAMP WHERE address = ?";

        try {
            PreparedStatement preparedStatement = Postgres.getConnection().prepareStatement(query);

            // set address
            String parsedAddress = Keys.toChecksumAddress(address);
            preparedStatement.setString(1, parsedAddress);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.warning(e.getMessage(), "failed to update user login");
        }
    }

    public ArrayList<String> getBannedPlayers() {
        String query = "SELECT address FROM users WHERE is_banned = true";
        ArrayList<String> bannedPlayers = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = Postgres.getConnection().prepareStatement(query);
            ResultSet res = preparedStatement.executeQuery();

            while (res.next()) {
                String address = res.getString("address");
                bannedPlayers.add(address);
            }
        } catch (SQLException e) {
            Logger.warning(e.getMessage(), "failed to get banned players");
        }

        return bannedPlayers;
    }

    public void setPlayerBanned(String address, boolean isBanned) {
        String query = "UPDATE users SET is_banned = ? WHERE address = ?";

        try {
            PreparedStatement preparedStatement = Postgres.getConnection().prepareStatement(query);

            // set is_banned
            preparedStatement.setBoolean(1, isBanned);
            // set address
            String parsedAddress = Keys.toChecksumAddress(address);
            preparedStatement.setString(2, parsedAddress);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.warning(e.getMessage(), "failed to update user ban status");
        }
    }

}

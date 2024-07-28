package com.boomchanotai.mine3.Config;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.boomchanotai.mine3.Mine3;
import com.boomchanotai.mine3.Logger.Logger;

public class SpawnConfig {
    private static File spawnConfigFile;
    private static FileConfiguration spawnConfig;

    public static FileConfiguration getSpawnConfig() {
        return spawnConfig;
    }

    public static void saveDefaultSpawnConfig() {
        spawnConfigFile = new File(Mine3.getInstance().getDataFolder(), "spawn.yml");

        if (!spawnConfigFile.exists()) {
            Mine3.getInstance().saveResource("spawn.yml", false);
        }

        spawnConfig = new YamlConfiguration();
        try {
            spawnConfig.load(spawnConfigFile);
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "Fail to load spawn.yml");
        }
    }

    public static void saveSpawnConfig() {
        try {
            spawnConfig.save(spawnConfigFile);
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "Fail to save spawn.yml");
        }
    }

    public static void loadConfig() {
        if (getSpawnConfig().getBoolean("spawn.init")) {
            return;
        }

        getSpawnConfig().set("spawn.init", true);
        Location defaultSpawnLocation = Mine3.getInstance().getServer().getWorlds().get(0).getSpawnLocation();
        setSpawnLocation(defaultSpawnLocation);
    }

    public static void setSpawnLocation(Location location) {
        getSpawnConfig().set("spawn.x", location.getX());
        getSpawnConfig().set("spawn.y", location.getY());
        getSpawnConfig().set("spawn.z", location.getZ());
        getSpawnConfig().set("spawn.yaw", location.getYaw());
        getSpawnConfig().set("spawn.pitch", location.getPitch());
        getSpawnConfig().set("spawn.world", location.getWorld().getName());
        saveSpawnConfig();
    }

    public static Location getSpawnLocation() {
        World world = Mine3.getInstance().getServer().getWorld(getSpawnConfig().getString("spawn.world"));
        Location spawnLocation = new Location(
                world,
                getSpawnConfig().getDouble("spawn.x"),
                getSpawnConfig().getDouble("spawn.y"),
                getSpawnConfig().getDouble("spawn.z"),
                (float) getSpawnConfig().getDouble("spawn.yaw"),
                (float) getSpawnConfig().getDouble("spawn.pitch"));
        return spawnLocation;
    }
}

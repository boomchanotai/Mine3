package com.boomchanotai.mine3Auth.service;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.boomchanotai.mine3Auth.Mine3Auth;
import com.boomchanotai.mine3Auth.config.SpawnConfig;
import com.boomchanotai.mine3Lib.logger.Logger;

public class SpawnService {
    public void setSpawnLocation(Location location) {
        Plugin mine3Standard = Mine3Auth.getPlugin().getServer().getPluginManager().getPlugin("Mine3Standard");
        if (mine3Standard != null) {
            Logger.info("Found Mine3Standard plugin, setting spawn location in Mine3Standard");
        }

        SpawnConfig.setSpawnLocation(location);
    }

    public Location getSpawnLocation() {
        Plugin mine3Standard = Mine3Auth.getPlugin().getServer().getPluginManager().getPlugin("Mine3Standard");
        if (mine3Standard == null) {
            return SpawnConfig.getSpawnLocation();
        }

        File spawnConfigFile = new File(mine3Standard.getDataFolder(), "spawn.yml");

        if (!spawnConfigFile.exists()) {
            return SpawnConfig.getSpawnLocation();
        }

        FileConfiguration spawnConfig = new YamlConfiguration();
        try {
            spawnConfig.load(spawnConfigFile);
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "Fail to load spawn.yml from Mine3Standard");
            return SpawnConfig.getSpawnLocation();
        }

        World world = Mine3Auth.getPlugin().getServer().getWorld(spawnConfig.getString("spawn.world"));
        Location spawnLocation = new Location(
                world,
                spawnConfig.getDouble("spawn.x"),
                spawnConfig.getDouble("spawn.y"),
                spawnConfig.getDouble("spawn.z"),
                (float) spawnConfig.getDouble("spawn.yaw"),
                (float) spawnConfig.getDouble("spawn.pitch"));

        return spawnLocation;
    }
}

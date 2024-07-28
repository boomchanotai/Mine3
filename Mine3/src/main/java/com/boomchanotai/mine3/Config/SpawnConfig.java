package com.boomchanotai.mine3.Config;

import java.io.File;

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
}

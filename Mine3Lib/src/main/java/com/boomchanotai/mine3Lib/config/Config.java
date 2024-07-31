package com.boomchanotai.mine3Lib.config;

import org.bukkit.configuration.file.FileConfiguration;

import com.boomchanotai.mine3Lib.Mine3Lib;

public class Config {
    // Redis Settings
    public static String REDIS_HOST;
    public static int REDIS_PORT;

    // Player Settings
    public static String PLAYER_ADDRESS_KEY;
    public static String PLAYER_PLAYER_KEY;

    public static void saveDefaultConfig() {
        Mine3Lib.getPlugin().saveDefaultConfig();
    }

    public static void loadConfig() {
        FileConfiguration config = Mine3Lib.getPlugin().getConfig();

        REDIS_HOST = config.getString("redis.host");
        REDIS_PORT = config.getInt("redis.port");

        PLAYER_ADDRESS_KEY = config.getString("player.address_key");
        PLAYER_PLAYER_KEY = config.getString("player.player_key");
    }
}

package com.boomchanotai.mine3Lib.config;

import org.bukkit.configuration.file.FileConfiguration;

import com.boomchanotai.mine3Lib.Mine3Lib;

public class Config {
    // Redis Settings
    public static String REDIS_HOST;
    public static int REDIS_PORT;

    // Postgres Settings
    public static boolean POSTGRES_ENABLE;
    public static String POSTGRES_HOST;
    public static String POSTGRES_USERNAME;
    public static String POSTGRES_PASSWORD;

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

        POSTGRES_ENABLE = config.getBoolean("postgres.enable");
        POSTGRES_HOST = config.getString("postgres.host");
        POSTGRES_USERNAME = config.getString("postgres.username");
        POSTGRES_PASSWORD = config.getString("postgres.password");

        PLAYER_ADDRESS_KEY = config.getString("player.address_key");
        PLAYER_PLAYER_KEY = config.getString("player.player_key");
    }
}

package com.boomchanotai.mine3Lib.config;

import java.util.Objects;

import org.bukkit.configuration.file.FileConfiguration;

import com.boomchanotai.mine3Lib.Mine3Lib;

public class Config {
    // Plugin Settings
    public static String TITLE;
    public static char COLOR_CODE_PREFIX;

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

        TITLE = config.getString("title");
        COLOR_CODE_PREFIX = Objects.requireNonNull(config.getString("color_code_prefix")).charAt(0);

        REDIS_HOST = config.getString("redis.host");
        REDIS_PORT = config.getInt("redis.port");

        PLAYER_ADDRESS_KEY = config.getString("player.address_key");
        PLAYER_PLAYER_KEY = config.getString("player.player_key");
    }

    public static void reloadConfig() {
        Mine3Lib.getPlugin().reloadConfig();
        saveDefaultConfig();
        Mine3Lib.getPlugin().getConfig().options().copyDefaults(true);
        loadConfig();
    }
}

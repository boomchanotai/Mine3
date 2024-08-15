package com.boomchanotai.mine3Standard.config;

import com.boomchanotai.mine3Standard.Mine3Standard;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    // Postgres Settings
    public static String POSTGRES_HOST;
    public static String POSTGRES_USERNAME;
    public static String POSTGRES_PASSWORD;
    public static String POSTGRES_USER_TABLE;

    // Chat Settings
    public static String CHAT_FORMAT;

    // Teleport Settings
    public static int TELEPORT_TIMEOUT;

    public static void saveDefaultConfig() {
        Mine3Standard.getPlugin().saveDefaultConfig();
    }

    public static void loadConfig() {
        FileConfiguration config = Mine3Standard.getPlugin().getConfig();

        POSTGRES_HOST = config.getString("postgres.host");
        POSTGRES_USERNAME = config.getString("postgres.username");
        POSTGRES_PASSWORD = config.getString("postgres.password");
        POSTGRES_USER_TABLE = config.getString("postgres.user_table");

        CHAT_FORMAT = config.getString("chat_format");

        TELEPORT_TIMEOUT = config.getInt("teleport_timeout");
    }

    public static void reloadConfig() {
        Mine3Standard.getPlugin().reloadConfig();
        saveDefaultConfig();
        Mine3Standard.getPlugin().getConfig().options().copyDefaults(true);
        loadConfig();
    }
}

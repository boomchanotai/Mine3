package com.boomchanotai.mine3Permission.config;

import org.bukkit.configuration.file.FileConfiguration;

import com.boomchanotai.mine3Permission.Mine3Permission;

public class Config {

    // Postgres Settings
    public static String POSTGRES_HOST;
    public static String POSTGRES_USERNAME;
    public static String POSTGRES_PASSWORD;
    public static String POSTGRES_GROUP_TABLE;

    public static void saveDefaultConfig() {
        Mine3Permission.getPlugin().saveDefaultConfig();
    }

    public static void loadConfig() {
        FileConfiguration config = Mine3Permission.getPlugin().getConfig();

        POSTGRES_HOST = config.getString("postgres.host");
        POSTGRES_USERNAME = config.getString("postgres.username");
        POSTGRES_PASSWORD = config.getString("postgres.password");
        POSTGRES_GROUP_TABLE = config.getString("postgres.group_table");
    }

    public static void reloadConfig() {
        Mine3Permission.getPlugin().reloadConfig();
        saveDefaultConfig();
        Mine3Permission.getPlugin().getConfig().options().copyDefaults(true);
        loadConfig();
    }
}

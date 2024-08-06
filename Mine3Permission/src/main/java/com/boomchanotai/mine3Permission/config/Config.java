package com.boomchanotai.mine3Permission.config;

import java.util.Objects;

import org.bukkit.configuration.file.FileConfiguration;

import com.boomchanotai.mine3Permission.Mine3Permission;

public class Config {
    // Plugin Settings
    public static char COLOR_CODE_PREFIX;

    // Postgres Settings
    public static String POSTGRES_HOST;
    public static String POSTGRES_USERNAME;
    public static String POSTGRES_PASSWORD;

    public static void saveDefaultConfig() {
        Mine3Permission.getPlugin().saveDefaultConfig();
    }

    public static void loadConfig() {
        FileConfiguration config = Mine3Permission.getPlugin().getConfig();
        COLOR_CODE_PREFIX = Objects.requireNonNull(config.getString("color_code_prefix")).charAt(0);

        POSTGRES_HOST = config.getString("postgres.host");
        POSTGRES_USERNAME = config.getString("postgres.username");
        POSTGRES_PASSWORD = config.getString("postgres.password");
    }

    public static void reloadConfig() {
        Mine3Permission.getPlugin().reloadConfig();
        saveDefaultConfig();
        Mine3Permission.getPlugin().getConfig().options().copyDefaults(true);
        loadConfig();
    }
}

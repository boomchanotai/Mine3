package com.boomchanotai.mine3Standard.config;

import com.boomchanotai.mine3Standard.Mine3Standard;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    // Chat Settings
    public static String CHAT_FORMAT;

    // Teleport Settings
    public static int TELEPORT_TIMEOUT;

    public static void saveDefaultConfig() {
        Mine3Standard.getPlugin().saveDefaultConfig();
    }

    public static void loadConfig() {
        FileConfiguration config = Mine3Standard.getPlugin().getConfig();
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

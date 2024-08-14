package com.boomchanotai.core.logger;

import org.bukkit.plugin.java.JavaPlugin;

public class Logger {
    private static JavaPlugin plugin;

    public static void init(JavaPlugin plugin) {
        Logger.plugin = plugin;
    }

    public static void info(String message, String... msg) {
        String allMessage = String.join(" ", msg);
        plugin.getLogger().info(message + " " + allMessage);
    }

    public static void warning(String message, String... msg) {
        String allMessage = String.join(" ", msg);
        plugin.getLogger().warning(message + " " + allMessage);
    }
}

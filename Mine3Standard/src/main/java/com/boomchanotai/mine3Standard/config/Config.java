package com.boomchanotai.mine3Standard.config;

import com.boomchanotai.mine3Standard.Mine3Standard;

import java.util.Objects;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    // Plugin Settings
    public static char COLOR_CODE_PREFIX;

    // Teleport Settings
    public static int TELEPORT_TIMEOUT;

    public static void saveDefaultConfig() {
        Mine3Standard.getPlugin().saveDefaultConfig();
    }

    public static void loadConfig() {
        FileConfiguration config = Mine3Standard.getPlugin().getConfig();
        COLOR_CODE_PREFIX = Objects.requireNonNull(config.getString("color_code_prefix")).charAt(0);

        TELEPORT_TIMEOUT = config.getInt("teleport_timeout");
    }
}

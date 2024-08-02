package com.boomchanotai.mine3Standard.config;

import com.boomchanotai.mine3Standard.Mine3Standard;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    public static String TELEPORT_TIMEOUT;

    public static void saveDefaultConfig() {
        Mine3Standard.getPlugin().saveDefaultConfig();
    }

    public static void loadConfig() {
        FileConfiguration config = Mine3Standard.getPlugin().getConfig();
        TELEPORT_TIMEOUT = config.getString("teleport_timeout");
    }
}

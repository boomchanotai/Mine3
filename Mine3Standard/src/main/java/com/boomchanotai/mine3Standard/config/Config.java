package com.boomchanotai.mine3Standard.config;

import com.boomchanotai.mine3Standard.Mine3Standard;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class Config {
    // Plugin Settings
    public static String TITLE;
    public static char COLOR_CODE_PREFIX;

    public static void saveDefaultConfig() {
        Mine3Standard.getPlugin().saveDefaultConfig();
    }

    public static void loadConfig() {
        FileConfiguration config = Mine3Standard.getPlugin().getConfig();
        TITLE = config.getString("title");
        COLOR_CODE_PREFIX = Objects.requireNonNull(config.getString("color_code_prefix")).charAt(0);
    }
}

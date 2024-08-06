package com.boomchanotai.mine3Permission.logger;

import com.boomchanotai.mine3Permission.Mine3Permission;

public class Logger {
    public static void info(String message, String... msg) {
        String allMessage = String.join("; ", msg);
        Mine3Permission.getPlugin().getLogger().info(message + " " + allMessage);
    }

    public static void warning(String message, String... msg) {
        String allMessage = String.join(" ", msg);
        Mine3Permission.getPlugin().getLogger().warning(message + " " + allMessage);
    }
}

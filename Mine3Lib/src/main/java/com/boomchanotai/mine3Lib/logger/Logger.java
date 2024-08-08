package com.boomchanotai.mine3Lib.logger;

import com.boomchanotai.mine3Lib.Mine3Lib;

public class Logger {
    public static void info(String message, String... msg) {
        String allMessage = String.join("; ", msg);
        Mine3Lib.getPlugin().getLogger().info(message + " " + allMessage);
    }

    public static void warning(String message, String... msg) {
        String allMessage = String.join(" ", msg);
        Mine3Lib.getPlugin().getLogger().warning(message + " " + allMessage);
    }
}

package com.boomchanotai.mine3Standard.logger;

import com.boomchanotai.mine3Standard.Mine3Standard;

public class Logger {
    public static void info(String message, String... msg) {
        String allMessage = String.join("; ", msg);
        Mine3Standard.getPlugin().getLogger().info(message + " " + allMessage);
    }

    public static void warning(String message, String... msg) {
        String allMessage = String.join(" ", msg);
        Mine3Standard.getPlugin().getLogger().warning(message + " " + allMessage);
    }
}

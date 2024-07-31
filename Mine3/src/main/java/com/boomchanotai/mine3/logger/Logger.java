package com.boomchanotai.mine3.logger;

import com.boomchanotai.mine3.Mine3;

public class Logger {
    public static void info(String message, String... msg) {
        String allMessage = String.join("; ", msg);
        Mine3.getInstance().getLogger().info(message + "; " + allMessage);
    }

    public static void warning(String message, String... msg) {
        String allMessage = String.join(" ", msg);
        Mine3.getInstance().getLogger().warning(message + "; " + allMessage);
    }
}

package com.boomchanotai.mine3Auth.logger;

import com.boomchanotai.mine3Auth.Mine3Auth;

public class Logger {
    public static void info(String message, String... msg) {
        String allMessage = String.join("; ", msg);
        Mine3Auth.getPlugin().getLogger().info(message + " " + allMessage);
    }

    public static void warning(String message, String... msg) {
        String allMessage = String.join(" ", msg);
        Mine3Auth.getPlugin().getLogger().warning(message + " " + allMessage);
    }
}

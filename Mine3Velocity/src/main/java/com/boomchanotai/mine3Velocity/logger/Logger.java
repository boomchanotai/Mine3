package com.boomchanotai.mine3Velocity.logger;

import com.boomchanotai.mine3Velocity.Mine3Velocity;

public class Logger {
    public static void info(String message, String... msg) {
        String allMessage = String.join("; ", msg);
        Mine3Velocity.getLogger().info(message + " " + allMessage);
    }

    public static void warning(String message, String... msg) {
        String allMessage = String.join(" ", msg);
        Mine3Velocity.getLogger().error(message + " " + allMessage);
    }
}

package com.boomchanotai.mine3.Logger;

import com.boomchanotai.mine3.Mine3;

public class Logger {
    public static void info(String message) {
        Mine3.getInstance().getLogger().info(message);
    }

    public static void warning(String message) {
        Mine3.getInstance().getLogger().warning(message);
    }
}

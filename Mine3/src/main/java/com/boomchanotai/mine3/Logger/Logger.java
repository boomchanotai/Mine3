package com.boomchanotai.mine3.Logger;

import com.boomchanotai.mine3.Mine3;

public class Logger {
    public static void info(String mainMsg, String... message) {
        String msg = String.join(" ", message);
        Mine3.getInstance().getLogger().info(mainMsg + " " + msg);
    }

    public static void warning(String mainMsg, String... message) {
        String msg = String.join(" ", message);
        Mine3.getInstance().getLogger().warning(mainMsg + " " + msg);
    }
}

package com.boomchanotai.core.logger;

public class Logger {
    private static LoggerAdapter logger;

    public static <T> void init(T logger) {
        if (logger instanceof java.util.logging.Logger) {
            Logger.logger = new JavaLogger((java.util.logging.Logger) logger);
            return;
        }

        if (logger instanceof org.slf4j.Logger) {
            Logger.logger = new VelocityLogger((org.slf4j.Logger) logger);
            return;
        }

        return;
    }

    public static LoggerAdapter getLogger() {
        return logger;
    }
}

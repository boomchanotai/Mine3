package com.boomchanotai.core.logger;

import java.util.logging.Logger;

public class JavaLogger implements LoggerAdapter {
    private static Logger logger;

    public JavaLogger(Logger logger) {
        JavaLogger.logger = logger;
    }

    @Override
    public void info(String message, String... msg) {
        String allMessage = String.join(" ", msg);
        logger.info(message + " " + allMessage);
    }

    @Override
    public void warning(String message, String... msg) {
        String allMessage = String.join(" ", msg);
        logger.warning(message + " " + allMessage);
    }
}

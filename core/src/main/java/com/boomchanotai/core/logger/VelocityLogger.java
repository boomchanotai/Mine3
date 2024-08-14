package com.boomchanotai.core.logger;

import org.slf4j.Logger;

public class VelocityLogger implements LoggerAdapter {
    private static Logger logger;

    public VelocityLogger(Logger logger) {
        VelocityLogger.logger = logger;
    }

    @Override
    public void info(String message, String... msg) {
        String allMessage = String.join(" ", msg);
        logger.info(message + " " + allMessage);
    }

    @Override
    public void warning(String message, String... msg) {
        String allMessage = String.join(" ", msg);
        logger.error(message + " " + allMessage);
    }

}

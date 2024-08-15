package com.boomchanotai.core.logger;

public interface LoggerAdapter {
    void info(String message, String... msg);

    void warning(String message, String... msg);
}

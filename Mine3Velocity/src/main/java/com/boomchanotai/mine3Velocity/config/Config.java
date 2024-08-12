package com.boomchanotai.mine3Velocity.config;

import java.io.File;
import java.nio.file.Path;

import com.boomchanotai.mine3Velocity.Mine3Velocity;
import com.boomchanotai.mine3Velocity.logger.Logger;
import com.velocitypowered.api.plugin.annotation.DataDirectory;

import dev.dejvokep.boostedyaml.YamlDocument;

public class Config {
    private static YamlDocument config;

    // Redis Settings
    public static String REDIS_HOST;
    public static int REDIS_PORT;

    // Httpserver Settings
    public static int HTTPSERVER_PORT;

    // Authentication Settings
    public static String AUTH_WEBSITE_TOKEN_BASE_URL;
    public static int AUTH_LOGIN_SESSION_TIMEOUT;
    public static String AUTH_TOKEN_PREFIX_KEY;
    public static String AUTH_CLICK_TO_LOGIN_MESSAGE;
    public static String AUTH_PREVENT_ACTION_MESSAGE;

    // Join server title
    public static String AUTH_JOIN_SERVER_TITLE_TITLE;
    public static String AUTH_JOIN_SERVER_TITLE_SUBTITLE;
    public static int AUTH_JOIN_SERVER_TITLE_FADE_IN;
    public static int AUTH_JOIN_SERVER_TITLE_STAY;
    public static int AUTH_JOIN_SERVER_TITLE_FADE_OUT;

    // Logged in title
    public static String AUTH_LOGGED_IN_TITLE_TITLE;
    public static String AUTH_LOGGED_IN_TITLE_SUBTITLE;
    public static int AUTH_LOGGED_IN_TITLE_FADE_IN;
    public static int AUTH_LOGGED_IN_TITLE_STAY;
    public static int AUTH_LOGGED_IN_TITLE_FADE_OUT;

    public static void loadConfig(@DataDirectory Path dataDirectory) {
        try {
            config = YamlDocument.create(new File(dataDirectory.toFile(),
                    "config.yml"),
                    Mine3Velocity.getPlugin().getClass().getResourceAsStream("/config.yml"));

            config.update();
            config.save();
        } catch (Exception e) {
            Logger.warning("Can't load config.yml", e.getMessage());
        }

        REDIS_HOST = config.getString("redis.host");
        REDIS_PORT = config.getInt("redis.port");

        HTTPSERVER_PORT = config.getInt("httpserver.port");

        AUTH_WEBSITE_TOKEN_BASE_URL = config.getString("auth.website_token_base_url");
        AUTH_LOGIN_SESSION_TIMEOUT = config.getInt("auth.login_session_timeout");
        AUTH_TOKEN_PREFIX_KEY = config.getString("auth.token_prefix_key");
    }
}

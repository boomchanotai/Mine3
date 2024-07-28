package com.boomchanotai.mine3.Config;

import com.boomchanotai.mine3.Mine3;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class Config {
    // Plugin Settings
    public static String TITLE;
    public static char COLOR_CODE_PREFIX;

    // Redis Settings
    public static String REDIS_HOST;
    public static int REDIS_PORT;

    // Postgres Settings
    public static String POSTGRES_HOST;
    public static String POSTGRES_USERNAME;
    public static String POSTGRES_PASSWORD;

    // Httpserver Settings
    public static int HTTPSERVER_PORT;

    // Authentication Settings
    public static String AUTH_WEBSITE_TOKEN_BASE_URL;
    public static int AUTH_LOGIN_SESSION_TIMEOUT;
    public static String AUTH_TOKEN_PREFIX_KEY;
    public static String AUTH_ADDRESS_KEY;
    public static String AUTH_PLAYER_KEY;
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

    public static void saveDefaultConfig() {
        Mine3.getInstance().saveDefaultConfig();
    }

    public static void loadConfig() {
        FileConfiguration config = Mine3.getInstance().getConfig();
        TITLE = config.getString("title");
        COLOR_CODE_PREFIX = Objects.requireNonNull(config.getString("color_code_prefix")).charAt(0);

        REDIS_HOST = config.getString("redis.host");
        REDIS_PORT = config.getInt("redis.port");

        POSTGRES_HOST = config.getString("postgres.host");
        POSTGRES_USERNAME = config.getString("postgres.username");
        POSTGRES_PASSWORD = config.getString("postgres.password");

        HTTPSERVER_PORT = config.getInt("httpserver.port");

        AUTH_WEBSITE_TOKEN_BASE_URL = config.getString("auth.website_token_base_url");
        AUTH_LOGIN_SESSION_TIMEOUT = config.getInt("auth.login_session_timeout");
        AUTH_TOKEN_PREFIX_KEY = config.getString("auth.token_prefix_key");
        AUTH_ADDRESS_KEY = config.getString("auth.address_key");
        AUTH_PLAYER_KEY = config.getString("auth.player_key");
        AUTH_CLICK_TO_LOGIN_MESSAGE = config.getString("auth.click_to_login_message");
        AUTH_PREVENT_ACTION_MESSAGE = config.getString("auth.prevent_action_message");

        AUTH_JOIN_SERVER_TITLE_TITLE = config.getString("auth.join_server_title.title");
        AUTH_JOIN_SERVER_TITLE_SUBTITLE = config.getString("auth.join_server_title.subtitle");
        AUTH_JOIN_SERVER_TITLE_FADE_IN = config.getInt("auth.join_server_title.fade_in");
        AUTH_JOIN_SERVER_TITLE_STAY = config.getInt("auth.join_server_title.stay");
        AUTH_JOIN_SERVER_TITLE_FADE_OUT = config.getInt("auth.join_server_title.fade_out");

        AUTH_LOGGED_IN_TITLE_TITLE = config.getString("auth.logged_in_title.title");
        AUTH_LOGGED_IN_TITLE_SUBTITLE = config.getString("auth.logged_in_title.subtitle");
        AUTH_LOGGED_IN_TITLE_FADE_IN = config.getInt("auth.logged_in_title.fade_in");
        AUTH_LOGGED_IN_TITLE_STAY = config.getInt("auth.logged_in_title.stay");
        AUTH_LOGGED_IN_TITLE_FADE_OUT = config.getInt("auth.logged_in_title.fade_out");
    }
}

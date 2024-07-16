package com.boomchanotai.mine3;

import com.boomchanotai.mine3.Javalin.Javalin;
import com.boomchanotai.mine3.Listeners.PlayerJoinQuitEvent;
import com.boomchanotai.mine3.Redis.Redis;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Mine3 extends JavaPlugin {
    public static final String LOG_TITLE = "[Mine3] ";
    private static Mine3 instance;
    public static String TITLE;
    public static char COLOR_CODE_PREFIX;
    public static Mine3 getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        FileConfiguration config = this.getConfig();
        TITLE = config.getString("title");
        COLOR_CODE_PREFIX = Objects.requireNonNull(config.getString("color_code_prefix")).charAt(0);

        saveDefaultConfig();
        Redis.connect();
        Javalin.startServer();

        getServer().getPluginManager().registerEvents(new PlayerJoinQuitEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Javalin.app.stop();
        Redis.getPool().close();
    }
}

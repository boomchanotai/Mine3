package com.boomchanotai.mine3Lib;

import org.bukkit.plugin.java.JavaPlugin;

import com.boomchanotai.mine3Lib.config.Config;
import com.boomchanotai.mine3Lib.postgres.Postgres;
import com.boomchanotai.mine3Lib.redis.Redis;

public final class Mine3Lib extends JavaPlugin {
    private static Mine3Lib plugin;

    public static Mine3Lib getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        // Config.yml
        Config.saveDefaultConfig();
        Config.loadConfig();

        Postgres.connect();
        Redis.connect();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Redis.close();
        Postgres.disconnect();
    }
}

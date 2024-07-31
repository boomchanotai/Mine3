package com.boomchanotai.mine3Lib;

import org.bukkit.plugin.java.JavaPlugin;

import com.boomchanotai.mine3Lib.config.Config;
import com.boomchanotai.mine3Lib.postgres.Postgres;
import com.boomchanotai.mine3Lib.redis.Redis;
import com.boomchanotai.mine3Lib.repository.Mine3Repository;

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
        Mine3Repository.clearPlayer();
        Redis.close();
        Postgres.disconnect();
    }
}

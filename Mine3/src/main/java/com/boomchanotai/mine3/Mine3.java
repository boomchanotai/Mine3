package com.boomchanotai.mine3;

import com.boomchanotai.mine3.Config.Config;
import com.boomchanotai.mine3.Server.Server;
import com.boomchanotai.mine3.Listeners.PlayerJoinQuitEvent;
import com.boomchanotai.mine3.Redis.Redis;
import org.bukkit.plugin.java.JavaPlugin;

public final class Mine3 extends JavaPlugin {
    private static Mine3 instance;
    public static Mine3 getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        saveDefaultConfig();
        Config.loadConfig();

        Redis.connect();
        Server.startServer();

        getServer().getPluginManager().registerEvents(new PlayerJoinQuitEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Server.getApp().stop();
        Redis.getPool().close();
    }
}

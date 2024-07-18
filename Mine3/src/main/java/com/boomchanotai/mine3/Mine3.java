package com.boomchanotai.mine3;

import com.boomchanotai.mine3.Commands.Address;
import com.boomchanotai.mine3.Config.Config;
import com.boomchanotai.mine3.Database.Database;
import com.boomchanotai.mine3.Listeners.PreventPlayerActionWhenNotLoggedIn;
import com.boomchanotai.mine3.Logger.Logger;
import com.boomchanotai.mine3.Server.Server;
import com.boomchanotai.mine3.Listeners.PlayerJoinQuitEvent;
import com.boomchanotai.mine3.Redis.Redis;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

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
        try {
            Database.connect();
        } catch (SQLException e) {
            Logger.warning("Fail to connect database.");
        }
        Server.startServer();

        getServer().getPluginManager().registerEvents(new PlayerJoinQuitEvent(), this);
        getServer().getPluginManager().registerEvents(new PreventPlayerActionWhenNotLoggedIn(), this);

        getCommand("address").setExecutor(new Address());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Server.stopServer();
        Redis.close();
        try {
            Database.disconnect();
        } catch (SQLException e) {
            Logger.warning("Fail to disconnect database.");
        }
    }
}

package com.boomchanotai.mine3;

import com.boomchanotai.mine3.Commands.Address;
import com.boomchanotai.mine3.Commands.Logout;
import com.boomchanotai.mine3.Config.Config;
import com.boomchanotai.mine3.Database.Database;
import com.boomchanotai.mine3.Listeners.PreventPlayerActionWhenNotLoggedIn;
import com.boomchanotai.mine3.Logger.Logger;
import com.boomchanotai.mine3.Server.Server;
import com.boomchanotai.mine3.Listeners.PlayerJoinQuitEvent;
import com.boomchanotai.mine3.Redis.Redis;
import com.boomchanotai.mine3.Service.PlayerService;
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


        try {
            Database.connect();
        } catch (SQLException e) {
            Logger.warning("Fail to connect database.");
        }
        Redis.connect();
        Server.startServer();

        getServer().getPluginManager().registerEvents(new PlayerJoinQuitEvent(), this);
        getServer().getPluginManager().registerEvents(new PreventPlayerActionWhenNotLoggedIn(), this);

        getCommand("address").setExecutor(new Address());
        getCommand("logout").setExecutor(new Logout());

        // Connect when reloaded
        getServer().getOnlinePlayers().forEach(PlayerService::connectPlayer);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        PreventPlayerActionWhenNotLoggedIn.disconnectAll();
        Server.stopServer();
        Redis.close();
        try {
            Database.disconnect();
        } catch (SQLException e) {
            Logger.warning("Fail to disconnect database.");
        }
    }
}

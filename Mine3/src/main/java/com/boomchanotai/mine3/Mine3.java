package com.boomchanotai.mine3;

import com.boomchanotai.mine3.Commands.Address;
import com.boomchanotai.mine3.Commands.Admin;
import com.boomchanotai.mine3.Commands.AdminTabCompletion;
import com.boomchanotai.mine3.Commands.Logout;
import com.boomchanotai.mine3.Config.Config;
import com.boomchanotai.mine3.Database.Database;
import com.boomchanotai.mine3.Listeners.PreventPlayerActionWhenNotLoggedIn;
import com.boomchanotai.mine3.Logger.Logger;
import com.boomchanotai.mine3.Repository.ItemStackAdapter;
import com.boomchanotai.mine3.Repository.PostgresRepository;
import com.boomchanotai.mine3.Repository.RedisRepository;
import com.boomchanotai.mine3.Repository.SpigotRepository;
import com.boomchanotai.mine3.Server.Server;
import com.boomchanotai.mine3.Listeners.PlayerJoinQuitEvent;
import com.boomchanotai.mine3.Redis.Redis;
import com.boomchanotai.mine3.Service.PlayerService;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class Mine3 extends JavaPlugin {
    private static Mine3 instance;
    PlayerService playerService;

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

        // Dependencies
        ItemStackAdapter itemStackAdapter = new ItemStackAdapter();

        PostgresRepository pgRepo = new PostgresRepository(itemStackAdapter);
        RedisRepository redisRepo = new RedisRepository();
        SpigotRepository spigotRepo = new SpigotRepository();

        playerService = new PlayerService(pgRepo, redisRepo, spigotRepo);

        Server server = new Server(playerService);
        server.startServer();

        getServer().getPluginManager().registerEvents(new PlayerJoinQuitEvent(playerService), this);
        getServer().getPluginManager().registerEvents(new PreventPlayerActionWhenNotLoggedIn(spigotRepo), this);

        getCommand("address").setExecutor(new Address(redisRepo, spigotRepo));
        getCommand("logout").setExecutor(new Logout(playerService));
        getCommand("mine3").setExecutor(new Admin(spigotRepo));
        getCommand("mine3").setTabCompleter(new AdminTabCompletion());

        // Connect all players if in game
        getServer().getOnlinePlayers().forEach(playerService::connectPlayer);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // Disconnect all players when server stop / reload
        getServer().getOnlinePlayers().forEach(playerService::disconnectPlayer);

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

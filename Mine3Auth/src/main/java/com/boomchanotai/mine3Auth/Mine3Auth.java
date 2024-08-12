package com.boomchanotai.mine3Auth;

import com.boomchanotai.mine3Auth.commands.AddressCommand;
import com.boomchanotai.mine3Auth.commands.LogoutCommand;
import com.boomchanotai.mine3Auth.commands.AuthCommand;
import com.boomchanotai.mine3Auth.commands.AuthTabCompletion;
import com.boomchanotai.mine3Auth.config.Config;
import com.boomchanotai.mine3Auth.config.SpawnConfig;
import com.boomchanotai.mine3Auth.listeners.AuthListener;
import com.boomchanotai.mine3Auth.listeners.PlayerJoinQuitEvent;
import com.boomchanotai.mine3Auth.listeners.PreventPlayerActionWhenNotLoggedIn;
import com.boomchanotai.mine3Auth.redis.Redis;
import com.boomchanotai.mine3Auth.repositories.RedisRepository;
import com.boomchanotai.mine3Auth.server.Server;
import com.boomchanotai.mine3Auth.services.AuthService;
import com.boomchanotai.mine3Auth.services.PlayerService;

import org.bukkit.plugin.java.JavaPlugin;

public final class Mine3Auth extends JavaPlugin {
    private static Mine3Auth plugin;

    PlayerService playerService;
    AuthService authService;

    public static Mine3Auth getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        // Configuration
        Config.saveDefaultConfig();
        Config.loadConfig();

        // Spawn Configuration
        SpawnConfig.saveDefaultSpawnConfig();
        SpawnConfig.loadConfig();

        // Redis
        Redis.connect();

        // Dependencies
        RedisRepository redisRepo = new RedisRepository();

        // Services
        playerService = new PlayerService();
        authService = new AuthService(playerService, redisRepo);

        // Start HTTP Server
        Server server = new Server(authService);
        server.startServer();

        // Register Events
        getServer().getPluginManager().registerEvents(new PlayerJoinQuitEvent(authService), this);
        getServer().getPluginManager().registerEvents(new PreventPlayerActionWhenNotLoggedIn(playerService), this);
        getServer().getPluginManager().registerEvents(new AuthListener(playerService), this);

        // Register Commands
        getCommand("address").setExecutor(new AddressCommand());

        getCommand("logout").setExecutor(new LogoutCommand(authService));

        getCommand("auth").setExecutor(new AuthCommand());
        getCommand("auth").setTabCompleter(new AuthTabCompletion());

        // Connect all players if in game
        getServer().getOnlinePlayers().forEach(authService::connect);
    }

    @Override
    public void onDisable() {
        // Disconnect all players when server stop / reload
        getServer().getOnlinePlayers().forEach(authService::disconnect);
        playerService.removeAll();

        // Stop HTTP Server
        Server.stopServer();
    }
}

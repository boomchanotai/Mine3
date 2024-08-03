package com.boomchanotai.mine3Auth;

import com.boomchanotai.mine3Auth.commands.AddressCommand;
import com.boomchanotai.mine3Auth.commands.LogoutCommand;
import com.boomchanotai.mine3Auth.commands.Mine3Command;
import com.boomchanotai.mine3Auth.commands.Mine3TabCompletion;
import com.boomchanotai.mine3Auth.config.Config;
import com.boomchanotai.mine3Auth.config.SpawnConfig;
import com.boomchanotai.mine3Auth.listeners.PlayerDeath;
import com.boomchanotai.mine3Auth.listeners.PlayerJoinQuitEvent;
import com.boomchanotai.mine3Auth.listeners.PreventPlayerActionWhenNotLoggedIn;
import com.boomchanotai.mine3Auth.repository.ItemStackAdapter;
import com.boomchanotai.mine3Auth.repository.PostgresRepository;
import com.boomchanotai.mine3Auth.repository.PotionEffectAdapter;
import com.boomchanotai.mine3Auth.repository.RedisRepository;
import com.boomchanotai.mine3Auth.server.Server;
import com.boomchanotai.mine3Auth.service.AuthService;
import com.boomchanotai.mine3Auth.service.PlayerService;

import org.bukkit.plugin.java.JavaPlugin;

public final class Mine3Auth extends JavaPlugin {
    private static Mine3Auth instance;

    PlayerService playerService;
    AuthService authService;

    public static Mine3Auth getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        // Configuration
        Config.saveDefaultConfig();
        Config.loadConfig();

        // Spawn Configuration
        SpawnConfig.saveDefaultSpawnConfig();
        SpawnConfig.loadConfig();

        // Dependencies
        ItemStackAdapter itemStackAdapter = new ItemStackAdapter();
        PotionEffectAdapter potionEffectAdapter = new PotionEffectAdapter();

        PostgresRepository pgRepo = new PostgresRepository(itemStackAdapter, potionEffectAdapter);
        RedisRepository redisRepo = new RedisRepository();

        playerService = new PlayerService();
        authService = new AuthService(playerService, pgRepo, redisRepo);

        Server server = new Server(authService);
        server.startServer();

        // Register Events
        getServer().getPluginManager().registerEvents(new PlayerJoinQuitEvent(authService), this);
        getServer().getPluginManager().registerEvents(new PreventPlayerActionWhenNotLoggedIn(playerService), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);

        // Register Commands
        getCommand("address").setExecutor(new AddressCommand(redisRepo));
        getCommand("logout").setExecutor(new LogoutCommand(authService));
        getCommand("mine3").setExecutor(new Mine3Command());
        getCommand("mine3").setTabCompleter(new Mine3TabCompletion());

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

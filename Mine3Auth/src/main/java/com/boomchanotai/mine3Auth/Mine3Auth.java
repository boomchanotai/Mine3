package com.boomchanotai.mine3Auth;

import com.boomchanotai.mine3Auth.commands.AddressCommand;
import com.boomchanotai.mine3Auth.commands.LogoutCommand;
import com.boomchanotai.mine3Auth.commands.PardonCommand;
import com.boomchanotai.mine3Auth.commands.PardonTabCompletion;
import com.boomchanotai.mine3Auth.commands.AuthCommand;
import com.boomchanotai.mine3Auth.commands.AuthTabCompletion;
import com.boomchanotai.mine3Auth.commands.BanCommand;
import com.boomchanotai.mine3Auth.commands.BanTabCompletion;
import com.boomchanotai.mine3Auth.config.Config;
import com.boomchanotai.mine3Auth.config.SpawnConfig;
import com.boomchanotai.mine3Auth.listeners.PlayerAuth;
import com.boomchanotai.mine3Auth.listeners.PlayerJoinQuitEvent;
import com.boomchanotai.mine3Auth.listeners.PreventPlayerActionWhenNotLoggedIn;
import com.boomchanotai.mine3Auth.postgres.Postgres;
import com.boomchanotai.mine3Auth.redis.Redis;
import com.boomchanotai.mine3Auth.repositories.ItemStackAdapter;
import com.boomchanotai.mine3Auth.repositories.PostgresRepository;
import com.boomchanotai.mine3Auth.repositories.PotionEffectAdapter;
import com.boomchanotai.mine3Auth.repositories.RedisRepository;
import com.boomchanotai.mine3Auth.server.Server;
import com.boomchanotai.mine3Auth.services.AuthService;
import com.boomchanotai.mine3Auth.services.PlayerService;
import com.boomchanotai.mine3Auth.services.SpawnService;

import dev.iiahmed.disguise.DisguiseManager;
import dev.iiahmed.disguise.DisguiseProvider;

import java.util.regex.Pattern;

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

        // Postgres
        Postgres.connect();

        // Dependencies
        DisguiseProvider disguiseProvider = DisguiseManager.getProvider();
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_.]{1,16}$");
        disguiseProvider.setNamePattern(pattern);

        DisguiseManager.initialize(this, true);

        ItemStackAdapter itemStackAdapter = new ItemStackAdapter();
        PotionEffectAdapter potionEffectAdapter = new PotionEffectAdapter();

        PostgresRepository pgRepo = new PostgresRepository(itemStackAdapter, potionEffectAdapter);
        RedisRepository redisRepo = new RedisRepository();

        SpawnService spawnService = new SpawnService();
        playerService = new PlayerService(spawnService, disguiseProvider);
        authService = new AuthService(playerService, pgRepo, redisRepo);

        // Start HTTP Server
        Server server = new Server(authService);
        server.startServer();

        // Register Events
        getServer().getPluginManager().registerEvents(new PlayerJoinQuitEvent(authService), this);
        getServer().getPluginManager().registerEvents(new PreventPlayerActionWhenNotLoggedIn(playerService), this);
        getServer().getPluginManager().registerEvents(new PlayerAuth(playerService, pgRepo), this);

        // Register Commands
        getCommand("address").setExecutor(new AddressCommand());

        getCommand("logout").setExecutor(new LogoutCommand(authService));

        getCommand("auth").setExecutor(new AuthCommand(spawnService));
        getCommand("auth").setTabCompleter(new AuthTabCompletion());

        getCommand("ban").setExecutor(new BanCommand());
        getCommand("ban").setTabCompleter(new BanTabCompletion());

        getCommand("pardon").setExecutor(new PardonCommand(pgRepo));
        getCommand("pardon").setTabCompleter(new PardonTabCompletion(pgRepo));

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

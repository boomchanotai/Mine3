package com.boomchanotai.mine3Auth;

import com.boomchanotai.mine3Auth.commands.AddressCommand;
import com.boomchanotai.mine3Auth.commands.LogoutCommand;
import com.boomchanotai.mine3Auth.commands.Mine3Command;
import com.boomchanotai.mine3Auth.commands.Mine3CommandTabCompletion;
import com.boomchanotai.mine3Auth.config.Config;
import com.boomchanotai.mine3Auth.config.SpawnConfig;
import com.boomchanotai.mine3Auth.listeners.PlayerJoinQuitEvent;
import com.boomchanotai.mine3Auth.listeners.PreventPlayerActionWhenNotLoggedIn;
import com.boomchanotai.mine3Auth.repository.ItemStackAdapter;
import com.boomchanotai.mine3Auth.repository.PostgresRepository;
import com.boomchanotai.mine3Auth.repository.PotionEffectAdapter;
import com.boomchanotai.mine3Auth.repository.RedisRepository;
import com.boomchanotai.mine3Auth.repository.SpigotRepository;
import com.boomchanotai.mine3Auth.server.Server;
import com.boomchanotai.mine3Auth.service.PlayerService;

import org.bukkit.plugin.java.JavaPlugin;

public final class Mine3Auth extends JavaPlugin {
    private static Mine3Auth instance;
    PlayerService playerService;

    public static Mine3Auth getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        // Config.yml
        Config.saveDefaultConfig();
        Config.loadConfig();

        // Spawn.yml
        SpawnConfig.saveDefaultSpawnConfig();
        SpawnConfig.loadConfig();

        // Dependencies
        ItemStackAdapter itemStackAdapter = new ItemStackAdapter();
        PotionEffectAdapter potionEffectAdapter = new PotionEffectAdapter();

        PostgresRepository pgRepo = new PostgresRepository(itemStackAdapter, potionEffectAdapter);
        RedisRepository redisRepo = new RedisRepository();
        SpigotRepository spigotRepo = new SpigotRepository();

        playerService = new PlayerService(pgRepo, redisRepo, spigotRepo);

        Server server = new Server(playerService);
        server.startServer();

        // Register Events
        getServer().getPluginManager().registerEvents(new PlayerJoinQuitEvent(playerService), this);
        getServer().getPluginManager().registerEvents(new PreventPlayerActionWhenNotLoggedIn(spigotRepo), this);

        // Register Commands
        getCommand("address").setExecutor(new AddressCommand(redisRepo, spigotRepo));
        getCommand("logout").setExecutor(new LogoutCommand(playerService));
        getCommand("mine3").setExecutor(new Mine3Command(spigotRepo));
        getCommand("mine3").setTabCompleter(new Mine3CommandTabCompletion());

        // Connect all players if in game
        getServer().getOnlinePlayers().forEach(playerService::connectPlayer);
    }

    @Override
    public void onDisable() {
        // Disconnect all players when server stop / reload
        getServer().getOnlinePlayers().forEach(playerService::disconnectPlayer);

        PreventPlayerActionWhenNotLoggedIn.disconnectAll();
        Server.stopServer();
    }
}

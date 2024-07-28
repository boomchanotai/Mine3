package com.boomchanotai.mine3;

import com.boomchanotai.mine3.Commands.Address;
import com.boomchanotai.mine3.Commands.Admin;
import com.boomchanotai.mine3.Commands.AdminTabCompletion;
import com.boomchanotai.mine3.Commands.Logout;
import com.boomchanotai.mine3.Config.Config;
import com.boomchanotai.mine3.Config.SpawnConfig;
import com.boomchanotai.mine3.Database.Database;
import com.boomchanotai.mine3.Listeners.PreventPlayerActionWhenNotLoggedIn;
import com.boomchanotai.mine3.Repository.ItemStackAdapter;
import com.boomchanotai.mine3.Repository.PostgresRepository;
import com.boomchanotai.mine3.Repository.RedisRepository;
import com.boomchanotai.mine3.Repository.SpigotRepository;
import com.boomchanotai.mine3.Server.Server;
import com.boomchanotai.mine3.Listeners.PlayerJoinQuitEvent;
import com.boomchanotai.mine3.Redis.Redis;
import com.boomchanotai.mine3.Service.PlayerService;
import org.bukkit.plugin.java.JavaPlugin;

public final class Mine3 extends JavaPlugin {
    private static Mine3 instance;
    PlayerService playerService;

    public static Mine3 getInstance() {
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

        Database.connect();
        Redis.connect();

        // Dependencies
        ItemStackAdapter itemStackAdapter = new ItemStackAdapter();

        PostgresRepository pgRepo = new PostgresRepository(itemStackAdapter);
        RedisRepository redisRepo = new RedisRepository();
        SpigotRepository spigotRepo = new SpigotRepository();

        playerService = new PlayerService(pgRepo, redisRepo, spigotRepo);

        Server server = new Server(playerService);
        server.startServer();

        // Register Events
        getServer().getPluginManager().registerEvents(new PlayerJoinQuitEvent(playerService), this);
        getServer().getPluginManager().registerEvents(new PreventPlayerActionWhenNotLoggedIn(spigotRepo), this);

        // Register Commands
        getCommand("address").setExecutor(new Address(redisRepo, spigotRepo));
        getCommand("logout").setExecutor(new Logout(playerService));
        getCommand("mine3").setExecutor(new Admin(spigotRepo));
        getCommand("mine3").setTabCompleter(new AdminTabCompletion());

        // Connect all players if in game
        getServer().getOnlinePlayers().forEach(playerService::connectPlayer);
    }

    @Override
    public void onDisable() {
        // Disconnect all players when server stop / reload
        getServer().getOnlinePlayers().forEach(playerService::disconnectPlayer);

        PreventPlayerActionWhenNotLoggedIn.disconnectAll();
        Server.stopServer();
        Redis.close();
        Database.disconnect();
    }
}

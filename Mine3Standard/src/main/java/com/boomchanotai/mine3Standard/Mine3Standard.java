package com.boomchanotai.mine3Standard;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

import org.bukkit.plugin.java.JavaPlugin;

import com.boomchanotai.core.logger.Logger;
import com.boomchanotai.core.postgres.Postgres;
import com.boomchanotai.mine3Standard.commands.BanCommand;
import com.boomchanotai.mine3Standard.commands.BroadcastCommand;
import com.boomchanotai.mine3Standard.commands.BurnCommand;
import com.boomchanotai.mine3Standard.commands.ClearInventoryCommand;
import com.boomchanotai.mine3Standard.commands.DeOpCommand;
import com.boomchanotai.mine3Standard.commands.EnderChestCommand;
import com.boomchanotai.mine3Standard.commands.FeedCommand;
import com.boomchanotai.mine3Standard.commands.FlyCommand;
import com.boomchanotai.mine3Standard.commands.GameModeCommand;
import com.boomchanotai.mine3Standard.commands.GiveCommand;
import com.boomchanotai.mine3Standard.commands.GiveMeCommand;
import com.boomchanotai.mine3Standard.commands.GodCommand;
import com.boomchanotai.mine3Standard.commands.HealCommand;
import com.boomchanotai.mine3Standard.commands.InvseeCommand;
import com.boomchanotai.mine3Standard.commands.KickCommand;
import com.boomchanotai.mine3Standard.commands.KillCommand;
import com.boomchanotai.mine3Standard.commands.OpCommand;
import com.boomchanotai.mine3Standard.commands.PardonCommand;
import com.boomchanotai.mine3Standard.commands.SetSpawnCommand;
import com.boomchanotai.mine3Standard.commands.SpawnCommand;
import com.boomchanotai.mine3Standard.commands.SpeedCommand;
import com.boomchanotai.mine3Standard.commands.StandardCommand;
import com.boomchanotai.mine3Standard.commands.TpCommand;
import com.boomchanotai.mine3Standard.commands.TpHereCommand;
import com.boomchanotai.mine3Standard.commands.TpaCancelCommand;
import com.boomchanotai.mine3Standard.commands.TpaCommand;
import com.boomchanotai.mine3Standard.commands.TpaHereCommand;
import com.boomchanotai.mine3Standard.commands.TpacceptCommand;
import com.boomchanotai.mine3Standard.commands.VanishCommand;
import com.boomchanotai.mine3Standard.config.Config;
import com.boomchanotai.mine3Standard.config.SpawnConfig;
import com.boomchanotai.mine3Standard.listeners.AuthListener;
import com.boomchanotai.mine3Standard.listeners.PlayerListener;
import com.boomchanotai.mine3Standard.repositories.ItemStackAdapter;
import com.boomchanotai.mine3Standard.repositories.PostgresRepository;
import com.boomchanotai.mine3Standard.repositories.PotionEffectAdapter;
import com.boomchanotai.mine3Standard.services.PlayerService;
import com.boomchanotai.mine3Standard.services.TpaService;
import com.boomchanotai.mine3Standard.tabcompletion.BanTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.BurnTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.ClearInventoryTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.DeOpTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.EnderChestTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.FeedTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.FlyTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.GameModeTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.GiveMeTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.GiveTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.GodTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.HealTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.InvseeTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.KickTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.KillTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.OpTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.PardonTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.SpeedTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.StandardTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.TpHereTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.TpTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.TpaHereTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.TpaTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.VanishTabCompletion;

import dev.iiahmed.disguise.DisguiseManager;
import dev.iiahmed.disguise.DisguiseProvider;

public final class Mine3Standard extends JavaPlugin {
    private static Mine3Standard plugin;

    public static Mine3Standard getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        Logger.init(getLogger());

        // Configuration
        Config.saveDefaultConfig();
        Config.loadConfig();

        // Postgres
        Postgres.init(Config.POSTGRES_HOST, Config.POSTGRES_USERNAME, Config.POSTGRES_PASSWORD);
        initializeDatabase();

        // Spawn Configuration
        SpawnConfig.saveDefaultSpawnConfig();
        SpawnConfig.loadConfig();

        // Dependency
        DisguiseProvider disguiseProvider = DisguiseManager.getProvider();
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_.]{1,16}$");
        disguiseProvider.setNamePattern(pattern);
        DisguiseManager.initialize(this, true);

        TpaService tpaService = new TpaService(this);

        // Adapter
        ItemStackAdapter itemStackAdapter = new ItemStackAdapter();
        PotionEffectAdapter potionEffectAdapter = new PotionEffectAdapter();

        // Repository
        PostgresRepository pgRepo = new PostgresRepository(itemStackAdapter, potionEffectAdapter);

        // Service
        PlayerService playerService = new PlayerService(disguiseProvider);

        // Event Listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new AuthListener(playerService, pgRepo), this);

        // ban
        getCommand("ban").setExecutor(new BanCommand());
        getCommand("ban").setTabCompleter(new BanTabCompletion());

        // pardon
        getCommand("pardon").setExecutor(new PardonCommand(pgRepo));
        getCommand("pardon").setTabCompleter(new PardonTabCompletion(pgRepo));

        // tp
        getCommand("tp").setExecutor(new TpCommand());
        getCommand("tp").setTabCompleter(new TpTabCompletion());

        // tphere
        getCommand("tphere").setExecutor(new TpHereCommand());
        getCommand("tphere").setTabCompleter(new TpHereTabCompletion());

        // tpa
        getCommand("tpa").setExecutor(new TpaCommand(tpaService));
        getCommand("tpa").setTabCompleter(new TpaTabCompletion());

        // tpahere
        getCommand("tpahere").setExecutor(new TpaHereCommand(tpaService));
        getCommand("tpahere").setTabCompleter(new TpaHereTabCompletion());

        // tpaccept
        getCommand("tpaccept").setExecutor(new TpacceptCommand(tpaService));

        // tpacancel
        getCommand("tpacancel").setExecutor(new TpaCancelCommand(tpaService));

        // give
        getCommand("give").setExecutor(new GiveCommand());
        getCommand("give").setTabCompleter(new GiveTabCompletion());

        // i
        getCommand("i").setExecutor(new GiveMeCommand());
        getCommand("i").setTabCompleter(new GiveMeTabCompletion());

        // gamemode
        getCommand("gamemode").setExecutor(new GameModeCommand());
        getCommand("gamemode").setTabCompleter(new GameModeTabCompletion());

        // broadcast
        getCommand("broadcast").setExecutor(new BroadcastCommand());

        // burn
        getCommand("burn").setExecutor(new BurnCommand());
        getCommand("burn").setTabCompleter(new BurnTabCompletion());

        // clear
        getCommand("clear").setExecutor(new ClearInventoryCommand());
        getCommand("clear").setTabCompleter(new ClearInventoryTabCompletion());

        // feed
        getCommand("feed").setExecutor(new FeedCommand());
        getCommand("feed").setTabCompleter(new FeedTabCompletion());

        // heal
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("heal").setTabCompleter(new HealTabCompletion());

        // vanish
        getCommand("vanish").setExecutor(new VanishCommand());
        getCommand("vanish").setTabCompleter(new VanishTabCompletion());

        // fly
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("fly").setTabCompleter(new FlyTabCompletion());

        // god
        getCommand("god").setExecutor(new GodCommand());
        getCommand("god").setTabCompleter(new GodTabCompletion());

        // speed
        getCommand("speed").setExecutor(new SpeedCommand());
        getCommand("speed").setTabCompleter(new SpeedTabCompletion());

        // kill
        getCommand("kill").setExecutor(new KillCommand());
        getCommand("kill").setTabCompleter(new KillTabCompletion());

        // kick
        getCommand("kick").setExecutor(new KickCommand());
        getCommand("kick").setTabCompleter(new KickTabCompletion());

        // invsee
        getServer().getPluginManager().registerEvents(new InvseeCommand(), this);
        getCommand("invsee").setExecutor(new InvseeCommand());
        getCommand("invsee").setTabCompleter(new InvseeTabCompletion());

        // enderchest
        getServer().getPluginManager().registerEvents(new EnderChestCommand(), this);
        getCommand("enderchest").setExecutor(new EnderChestCommand());
        getCommand("enderchest").setTabCompleter(new EnderChestTabCompletion());

        // setspawn
        getCommand("setspawn").setExecutor(new SetSpawnCommand());

        // spawn
        getCommand("spawn").setExecutor(new SpawnCommand());

        // op
        getCommand("op").setExecutor(new OpCommand());
        getCommand("op").setTabCompleter(new OpTabCompletion());

        // deop
        getCommand("deop").setExecutor(new DeOpCommand());
        getCommand("deop").setTabCompleter(new DeOpTabCompletion());

        // standard reload
        getCommand("standard").setExecutor(new StandardCommand());
        getCommand("standard").setTabCompleter(new StandardTabCompletion());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Postgres.disconnect();
    }

    public static void initializeDatabase() {
        Statement statement;
        try {
            statement = Postgres.getConnection().createStatement();
        } catch (SQLException e) {
            Logger.warning(e.getMessage(), "Fail to create statement on initialize database.");
            return;
        }

        try {
            statement.execute("CREATE TABLE IF NOT EXISTS \"users\" (\n" +
                    "\t\"address\" TEXT PRIMARY KEY,\n" +
                    "\t\"ens_domain\" TEXT NOT NULL DEFAULT '',\n" +
                    "\t\"is_logged_in\" BOOLEAN NOT NULL DEFAULT FALSE,\n" +
                    "\t\"xp_level\" INTEGER NOT NULL DEFAULT 0,\n" +
                    "\t\"xp_exp\" FLOAT NOT NULL DEFAULT 0,\n" +
                    "\t\"health\" DECIMAL NOT NULL DEFAULT 20,\n" +
                    "\t\"food_level\" INTEGER NOT NULL DEFAULT 20,\n" +
                    "\t\"game_mode\" TEXT NOT NULL DEFAULT 'SURVIVAL',\n" +
                    "\t\"fly_speed\" FLOAT NOT NULL DEFAULT 0.1,\n" +
                    "\t\"walk_speed\" FLOAT NOT NULL DEFAULT 0.2,\n" +
                    "\t\"allow_flight\" BOOLEAN NOT NULL DEFAULT FALSE,\n" +
                    "\t\"is_flying\" BOOLEAN NOT NULL DEFAULT FALSE,\n" +
                    "\t\"is_op\" BOOLEAN NOT NULL DEFAULT FALSE,\n" +
                    "\t\"is_banned\" BOOLEAN NOT NULL DEFAULT FALSE,\n" +
                    "\t\"potion_effects\" JSONB NOT NULL DEFAULT '[]',\n" +
                    "\t\"inventory\" JSONB NOT NULL DEFAULT '[]',\n" +
                    "\t\"ender_chest\" JSONB NOT NULL DEFAULT '[]',\n" +
                    "\t\"last_location_x\" DECIMAL NOT NULL DEFAULT 0,\n" +
                    "\t\"last_location_y\" DECIMAL NOT NULL DEFAULT 0,\n" +
                    "\t\"last_location_z\" DECIMAL NOT NULL DEFAULT 0,\n" +
                    "\t\"last_location_yaw\" FLOAT NOT NULL DEFAULT 0,\n" +
                    "\t\"last_location_pitch\" FLOAT NOT NULL DEFAULT 0,\n" +
                    "\t\"last_location_world\" TEXT NOT NULL DEFAULT 'world',\n" +
                    "\t\"metadata\" JSONB NOT NULL DEFAULT '{}',\n" +
                    "\t\"created_at\" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                    "\t\"last_login\" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP\n" +
                    ");");
        } catch (SQLException e) {
            Logger.warning(e.getMessage(), "Fail to create table users.");
        }
    }
}

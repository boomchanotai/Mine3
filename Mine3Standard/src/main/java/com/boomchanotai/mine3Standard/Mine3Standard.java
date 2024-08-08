package com.boomchanotai.mine3Standard;

import org.bukkit.plugin.java.JavaPlugin;

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
import com.boomchanotai.mine3Standard.listeners.PlayerChat;
import com.boomchanotai.mine3Standard.listeners.PlayerDeath;
import com.boomchanotai.mine3Standard.listeners.PlayerJoinServer;
import com.boomchanotai.mine3Standard.services.TpaService;
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
import com.boomchanotai.mine3Standard.tabcompletion.SpeedTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.StandardTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.TpHereTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.TpTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.TpaHereTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.TpaTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.VanishTabCompletion;

public final class Mine3Standard extends JavaPlugin {
    private static Mine3Standard plugin;

    public static Mine3Standard getPlugin() {
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

        // Dependency
        TpaService tpaService = new TpaService(this);

        // Event Listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinServer(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(), this);

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
    }
}

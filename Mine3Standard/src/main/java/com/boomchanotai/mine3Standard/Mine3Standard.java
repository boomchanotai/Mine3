package com.boomchanotai.mine3Standard;

import org.bukkit.plugin.java.JavaPlugin;

import com.boomchanotai.mine3Standard.commands.BroadcastCommand;
import com.boomchanotai.mine3Standard.commands.BurnCommand;
import com.boomchanotai.mine3Standard.commands.ClearInventoryCommand;
import com.boomchanotai.mine3Standard.commands.FeedCommand;
import com.boomchanotai.mine3Standard.commands.FlyCommand;
import com.boomchanotai.mine3Standard.commands.GameModeCommand;
import com.boomchanotai.mine3Standard.commands.GiveCommand;
import com.boomchanotai.mine3Standard.commands.GodCommand;
import com.boomchanotai.mine3Standard.commands.HealCommand;
import com.boomchanotai.mine3Standard.commands.KillCommand;
import com.boomchanotai.mine3Standard.commands.SpeedCommand;
import com.boomchanotai.mine3Standard.commands.TeleportCommand;
import com.boomchanotai.mine3Standard.commands.VanishCommand;
import com.boomchanotai.mine3Standard.config.Config;
import com.boomchanotai.mine3Standard.tabcompletion.BurnTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.ClearInventoryTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.FeedTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.FlyTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.GameModeTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.GiveTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.GodTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.HealTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.KillTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.SpeedTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.TeleportTabCompletion;
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

        // tp
        getCommand("tp").setExecutor(new TeleportCommand());
        getCommand("tp").setTabCompleter(new TeleportTabCompletion());

        // give
        getCommand("give").setExecutor(new GiveCommand());
        getCommand("give").setTabCompleter(new GiveTabCompletion());

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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

package com.boomchanotai.mine3Standard;

import org.bukkit.plugin.java.JavaPlugin;

import com.boomchanotai.mine3Standard.commands.BroadcastCommand;
import com.boomchanotai.mine3Standard.commands.BurnCommand;
import com.boomchanotai.mine3Standard.commands.ClearInventoryCommand;
import com.boomchanotai.mine3Standard.commands.GameModeCommand;
import com.boomchanotai.mine3Standard.commands.GiveCommand;
import com.boomchanotai.mine3Standard.commands.TeleportCommand;
import com.boomchanotai.mine3Standard.config.Config;
import com.boomchanotai.mine3Standard.tabcompletion.BurnTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.ClearInventoryTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.GameModeTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.GiveTabCompletion;
import com.boomchanotai.mine3Standard.tabcompletion.TeleportTabCompletion;

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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

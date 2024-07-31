package com.boomchanotai.mine3Standard;

import org.bukkit.plugin.java.JavaPlugin;

import com.boomchanotai.mine3Standard.commands.TeleportTabCompletion;
import com.boomchanotai.mine3Standard.commands.GiveCommand;
import com.boomchanotai.mine3Standard.commands.GiveTabCompletion;
import com.boomchanotai.mine3Standard.commands.TeleportCommand;
import com.boomchanotai.mine3Standard.config.Config;
import com.boomchanotai.mine3Standard.repository.SpigotRepository;

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

        SpigotRepository spigotRepository = new SpigotRepository();

        // tp
        getCommand("tp").setExecutor(new TeleportCommand(spigotRepository));
        getCommand("tp").setTabCompleter(new TeleportTabCompletion());

        // give
        getCommand("give").setExecutor(new GiveCommand(spigotRepository));
        getCommand("give").setTabCompleter(new GiveTabCompletion());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

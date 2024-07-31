package com.boomchanotai.mine3Standard;

import org.bukkit.plugin.java.JavaPlugin;

import com.boomchanotai.mine3Standard.commands.TeleportTabCompletion;
import com.boomchanotai.mine3Standard.commands.Teleportcommand;
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

        getCommand("tp").setExecutor(new Teleportcommand(spigotRepository));
        getCommand("tp").setTabCompleter(new TeleportTabCompletion());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

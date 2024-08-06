package com.boomchanotai.mine3Permission;

import org.bukkit.plugin.java.JavaPlugin;

import com.boomchanotai.mine3Permission.command.PermissionCommand;
import com.boomchanotai.mine3Permission.config.Config;
import com.boomchanotai.mine3Permission.listeners.PlayerAuth;
import com.boomchanotai.mine3Permission.postgres.Postgres;
import com.boomchanotai.mine3Permission.tabcompletion.PermissionTabCompletion;

public final class Mine3Permission extends JavaPlugin {
    private static Mine3Permission plugin;

    public static Mine3Permission getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        // Configuration
        Config.saveDefaultConfig();
        Config.loadConfig();

        // Postgres
        Postgres.connect();

        // Register Event
        getServer().getPluginManager().registerEvents(new PlayerAuth(), plugin);

        // Commands
        // permission
        getCommand("permission").setExecutor(new PermissionCommand());
        getCommand("permission").setTabCompleter(new PermissionTabCompletion());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

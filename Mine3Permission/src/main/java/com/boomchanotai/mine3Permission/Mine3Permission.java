package com.boomchanotai.mine3Permission;

import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.plugin.java.JavaPlugin;

import com.boomchanotai.core.logger.Logger;
import com.boomchanotai.core.postgres.Postgres;
import com.boomchanotai.mine3Permission.command.PermissionCommand;
import com.boomchanotai.mine3Permission.config.Config;
import com.boomchanotai.mine3Permission.listeners.PlayerAuth;
import com.boomchanotai.mine3Permission.repositories.PostgresRepository;
import com.boomchanotai.mine3Permission.services.PermissionManager;
import com.boomchanotai.mine3Permission.tabcompletion.PermissionTabCompletion;

public final class Mine3Permission extends JavaPlugin {
    private static Mine3Permission plugin;

    public static Mine3Permission getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        Logger.init(this);

        // Configuration
        Config.saveDefaultConfig();
        Config.loadConfig();

        // Postgres
        Postgres.init(Config.POSTGRES_HOST, Config.POSTGRES_USERNAME, Config.POSTGRES_PASSWORD);

        // Dependencies
        PostgresRepository pgRepo = new PostgresRepository();
        PermissionManager permissionManager = new PermissionManager(pgRepo);
        permissionManager.initializePermission();

        // Register Event
        getServer().getPluginManager().registerEvents(new PlayerAuth(pgRepo, permissionManager), plugin);

        // Commands
        // permission
        getCommand("permission").setExecutor(new PermissionCommand(permissionManager));
        getCommand("permission").setTabCompleter(new PermissionTabCompletion(permissionManager));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void initializeDatabase() throws SQLException {
        Statement statement = Postgres.getConnection().createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS \"groups\" (\n" +
                "\t\"address\" TEXT PRIMARY KEY,\n" +
                "\t\"group\" TEXT NOT NULL,\n" +
                "\t\"metadata\" JSONB NOT NULL DEFAULT '{}',\n" +
                "\t\"created_at\" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "\t\"updated_at\" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP\n" +
                ");");
    }
}

package com.boomchanotai.mine3AuthPaper;

import org.bukkit.plugin.java.JavaPlugin;

import com.boomchanotai.mine3AuthPaper.listeners.PaperListener;

public final class Mine3AuthPaper extends JavaPlugin {
    private static Mine3AuthPaper plugin;

    public static Mine3AuthPaper getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new PaperListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

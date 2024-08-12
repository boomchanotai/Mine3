package com.boomchanotai.mine3Velocity;

import com.boomchanotai.mine3Velocity.config.Config;
import com.boomchanotai.mine3Velocity.listeners.ConnectionEvent;
import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import java.nio.file.Path;

import org.slf4j.Logger;

@Plugin(id = "mine3velocity", name = "Mine3Velocity", version = "1.0-SNAPSHOT", description = "velocity support", url = "https://boomchanotai.com", authors = {
        "BoomChanotai" })
public class Mine3Velocity {
    private static Mine3Velocity plugin;

    private final ProxyServer server;
    private Logger logger;

    public static Mine3Velocity getPlugin() {
        return plugin;
    }

    public static Logger getLogger() {
        return plugin.logger;
    }

    @Inject
    public Mine3Velocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        plugin = this;
        this.server = server;
        this.logger = logger;

        Config.loadConfig(dataDirectory);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("Mine3Velocity has been initialized");
        server.getEventManager().register(this, new ConnectionEvent());
    }
}

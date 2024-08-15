package com.boomchanotai.mine3AuthProxy;

import com.boomchanotai.mine3AuthProxy.config.Config;
import com.boomchanotai.mine3AuthProxy.listeners.PlayerDisconnectListener;
import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import java.nio.file.Path;

import org.slf4j.Logger;

@Plugin(id = "mine3authproxy", name = "Mine3AuthProxy", version = "1.0-SNAPSHOT", description = "Velocity proxy wrapper for Mine3Auth", url = "https://boomchanotai.com", authors = {
        "BoomChanotai" })
public class Mine3AuthProxy {
    private static Mine3AuthProxy plugin;

    public static Mine3AuthProxy getPlugin() {
        return plugin;
    }

    private final ProxyServer server;

    @Inject
    public Mine3AuthProxy(ProxyServer proxyServer, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = proxyServer;
        com.boomchanotai.core.logger.Logger.init(logger);
        plugin = this;

        Config.loadConfig(dataDirectory);
    }

    public ProxyServer getServer() {
        return server;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getEventManager().register(this, DisconnectEvent.class, new PlayerDisconnectListener());
    }
}

package com.boomchanotai.mine3Velocity;

import com.boomchanotai.listeners.ConnectionEvent;
import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;

@Plugin(id = "mine3velocity", name = "Mine3Velocity", version = "1.0-SNAPSHOT", description = "velocity support", url = "https://boomchanotai.com", authors = {
        "BoomChanotai" })
public class Mine3Velocity {

    private final ProxyServer server;
    private Logger logger;

    @Inject
    public Mine3Velocity(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("Mine3Velocity has been initialized");
        server.getEventManager().register(this, new ConnectionEvent());
    }
}

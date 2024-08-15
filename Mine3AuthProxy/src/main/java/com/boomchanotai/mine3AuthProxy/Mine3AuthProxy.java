package com.boomchanotai.mine3AuthProxy;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import org.slf4j.Logger;

@Plugin(
        id = "mine3authproxy",
        name = "Mine3AuthProxy",
        version = "1.0-SNAPSHOT"
        , description = "Velocity proxy wrapper for Mine3Auth"
        , url = "https://boomchanotai.com"
        , authors = {"BoomChanotai"}
)
public class Mine3AuthProxy {

    @Inject
    private Logger logger;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
    }
}
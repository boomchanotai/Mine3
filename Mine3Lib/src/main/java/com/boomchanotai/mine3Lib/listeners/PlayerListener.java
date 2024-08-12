package com.boomchanotai.mine3Lib.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.boomchanotai.mine3Lib.events.PreAuthEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PreAuthEvent preAuthEvent = new PreAuthEvent(event.getPlayer());
        Bukkit.getPluginManager().callEvent(preAuthEvent);
    }
}

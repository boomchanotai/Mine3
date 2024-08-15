package com.boomchanotai.mine3AuthPaper.listeners;

import java.net.http.WebSocket.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.boomchanotai.mine3Lib.core.entities.Address;
import com.boomchanotai.mine3Lib.core.repositories.RedisRepository;
import com.boomchanotai.mine3Lib.events.PlayerDisconnectEvent;
import com.boomchanotai.mine3Lib.events.PreAuthEvent;

public class PlayerListener implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerAuth(PreAuthEvent event) {
        Address address = RedisRepository.getAddress(event.getPlayer().getUniqueId());
        if (address == null) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDisconnect(PlayerDisconnectEvent event) {
        event.setCancelled(true);
    }
}

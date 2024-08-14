package com.boomchanotai.mine3Lib.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.boomchanotai.core.entities.Address;
import com.boomchanotai.mine3Lib.events.PlayerDisconnectEvent;
import com.boomchanotai.mine3Lib.events.PreAuthEvent;
import com.boomchanotai.mine3Lib.repositories.PlayerRepository;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PreAuthEvent preAuthEvent = new PreAuthEvent(event.getPlayer());
        Bukkit.getPluginManager().callEvent(preAuthEvent);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Address address = PlayerRepository.getAddress(player.getUniqueId());
        if (address == null) {
            return;
        }

        PlayerDisconnectEvent playerDisconnectEvent = new PlayerDisconnectEvent(address, player);
        Bukkit.getPluginManager().callEvent(playerDisconnectEvent);
    }
}

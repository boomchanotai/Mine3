package com.boomchanotai.mine3Auth.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.boomchanotai.mine3Auth.services.AuthService;

public class PlayerJoinQuitEvent implements Listener {

    private AuthService authService;

    public PlayerJoinQuitEvent(AuthService authService) {
        this.authService = authService;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();
        authService.connect(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        Player player = event.getPlayer();
        authService.disconnect(player);
    }
}

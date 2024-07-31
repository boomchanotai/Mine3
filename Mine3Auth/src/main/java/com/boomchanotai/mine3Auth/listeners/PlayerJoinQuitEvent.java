package com.boomchanotai.mine3Auth.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.boomchanotai.mine3Auth.service.PlayerService;

public class PlayerJoinQuitEvent implements Listener {

    private PlayerService playerService;

    public PlayerJoinQuitEvent(PlayerService playerService) {
        this.playerService = playerService;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        playerService.connectPlayer(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        playerService.disconnectPlayer(player);
    }
}

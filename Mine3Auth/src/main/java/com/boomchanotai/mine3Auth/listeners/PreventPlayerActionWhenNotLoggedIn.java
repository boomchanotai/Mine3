package com.boomchanotai.mine3Auth.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.boomchanotai.mine3Auth.services.PlayerService;

public class PreventPlayerActionWhenNotLoggedIn implements Listener {
    private PlayerService playerService;

    public PreventPlayerActionWhenNotLoggedIn(PlayerService playerService) {
        this.playerService = playerService;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!playerService.getPlayerList().isEmpty()
                && playerService.getPlayerList().getOrDefault(player.getUniqueId(), false)) {
            return;
        }

        playerService.sendPreventActionMessage(player);
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (!playerService.getPlayerList().isEmpty()
                && playerService.getPlayerList().getOrDefault(player.getUniqueId(), false)) {
            return;
        }

        playerService.sendPreventActionMessage(player);
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!playerService.getPlayerList().isEmpty()
                && playerService.getPlayerList().getOrDefault(player.getUniqueId(), false)) {
            return;
        }

        playerService.sendPreventActionMessage(player);
        event.setCancelled(true);
    }
}

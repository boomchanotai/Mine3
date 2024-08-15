package com.boomchanotai.mine3Auth.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.boomchanotai.mine3Auth.services.PlayerService;
import com.boomchanotai.mine3Lib.repositories.PlayerRepository;

public class PlayerActionListener implements Listener {
    private PlayerService playerService;

    public PlayerActionListener(PlayerService playerService) {
        this.playerService = playerService;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!PlayerRepository.getUUIDList().isEmpty()
                && PlayerRepository.getUUIDList().getOrDefault(player.getUniqueId(), false)) {
            return;
        }

        playerService.sendPreventActionMessage(player);
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (!PlayerRepository.getUUIDList().isEmpty()
                && PlayerRepository.getUUIDList().getOrDefault(player.getUniqueId(), false)) {
            return;
        }

        playerService.sendPreventActionMessage(player);
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!PlayerRepository.getUUIDList().isEmpty()
                && PlayerRepository.getUUIDList().getOrDefault(player.getUniqueId(), false)) {
            return;
        }

        playerService.sendPreventActionMessage(player);
        event.setCancelled(true);
    }
}

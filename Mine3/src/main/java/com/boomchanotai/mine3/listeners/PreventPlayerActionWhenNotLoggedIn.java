package com.boomchanotai.mine3.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.boomchanotai.mine3.repository.SpigotRepository;

import static com.boomchanotai.mine3.config.Config.*;

import java.util.HashMap;
import java.util.UUID;

public class PreventPlayerActionWhenNotLoggedIn implements Listener {
    private SpigotRepository spigotRepo;
    private static HashMap<UUID, Boolean> isLoggedIn;

    public PreventPlayerActionWhenNotLoggedIn(SpigotRepository spigotRepo) {
        this.spigotRepo = spigotRepo;
        isLoggedIn = new HashMap<>();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!isLoggedIn.isEmpty() && isLoggedIn.getOrDefault(player.getUniqueId(), false)) {
            return;
        }

        spigotRepo.sendMessage(player, AUTH_PREVENT_ACTION_MESSAGE);
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (!isLoggedIn.isEmpty() && isLoggedIn.getOrDefault(player.getUniqueId(), false)) {
            return;
        }

        spigotRepo.sendMessage(player, AUTH_PREVENT_ACTION_MESSAGE);
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!isLoggedIn.isEmpty() && isLoggedIn.getOrDefault(player.getUniqueId(), false)) {
            return;
        }

        spigotRepo.sendMessage(player, AUTH_PREVENT_ACTION_MESSAGE);
        event.setCancelled(true);
    }

    public static void playerConnected(UUID playerUUID) {
        isLoggedIn.put(playerUUID, true);
    }

    public static void playerDisconnected(UUID playerUUID) {
        isLoggedIn.remove(playerUUID);
    }

    public static void disconnectAll() {
        isLoggedIn.clear();
    }
}

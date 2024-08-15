package com.boomchanotai.mine3Lib.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.boomchanotai.core.entities.Address;
import com.boomchanotai.mine3Lib.Mine3Lib;
import com.boomchanotai.mine3Lib.events.PlayerAuthEvent;
import com.boomchanotai.mine3Lib.events.PlayerDisconnectEvent;
import com.boomchanotai.mine3Lib.events.PreAuthEvent;
import com.boomchanotai.mine3Lib.repositories.PlayerRepository;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Address address = PlayerRepository.getAddress(player.getUniqueId());
        // If player is already authenticated
        // This case should happen when using velocity
        if (address != null) {
            PlayerRepository.addPlayerList(address, player);
            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    PlayerAuthEvent playerAuthEvent = new PlayerAuthEvent(address, player);
                    Bukkit.getPluginManager().callEvent(playerAuthEvent);
                }
            };
            runnable.runTaskLater(Mine3Lib.getPlugin(), 0);
            return;
        }

        // If player is not authenticated
        PreAuthEvent preAuthEvent = new PreAuthEvent(event.getPlayer());
        Bukkit.getPluginManager().callEvent(preAuthEvent);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Address address = PlayerRepository.getAddress(player.getUniqueId());
        // If player is not authenticated yet
        if (address == null) {
            return;
        }
        PlayerRepository.removePlayerList(address, player);

        // If player is authenticated
        PlayerDisconnectEvent playerDisconnectEvent = new PlayerDisconnectEvent(address, player);
        Bukkit.getPluginManager().callEvent(playerDisconnectEvent);
    }
}

package com.boomchanotai.mine3Standard.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.boomchanotai.mine3Standard.config.SpawnConfig;

public class PlayerJoinServer implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // set respawn point
        Location spawnLocation = SpawnConfig.getSpawnLocation();
        player.setRespawnLocation(spawnLocation, true);
    }
}

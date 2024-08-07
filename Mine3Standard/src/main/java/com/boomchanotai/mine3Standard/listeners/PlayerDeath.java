package com.boomchanotai.mine3Standard.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.boomchanotai.mine3Standard.config.SpawnConfig;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();

        // set respawn point
        Location spawnLocation = SpawnConfig.getSpawnLocation();
        player.setRespawnLocation(spawnLocation, true);
    }

}

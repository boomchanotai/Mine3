package com.boomchanotai.mine3Standard.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.boomchanotai.mine3Lib.repository.PlayerRepository;
import com.boomchanotai.mine3Standard.config.SpawnConfig;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity().getPlayer();

        // set respawn point
        Location spawnLocation = SpawnConfig.getSpawnLocation();
        player.setRespawnLocation(spawnLocation, true);

        // replace death message
        String address = PlayerRepository.getAddress(player.getUniqueId());
        e.setDeathMessage(e.getDeathMessage().replaceAll(player.getName(), address));
    }

}

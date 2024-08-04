package com.boomchanotai.mine3Auth.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.boomchanotai.mine3Lib.address.Address;
import com.boomchanotai.mine3Lib.repository.PlayerRepository;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();

        // replace death message
        // if player is not registered, use default name
        Address address = PlayerRepository.getAddress(player.getUniqueId());
        if (address == null)
            return;

        event.setDeathMessage(event.getDeathMessage().replaceAll(player.getName(), address.getShortAddress()));
    }
}

package com.boomchanotai.mine3Auth.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.boomchanotai.mine3Auth.services.PlayerService;
import com.boomchanotai.mine3Lib.address.Address;
import com.boomchanotai.mine3Lib.events.PlayerAuthEvent;
import com.boomchanotai.mine3Lib.repositories.PlayerRepository;

public class AuthListener implements Listener {
    private PlayerService playerService;

    public AuthListener(PlayerService playerService) {
        this.playerService = playerService;
    }

    @EventHandler
    public void onPlayerAuth(PlayerAuthEvent event) {
        Address address = event.getAddress();
        Player player = event.getPlayer();

        playerService.sendJoinMessage(address);

        playerService.sendAuthenticatedTitle(player);
        PlayerRepository.sendMessage(player, "Login as " + address);
    }
}

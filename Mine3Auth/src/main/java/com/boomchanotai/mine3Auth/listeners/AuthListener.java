package com.boomchanotai.mine3Auth.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.boomchanotai.mine3Auth.services.AuthService;
import com.boomchanotai.mine3Auth.services.PlayerService;
import com.boomchanotai.mine3Lib.core.entities.Address;
import com.boomchanotai.mine3Lib.events.PlayerAuthEvent;
import com.boomchanotai.mine3Lib.events.PlayerDisconnectEvent;
import com.boomchanotai.mine3Lib.events.PreAuthEvent;
import com.boomchanotai.mine3Lib.repositories.PlayerRepository;

public class AuthListener implements Listener {
    private PlayerService playerService;
    private AuthService authService;

    public AuthListener(AuthService authService, PlayerService playerService) {
        this.authService = authService;
        this.playerService = playerService;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void preAuth(PreAuthEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        authService.connect(player);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerAuth(PlayerAuthEvent event) {
        Address address = event.getAddress();
        Player player = event.getPlayer();

        playerService.sendJoinMessage(address);

        playerService.sendAuthenticatedTitle(player);
        PlayerRepository.sendMessage(player, "Login as " + address);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDisconnect(PlayerDisconnectEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Address address = event.getAddress();
        Player player = event.getPlayer();

        authService.disconnect(address, player);
    }
}

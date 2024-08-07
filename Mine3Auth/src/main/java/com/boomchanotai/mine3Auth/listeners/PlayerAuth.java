package com.boomchanotai.mine3Auth.listeners;

import java.util.Objects;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.boomchanotai.mine3Auth.entities.PlayerData;
import com.boomchanotai.mine3Auth.entities.PlayerLocation;
import com.boomchanotai.mine3Auth.logger.Logger;
import com.boomchanotai.mine3Auth.repositories.PostgresRepository;
import com.boomchanotai.mine3Auth.services.PlayerService;
import com.boomchanotai.mine3Lib.address.Address;
import com.boomchanotai.mine3Lib.events.PlayerAuthEvent;
import com.boomchanotai.mine3Lib.repository.PlayerRepository;

public class PlayerAuth implements Listener {
    private PostgresRepository pgRepo;
    private PlayerService playerService;

    public PlayerAuth(PlayerService playerService, PostgresRepository pgRepo) {
        this.playerService = playerService;
        this.pgRepo = pgRepo;
    }

    @EventHandler
    public void onPlayerAuth(PlayerAuthEvent event) {
        Address address = event.getAddress();
        Player player = event.getPlayer();

        playerService.sendJoinMessage(address);

        // 1. Create User in Postgres if not exist
        if (!pgRepo.isAddressExist(address)) {
            PlayerLocation playerLocation = new PlayerLocation(
                    player.getLocation().getX(),
                    player.getLocation().getY(),
                    player.getLocation().getZ(),
                    player.getLocation().getYaw(),
                    player.getLocation().getPitch(),
                    Objects.requireNonNull(player.getLocation().getWorld()));

            PlayerData createPlayerData = new PlayerData(address, "", playerLocation);
            pgRepo.createNewPlayer(createPlayerData);
        }

        // 2. Update user login status
        pgRepo.setUserLoggedIn(address);

        // 3. Restore player state
        PlayerData playerData = pgRepo.getPlayerData(address);
        if (playerData == null) {
            Logger.warning("PlayerData is null", "Failed to get player data in database.", address.getValue());
            return;
        }

        playerService.restorePlayerState(player, playerData);
        playerService.setPlayerActiveState(player);
        playerService.sendAuthenticatedTitle(player);
        PlayerRepository.sendMessage(player, "Login as " + address);
    }

}

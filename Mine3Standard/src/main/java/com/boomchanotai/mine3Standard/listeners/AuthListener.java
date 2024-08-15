package com.boomchanotai.mine3Standard.listeners;

import java.util.Objects;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.boomchanotai.mine3Lib.core.entities.Address;
import com.boomchanotai.mine3Lib.events.PlayerAuthEvent;
import com.boomchanotai.mine3Lib.events.PlayerDisconnectEvent;
import com.boomchanotai.mine3Standard.entities.PlayerData;
import com.boomchanotai.mine3Standard.entities.PlayerLocation;
import com.boomchanotai.core.logger.Logger;
import com.boomchanotai.mine3Standard.repositories.PostgresRepository;
import com.boomchanotai.mine3Standard.services.PlayerService;

public class AuthListener implements Listener {
    private PlayerService playerService;
    private PostgresRepository pgRepo;

    public AuthListener(PlayerService playerService, PostgresRepository pgRepo) {
        this.playerService = playerService;
        this.pgRepo = pgRepo;
    }

    @EventHandler
    public void onConnect(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        playerService.setPlayerIdleState(player);
    }

    @EventHandler
    public void onAuthenticate(PlayerAuthEvent event) {
        Address address = event.getAddress();
        Player player = event.getPlayer();

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
            Logger.warning("PlayerData is null", "Failed to get player data in database.",
                    address.getValue());
            return;
        }

        playerService.restorePlayerState(player, playerData);
        playerService.setPlayerActiveState(player);

        // Teleport Player to Last Location
        if (!event.isForceRespawn()) {
            playerService.teleportPlayerToLastLocation(player, playerData);
        }
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        Player player = event.getPlayer();
        Address address = event.getAddress();

        // Update user inventory & Clear player state
        PlayerLocation playerLocation = new PlayerLocation(
                player.getLocation().getX(),
                player.getLocation().getY(),
                player.getLocation().getZ(),
                player.getLocation().getYaw(),
                player.getLocation().getPitch(),
                Objects.requireNonNull(player.getLocation().getWorld()));
        PlayerData playerData = new PlayerData(
                address,
                "",
                false,
                player.getLevel(),
                player.getExp(),
                player.getHealth(),
                player.getFoodLevel(),
                player.getGameMode(),
                player.getFlySpeed(),
                player.getWalkSpeed(),
                player.getAllowFlight(),
                player.isFlying(),
                player.isOp(),
                player.isBanned(),
                player.getActivePotionEffects(),
                player.getInventory().getContents(),
                player.getEnderChest().getContents(),
                playerLocation);
        pgRepo.updateUserInventory(playerData);

        playerService.clearPlayerState(player); // Clear player state
    }
}

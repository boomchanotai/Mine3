package com.boomchanotai.mine3Auth.service;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.web3j.crypto.Keys;

import com.boomchanotai.mine3Auth.Mine3Auth;
import com.boomchanotai.mine3Auth.entity.PlayerCacheData;
import com.boomchanotai.mine3Auth.entity.PlayerData;
import com.boomchanotai.mine3Auth.entity.PlayerLocation;
import com.boomchanotai.mine3Lib.logger.Logger;
import com.boomchanotai.mine3Auth.repository.PostgresRepository;
import com.boomchanotai.mine3Auth.repository.RedisRepository;
import com.boomchanotai.mine3Lib.repository.PlayerRepository;

public class AuthService {
    private PlayerService playerService;

    private PostgresRepository pgRepo;
    private RedisRepository redisRepo;

    private static final int TOKEN_LENGTH = 32;

    public AuthService(PlayerService playerService,
            PostgresRepository pgRepo,
            RedisRepository redisRepo) {
        this.playerService = playerService;
        this.pgRepo = pgRepo;
        this.redisRepo = redisRepo;
    }

    private static String getRandomHexString(int numchars) {
        Random r = new Random();
        StringBuilder stringBuffer = new StringBuilder();
        while (stringBuffer.length() < numchars) {
            stringBuffer.append(Integer.toHexString(r.nextInt()));
        }

        return stringBuffer.substring(0, numchars);
    }

    // When player connect to server
    public void connect(Player player) {
        // 1. Set Player to idle state
        playerService.setPlayerIdleState(player);

        // 2. Set Redis Token
        UUID playerUUID = player.getUniqueId();
        String token = getRandomHexString(TOKEN_LENGTH);
        redisRepo.setToken(token, playerUUID);

        // 3. Send Login URL & Title
        playerService.sendLoginURL(player, token);
        playerService.sendWelcomeTitle(player);
    }

    // When player login with website
    public void authenticate(String token, String address) throws Exception {
        String parsedAddress = Keys.toChecksumAddress(address);

        // 1. Get player UUID
        UUID playerUUID = redisRepo.getPlayerUUIDFromToken(token);
        if (playerUUID == null) {
            Logger.warning("INVALID_TOKEN", "Can't get player from token.", parsedAddress);
            throw new Exception("INVALID_TOKEN");
        }

        // 2. Get player from spigot
        Player player = Mine3Auth.getPlugin().getServer().getPlayer(playerUUID);
        if (player == null) {
            Logger.warning("PLAYER_NOT_IN_GAME", "Not found player in game.", parsedAddress);
            throw new Exception("PLAYER_NOT_IN_GAME");
        }

        // 3. Get Player from Address
        if (PlayerRepository.getPlayer(parsedAddress) != null) {
            Logger.warning("ADDRESS_ALREADY_USED", "Address already used", parsedAddress);
            throw new Exception("ADDRESS_ALREADY_USED");
        }

        // 4. Set Player
        PlayerRepository.setPlayer(parsedAddress, player); // Set player in Mine3Lib
        playerService.addPlayer(playerUUID); // Add player to player list
        redisRepo.deleteToken(token); // Delete login token

        // 5. Create User in Database if not exist
        if (!pgRepo.isAddressExist(parsedAddress)) {
            PlayerLocation playerLocation = new PlayerLocation(
                    player.getLocation().getX(),
                    player.getLocation().getY(),
                    player.getLocation().getZ(),
                    player.getLocation().getYaw(),
                    player.getLocation().getPitch(),
                    Objects.requireNonNull(player.getLocation().getWorld()));

            PlayerData createPlayerData = new PlayerData(parsedAddress, playerLocation);
            pgRepo.createNewPlayer(createPlayerData);
        }

        // 6. Update user login status
        pgRepo.setUserLoggedIn(parsedAddress);

        // 7. Restore player data
        PlayerData playerData = pgRepo.getPlayerData(parsedAddress);
        if (playerData == null) {
            Logger.warning("PlayerData is null", "Failed to get player data in database.", parsedAddress);
            return;
        }
        playerService.restorePlayerState(player, playerData);
        playerService.setPlayerActiveState(player);
        playerService.sendAuthenticatedTitle(player);
        PlayerRepository.sendMessage(player, "Login as " + parsedAddress);
    }

    public void disconnect(Player player) {
        UUID playerUUID = player.getUniqueId();

        // 1. If Player is not logged in, do nothing
        PlayerCacheData playerCacheData = redisRepo.getPlayerInfo(playerUUID);
        if (playerCacheData == null) {
            return;
        }

        // 2. Update user inventory & Clear player state
        PlayerLocation playerLocation = new PlayerLocation(
                player.getLocation().getX(),
                player.getLocation().getY(),
                player.getLocation().getZ(),
                player.getLocation().getYaw(),
                player.getLocation().getPitch(),
                Objects.requireNonNull(player.getLocation().getWorld()));

        PlayerData playerData = new PlayerData(
                playerCacheData.getAddress(),
                false,
                player.getLevel(),
                player.getExp(),
                player.getHealth(),
                player.getFoodLevel(),
                player.getGameMode(),
                player.getFlySpeed(),
                player.getWalkSpeed(),
                player.isFlying(),
                player.isOp(),
                player.getActivePotionEffects(),
                player.getInventory().getContents(),
                player.getEnderChest().getContents(),
                playerLocation);

        pgRepo.updateUserInventory(playerData);

        // 3. Remove player from system
        playerService.removePlayer(playerUUID); // Remove player from player list
        playerService.clearPlayerState(player); // Clear player state
        PlayerRepository.removePlayer(playerCacheData.getAddress()); // Remove player from Mine3Lib
    }
}

package com.boomchanotai.mine3Auth.services;

import java.util.Random;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.boomchanotai.mine3Auth.Mine3Auth;
import com.boomchanotai.core.logger.Logger;
import com.boomchanotai.mine3Auth.repositories.RedisRepository;
import com.boomchanotai.mine3Lib.core.entities.Address;
import com.boomchanotai.mine3Lib.repositories.PlayerRepository;

import static com.boomchanotai.mine3Auth.config.Config.AUTH_FORCE_RESPAWN;

public class AuthService {
    private PlayerService playerService;

    private RedisRepository redisRepo;

    private static final int TOKEN_LENGTH = 32;

    public AuthService(PlayerService playerService,
            RedisRepository redisRepo) {
        this.playerService = playerService;
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
        // Create Token & set Redis Token
        UUID playerUUID = player.getUniqueId();
        String token = getRandomHexString(TOKEN_LENGTH);
        redisRepo.setToken(token, playerUUID);

        // Send Login URL & Title
        playerService.sendLoginURL(player, token);
        playerService.sendWelcomeTitle(player);
    }

    // When player login with website
    public void authenticate(String token, Address address) throws Exception {
        // 1. Get player UUID
        UUID playerUUID = redisRepo.getPlayerUUIDFromToken(token);
        if (playerUUID == null) {
            Logger.warning("INVALID_TOKEN", "Can't get player from token.", address.getValue());
            throw new Exception("INVALID_TOKEN");
        }

        // 2. Get player from spigot
        Player player = Mine3Auth.getPlugin().getServer().getPlayer(playerUUID);
        if (player == null) {
            Logger.warning("PLAYER_NOT_IN_GAME", "Not found player in game.", address.getValue());
            throw new Exception("PLAYER_NOT_IN_GAME");
        }

        // 3. Get Player from Address
        if (PlayerRepository.getPlayer(address) != null) {
            Logger.warning("ADDRESS_ALREADY_USED", "Address already used", address.getValue());
            throw new Exception("ADDRESS_ALREADY_USED");
        }

        // 4. Set Player
        PlayerRepository.setPlayer(address, player, AUTH_FORCE_RESPAWN); // Add player to Redis & PlayerList
        redisRepo.deleteToken(token); // Delete login token
    }

    public void disconnect(Player player) {
        Address address = PlayerRepository.getAddress(player.getUniqueId());
        if (address == null) {
            return;
        }

        disconnect(address, player);
    }

    public void disconnect(Address address, Player player) {
        PlayerRepository.removePlayer(address); // Remove player from Redis & PlayerList
        playerService.sendQuitMessage(address);
    }
}

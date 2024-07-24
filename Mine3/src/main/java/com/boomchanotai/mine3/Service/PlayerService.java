package com.boomchanotai.mine3.Service;

import com.boomchanotai.mine3.Listeners.PreventPlayerActionWhenNotLoggedIn;
import com.boomchanotai.mine3.Logger.Logger;
import com.boomchanotai.mine3.Mine3;
import com.boomchanotai.mine3.Entity.PlayerCacheData;
import com.boomchanotai.mine3.Entity.PlayerData;
import com.boomchanotai.mine3.Repository.PostgresRepository;
import com.boomchanotai.mine3.Repository.RedisRepository;
import com.boomchanotai.mine3.Repository.SpigotRepository;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.json.JSONObject;
import org.web3j.crypto.Keys;

import java.sql.SQLException;
import java.util.*;

import static com.boomchanotai.mine3.Config.Config.*;

public class PlayerService {
    private PostgresRepository pgRepo;
    private RedisRepository redisRepo;
    private SpigotRepository spigotRepo;
    private static final int TOKEN_LENGTH = 32;

    public PlayerService(PostgresRepository pgRepo, RedisRepository redisRepo, SpigotRepository spigotRepo) {
        this.pgRepo = pgRepo;
        this.redisRepo = redisRepo;
        this.spigotRepo = spigotRepo;
    }

    private static String getRandomHexString(int numchars) {
        Random r = new Random();
        StringBuilder stringBuffer = new StringBuilder();
        while (stringBuffer.length() < numchars) {
            stringBuffer.append(Integer.toHexString(r.nextInt()));
        }

        return stringBuffer.substring(0, numchars);
    }

    public boolean isPlayerInGame(UUID playerUUID) {
        Player player = Mine3.getInstance().getServer().getPlayer(playerUUID);
        return player != null;
    }

    public void connectPlayer(Player player) {
        spigotRepo.setPlayerDefaultState(player);

        UUID playerUUID = player.getUniqueId();

        String token = getRandomHexString(TOKEN_LENGTH);
        redisRepo.setToken(token, playerUUID);

        // Send Message to player
        TextComponent titleComponent = new TextComponent(
                ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, TITLE));
        titleComponent.setColor(ChatColor.BLUE);

        String url = AUTH_WEBSITE_TOKEN_BASE_URL + token;
        TextComponent urlComponent = new TextComponent(
                ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, AUTH_CLICK_TO_LOGIN_MESSAGE));
        urlComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        urlComponent.setUnderlined(true);
        urlComponent.setColor(ChatColor.GRAY);

        spigotRepo.sendMessage(
                player,
                titleComponent,
                urlComponent);

        spigotRepo.sendTitle(player,
                AUTH_JOIN_SERVER_TITLE_TITLE,
                AUTH_JOIN_SERVER_TITLE_SUBTITLE,
                AUTH_JOIN_SERVER_TITLE_FADE_OUT,
                AUTH_JOIN_SERVER_TITLE_STAY,
                AUTH_JOIN_SERVER_TITLE_FADE_IN);
    }

    public void playerLogin(String token, String address) throws Exception {
        String parsedAddress = Keys.toChecksumAddress(address);
        // 1. Get Player UUID
        UUID playerUUID = redisRepo.getPlayerUUIDFromToken(token);
        if (playerUUID == null) {
            throw new Exception("INVALID_TOKEN");
        }
        ;

        // 2. Check Player in game
        boolean isPlayerInGame = isPlayerInGame(playerUUID);
        if (!isPlayerInGame) {
            throw new Exception("PLAYER_NOT_IN_GAME");
        }

        // 3. Check address already exist
        UUID checkPlayer = redisRepo.getPlayerFromAddress(parsedAddress);
        if (checkPlayer != null) {
            throw new Exception("ADDRESS_ALREADY_USED");
        }
        ;

        // 4. Store Player: (json) address
        JSONObject playerInfo = new JSONObject();
        playerInfo.put("address", parsedAddress);
        redisRepo.setPlayerInfo(playerUUID, playerInfo.toString());

        // 5. Store Address: PlayerUUID
        redisRepo.setAddress(playerUUID, parsedAddress);

        // 6. Delete Token
        redisRepo.deleteToken(token);

        // 7. Game Logic
        PreventPlayerActionWhenNotLoggedIn.playerConnected(playerUUID);

        Player player = Mine3.getInstance().getServer().getPlayer(playerUUID);
        if (player == null)
            return;

        // 8. Create User in Database
        try {
            pgRepo.createNewPlayer(
                    parsedAddress,
                    player.getLocation().getX(),
                    player.getLocation().getY(),
                    player.getLocation().getZ(),
                    player.getLocation().getYaw(),
                    player.getLocation().getPitch(),
                    Objects.requireNonNull(player.getLocation().getWorld()).getName());
        } catch (SQLException exception) {
            Logger.warning(exception.getMessage());
            throw exception;
        }

        // 9. Update user Login
        try {
            pgRepo.setUserLoggedIn(parsedAddress);
        } catch (SQLException exception) {
            Logger.warning(exception.getMessage());
            throw exception;
        }

        // 10. get player info
        PlayerData playerData = pgRepo.getPlayerData(parsedAddress);
        if (playerData == null)
            return;
        spigotRepo.restorePlayerState(player, playerData);

        // 11. Send Title
        spigotRepo.sendTitle(
                player,
                AUTH_LOGGED_IN_TITLE_TITLE,
                AUTH_LOGGED_IN_TITLE_SUBTITLE,
                AUTH_LOGGED_IN_TITLE_FADE_IN,
                AUTH_LOGGED_IN_TITLE_STAY,
                AUTH_LOGGED_IN_TITLE_FADE_OUT);
    }

    public void disconnectPlayer(Player player) {
        UUID playerUUID = player.getUniqueId();

        PreventPlayerActionWhenNotLoggedIn.playerDisconnected(playerUUID);

        PlayerCacheData playerCacheData = redisRepo.getPlayerInfo(playerUUID);
        if (playerCacheData == null)
            return;
        String address = playerCacheData.getAddress();

        try {
            pgRepo.updateUserInventory(
                    address,
                    player.getLevel(),
                    player.getExp(),
                    player.getHealth(),
                    player.getFoodLevel(),
                    player.getInventory().getArmorContents(),
                    player.getInventory().getContents(),
                    player.getEnderChest().getContents(),
                    player.getLocation().getX(),
                    player.getLocation().getY(),
                    player.getLocation().getZ(),
                    player.getLocation().getYaw(),
                    player.getLocation().getPitch(),
                    Objects.requireNonNull(player.getLocation().getWorld()).getName());
        } catch (SQLException exception) {
            Logger.warning(exception.getMessage());
        }

        redisRepo.deleteAddress(address);
        redisRepo.deletePlayerInfo(playerUUID);
    }

    public PlayerCacheData getPlayer(UUID playerUUID) {
        return redisRepo.getPlayerInfo(playerUUID);
    }
}

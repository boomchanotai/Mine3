package com.boomchanotai.mine3.Service;

import com.boomchanotai.mine3.Listeners.PreventPlayerActionWhenNotLoggedIn;
import com.boomchanotai.mine3.Mine3;
import com.boomchanotai.mine3.Repository.RedisRepository;
import com.fasterxml.jackson.databind.JsonNode;
import io.javalin.http.HttpStatus;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.json.JSONObject;
import org.web3j.crypto.Keys;

import java.util.Random;
import java.util.UUID;

import static com.boomchanotai.mine3.Config.Config.*;
import static com.boomchanotai.mine3.Config.Config.AUTH_JOIN_SERVER_TITLE_FADE_OUT;

public class PlayerService {
    private static final int TOKEN_LENGTH = 32;
    private static String getRandomHexString(int numchars){
        Random r = new Random();
        StringBuilder stringBuffer = new StringBuilder();
        while(stringBuffer.length() < numchars){
            stringBuffer.append(Integer.toHexString(r.nextInt()));
        }

        return stringBuffer.substring(0, numchars);
    }

    public static class LoginRequest {
        public String token;
        public String address;
        public String signature;
        public long timestamp;
    }

    public static boolean isPlayerInGame(UUID playerUUID) {
        Player player = Mine3.getInstance().getServer().getPlayer(playerUUID);
        return player != null;
    }

    public static void connectPlayer(Player player) {
        UUID playerUUID = player.getUniqueId();

        String token = getRandomHexString(TOKEN_LENGTH);
        RedisRepository.setToken(token, playerUUID);

        // Send Message to player
        TextComponent titleComponent = new TextComponent(ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, TITLE));
        titleComponent.setColor(ChatColor.BLUE);

        String url = AUTH_WEBSITE_TOKEN_BASE_URL + token;
        TextComponent urlComponent = new TextComponent(ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, AUTH_CLICK_TO_LOGIN_MESSAGE));
        urlComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        urlComponent.setUnderlined(true);
        urlComponent.setColor(ChatColor.GRAY);

        player.sendTitle(
                org.bukkit.ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, AUTH_JOIN_SERVER_TITLE_TITLE),
                org.bukkit.ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, AUTH_JOIN_SERVER_TITLE_SUBTITLE),
                AUTH_JOIN_SERVER_TITLE_FADE_IN,
                AUTH_JOIN_SERVER_TITLE_STAY,
                AUTH_JOIN_SERVER_TITLE_FADE_OUT);

        player.spigot().sendMessage(titleComponent, urlComponent);
    }

    public static void playerLogin(LoginRequest loginRequest) throws Exception {
        String parsedAddress = Keys.toChecksumAddress(loginRequest.address);
        // 1. Get Player UUID
        UUID playerUUID = RedisRepository.getPlayerUUIDFromToken(loginRequest.token);
        if (playerUUID == null) {
            throw new Exception("INVALID_TOKEN");
        };

        // 2. Check Player in game
        boolean isPlayerInGame = PlayerService.isPlayerInGame(playerUUID);
        if (!isPlayerInGame) {
            throw new Exception("PLAYER_NOT_IN_GAME");
        }

        // 3. Check address already exist
        UUID checkPlayer = RedisRepository.getPlayerFromAddress(parsedAddress);
        if (checkPlayer != null) {
            throw new Exception("ADDRESS_ALREADY_USED");
        };

        // 4. Store Player: (json) address
        JSONObject playerInfo = new JSONObject();
        playerInfo.put("address", parsedAddress);
        RedisRepository.setPlayerInfo(playerUUID, playerInfo.toString());

        // 5. Store Address: PlayerUUID
        RedisRepository.setAddress(playerUUID, parsedAddress);

        // 6. Delete Token
        RedisRepository.deleteToken(loginRequest.token);

        // Game Logic
        PreventPlayerActionWhenNotLoggedIn.playerConnected(playerUUID);

        Player player = Mine3.getInstance().getServer().getPlayer(playerUUID);
        if (player == null) return;

        player.sendTitle(
                org.bukkit.ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, AUTH_LOGGED_IN_TITLE_TITLE),
                org.bukkit.ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, AUTH_LOGGED_IN_TITLE_SUBTITLE),
                AUTH_LOGGED_IN_TITLE_FADE_IN,
                AUTH_LOGGED_IN_TITLE_STAY,
                AUTH_LOGGED_IN_TITLE_FADE_OUT);
    }

    public static void disconnectPlayer(Player player) {
        UUID playerUUID = player.getUniqueId();

        PreventPlayerActionWhenNotLoggedIn.playerDisconnected(playerUUID);

        JsonNode playerInfo = RedisRepository.getPlayerInfo(playerUUID);
        if (playerInfo == null) return;
        String address = playerInfo.get("address").asText();

        RedisRepository.deleteAddress(address);
        RedisRepository.deletePlayerInfo(playerUUID);
    }
}

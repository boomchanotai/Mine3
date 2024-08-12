package com.boomchanotai.mine3Auth.services;

import com.boomchanotai.mine3Lib.address.Address;
import com.boomchanotai.mine3Lib.repositories.PlayerRepository;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static com.boomchanotai.mine3Lib.config.Config.COLOR_CODE_PREFIX;
import static com.boomchanotai.mine3Auth.config.Config.*;

import java.util.*;

public class PlayerService {
    private HashMap<UUID, Boolean> playerList;

    public PlayerService() {
        playerList = new HashMap<>();
    }

    public Map<UUID, Boolean> getPlayerList() {
        return playerList;
    }

    public void addPlayer(UUID playerUUID) {
        playerList.put(playerUUID, true);
    }

    public void removePlayer(UUID playerUUID) {
        playerList.remove(playerUUID);
    }

    public void removeAll() {
        playerList.clear();
    }

    public void sendLoginURL(Player player, String token) {
        String url = AUTH_WEBSITE_TOKEN_BASE_URL + token;
        TextComponent urlComponent = new TextComponent(
                ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, AUTH_CLICK_TO_LOGIN_MESSAGE));
        urlComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        urlComponent.setUnderlined(true);
        urlComponent.setColor(ChatColor.GRAY);

        PlayerRepository.sendMessage(player, urlComponent);
    }

    public void sendWelcomeTitle(Player player) {
        player.sendTitle(
                ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, AUTH_JOIN_SERVER_TITLE_TITLE),
                ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, AUTH_JOIN_SERVER_TITLE_SUBTITLE),
                AUTH_JOIN_SERVER_TITLE_FADE_OUT,
                AUTH_JOIN_SERVER_TITLE_STAY,
                AUTH_JOIN_SERVER_TITLE_FADE_IN);
    }

    public void sendAuthenticatedTitle(Player player) {
        player.sendTitle(
                ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, AUTH_LOGGED_IN_TITLE_TITLE),
                ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, AUTH_LOGGED_IN_TITLE_SUBTITLE),
                AUTH_LOGGED_IN_TITLE_FADE_IN,
                AUTH_LOGGED_IN_TITLE_STAY,
                AUTH_LOGGED_IN_TITLE_FADE_OUT);
    }

    public void sendPreventActionMessage(Player player) {
        PlayerRepository.sendMessage(player, AUTH_PREVENT_ACTION_MESSAGE);
    }

    public void sendJoinMessage(Address address) {
        String message = AUTH_JOIN_MESSAGE.replace("{address}", address.getShortAddress());
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, message));
    }

    public void sendQuitMessage(Address address) {
        String message = AUTH_QUIT_MESSAGE.replace("{address}", address.getShortAddress());
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, message));
    }
}

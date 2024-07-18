package com.boomchanotai.mine3.Listeners;

import com.boomchanotai.mine3.Redis.Redis;
import com.boomchanotai.mine3.Repository.PlayerRepository;
import com.fasterxml.jackson.databind.JsonNode;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.Random;
import java.util.UUID;

import static com.boomchanotai.mine3.Config.Config.*;
import static com.boomchanotai.mine3.Config.Config.AUTH_JOIN_SERVER_TITLE_FADE_OUT;

public class PlayerJoinQuitEvent implements Listener {
    private static final int TOKEN_LENGTH = 32;

    private String getRandomHexString(int numchars){
        Random r = new Random();
        StringBuilder stringBuffer = new StringBuilder();
        while(stringBuffer.length() < numchars){
            stringBuffer.append(Integer.toHexString(r.nextInt()));
        }

        return stringBuffer.substring(0, numchars);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        String token = getRandomHexString(TOKEN_LENGTH);
        PlayerRepository.setToken(token, playerUUID);

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

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        JsonNode playerInfo = PlayerRepository.getPlayerInfo(playerUUID);
        if (playerInfo == null) return;
        String address = playerInfo.get("address").asText();

        PlayerRepository.deleteAddress(address);
        PlayerRepository.deletePlayerInfo(playerUUID);
    }
}

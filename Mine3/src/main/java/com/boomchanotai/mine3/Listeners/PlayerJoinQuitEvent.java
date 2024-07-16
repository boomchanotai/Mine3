package com.boomchanotai.mine3.Listeners;

import com.boomchanotai.mine3.Mine3;
import com.boomchanotai.mine3.Redis.Redis;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import redis.clients.jedis.Jedis;

import java.util.Random;

import static com.boomchanotai.mine3.Mine3.COLOR_CODE_PREFIX;
import static com.boomchanotai.mine3.Mine3.TITLE;

public class PlayerJoinQuitEvent implements Listener {
    private static final int TOKEN_LENGTH = 32;

    // Redis Keys
    public static String TOKEN_PREFIX_KEY;
    public static String PLAYER_PREFIX_KEY;
    public static String IP_PREFIX_KEY;

    private static String CLICK_TO_LOGIN_MESSAGE;
    private static String WEBSITE_TOKEN_BASE_URL;
    private static int LOGIN_SESSION_TIMEOUT_SEC;
    private static int AUTO_LOGIN_TIMEOUT_SEC;

    private String getRandomHexString(int numchars){
        Random r = new Random();
        StringBuilder stringBuffer = new StringBuilder();
        while(stringBuffer.length() < numchars){
            stringBuffer.append(Integer.toHexString(r.nextInt()));
        }

        return stringBuffer.substring(0, numchars);
    }

    public PlayerJoinQuitEvent() {
        FileConfiguration config = Mine3.getInstance().getConfig();
        WEBSITE_TOKEN_BASE_URL = config.getString("auth.website_token_base_url");
        LOGIN_SESSION_TIMEOUT_SEC = config.getInt("auth.login_session_timeout");
        AUTO_LOGIN_TIMEOUT_SEC = config.getInt("auth.auto_login_timeout");
        TOKEN_PREFIX_KEY = config.getString("auth.token_prefix_key");
        IP_PREFIX_KEY = config.getString("auth.ip_prefix_key");
        PLAYER_PREFIX_KEY = config.getString("auth.player_prefix_key");
        CLICK_TO_LOGIN_MESSAGE = config.getString("auth.click_to_login_message");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerUUID = player.getUniqueId().toString();

        player.setWalkSpeed(0);

        String token = getRandomHexString(TOKEN_LENGTH);

        try (Jedis j = Redis.getPool().getResource()) {
            j.setex(TOKEN_PREFIX_KEY + ":" + token, LOGIN_SESSION_TIMEOUT_SEC, playerUUID);
            j.setex(IP_PREFIX_KEY + ":" + playerUUID, AUTO_LOGIN_TIMEOUT_SEC, String.valueOf(player.getAddress()));
        } catch (Exception e) {
            Bukkit.getLogger().warning(e.getMessage());
        }

        // Send Message to player
        TextComponent titleComponent = new TextComponent(ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, TITLE));
        titleComponent.setColor(ChatColor.BLUE);

        String url = WEBSITE_TOKEN_BASE_URL + token;
        TextComponent urlComponent = new TextComponent(ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, CLICK_TO_LOGIN_MESSAGE));
        urlComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        urlComponent.setUnderlined(true);
        urlComponent.setColor(ChatColor.GRAY);

        player.spigot().sendMessage(titleComponent, urlComponent);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String playerUUID = player.getUniqueId().toString();

        try (Jedis j = Redis.getPool().getResource()) {
            j.hdel(PLAYER_PREFIX_KEY, playerUUID);
        } catch (Exception e) {
            Bukkit.getLogger().warning(e.getMessage());
        }
    }
}

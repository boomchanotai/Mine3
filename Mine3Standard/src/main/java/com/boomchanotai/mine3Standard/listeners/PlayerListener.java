package com.boomchanotai.mine3Standard.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.boomchanotai.mine3Standard.config.SpawnConfig;

import static com.boomchanotai.mine3Lib.config.Config.COLOR_CODE_PREFIX;
import static com.boomchanotai.mine3Standard.config.Config.CHAT_FORMAT;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // set respawn point
        Location spawnLocation = SpawnConfig.getSpawnLocation();
        player.setRespawnLocation(spawnLocation, true);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        event.setFormat(ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX,
                CHAT_FORMAT.replace("{displayname}", event.getPlayer().getDisplayName()).replace("{message}",
                        event.getMessage())));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();

        // set respawn point
        Location spawnLocation = SpawnConfig.getSpawnLocation();
        player.setRespawnLocation(spawnLocation, true);
    }
}

package com.boomchanotai.mine3.Listeners;

import com.boomchanotai.mine3.Repository.PlayerRepository;
import com.boomchanotai.mine3.Service.PlayerService;
import com.fasterxml.jackson.databind.JsonNode;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Random;
import java.util.UUID;

import static com.boomchanotai.mine3.Config.Config.*;
import static com.boomchanotai.mine3.Config.Config.AUTH_JOIN_SERVER_TITLE_FADE_OUT;

public class PlayerJoinQuitEvent implements Listener {




    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerService.connectPlayer(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerService.disconnectPlayer(player);
    }
}

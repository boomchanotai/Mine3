package com.boomchanotai.mine3Standard.listeners;

import static com.boomchanotai.mine3Lib.config.Config.COLOR_CODE_PREFIX;
import static com.boomchanotai.mine3Standard.config.Config.CHAT_FORMAT;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        event.setFormat(ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX,
                CHAT_FORMAT.replace("{displayname}", event.getPlayer().getDisplayName()).replace("{message}",
                        event.getMessage())));
    }
}

package com.boomchanotai.mine3Standard.repository;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import static com.boomchanotai.mine3Standard.config.Config.COLOR_CODE_PREFIX;
import static com.boomchanotai.mine3Standard.config.Config.TITLE;

public class SpigotRepository {
    public void sendMessage(CommandSender player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, TITLE + message));
    }

    public void sendMessage(Player player, TextComponent... textComponent) {
        player.spigot().sendMessage(textComponent);
    }

    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(
                ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, title),
                ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, subtitle),
                fadeIn,
                stay,
                fadeOut);
    }
}
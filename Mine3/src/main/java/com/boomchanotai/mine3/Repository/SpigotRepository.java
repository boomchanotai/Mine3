package com.boomchanotai.mine3.Repository;

import static com.boomchanotai.mine3.Config.Config.COLOR_CODE_PREFIX;
import static com.boomchanotai.mine3.Config.Config.TITLE;

import org.bukkit.entity.Player;

import com.boomchanotai.mine3.Entity.PlayerData;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class SpigotRepository {
    public void sendMessage(Player player, String message) {
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

    public void clearPlayerState(Player player) {
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setExp(0.0F);
        player.getInventory().clear();
        player.getEnderChest().clear();
    }

    public void restorePlayerState(Player player, PlayerData playerData) {
        // Set Display Name
        player.setDisplayName(playerData.getAddress());
        player.setPlayerListName(playerData.getAddress());

        // Set Player State
        player.setLevel(playerData.getXpLevel());
        player.setExp(playerData.getXpExp());
        player.setHealth(playerData.getHealth());
        player.setFoodLevel(playerData.getFoodLevel());

        player.getInventory().setContents(playerData.getInventory());
        player.getEnderChest().setContents(playerData.getEnderchest());

        // Set Location
        player.getLocation().setX(playerData.getPlayerLocation().getX());
        player.getLocation().setY(playerData.getPlayerLocation().getY());
        player.getLocation().setZ(playerData.getPlayerLocation().getZ());
        player.getLocation().setYaw(playerData.getPlayerLocation().getYaw());
        player.getLocation().setPitch(playerData.getPlayerLocation().getPitch());
        player.getLocation().setWorld(playerData.getPlayerLocation().getWorld());
    }

    public void setPlayerIdleState(Player player) {
        player.setInvulnerable(true);
    }

    public void setPlayerActiveState(Player player) {
        player.setInvulnerable(false);
    }
}

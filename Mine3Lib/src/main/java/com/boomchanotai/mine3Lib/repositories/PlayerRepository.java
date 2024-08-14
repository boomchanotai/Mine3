package com.boomchanotai.mine3Lib.repositories;

import static com.boomchanotai.mine3Lib.config.Config.*;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.boomchanotai.core.entities.Address;
import com.boomchanotai.core.repositories.RedisRepository;
import com.boomchanotai.mine3Lib.Mine3Lib;
import com.boomchanotai.mine3Lib.events.PlayerAuthEvent;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerRepository {
    private static ArrayList<Address> playerList = new ArrayList<>();

    // Get All Address in Redis
    public static ArrayList<Address> getAllAddress() {
        return RedisRepository.getAllAddress();
    }

    // Get All Player in this server
    public static ArrayList<Address> getOnlinePlayers() {
        return playerList;
    }

    public static Address getAddress(UUID uuid) {
        return RedisRepository.getAddress(uuid);
    }

    public static Player getPlayer(Address address) {
        return RedisRepository.getPlayer(address);
    }

    public static void setPlayer(Address address, Player player, boolean forceRespawn) {
        RedisRepository.setPlayer(address, player);
        playerList.add(address);

        // Call PlayerAuthEvent
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                PlayerAuthEvent playerAuthEvent = new PlayerAuthEvent(address, player, forceRespawn);
                Bukkit.getPluginManager().callEvent(playerAuthEvent);
            }
        };
        runnable.runTaskLater(Mine3Lib.getPlugin(), 0);
    }

    public static void setPlayer(Address address, Player player) {
        setPlayer(address, player, false);
    }

    public static void removePlayer(Address address) {
        Player player = getPlayer(address);
        if (player == null) {
            return;
        }

        RedisRepository.removePlayer(address);
        playerList.remove(address);
    }

    public static void clearPlayer() {
        RedisRepository.clearPlayer();

        playerList.clear();
    }

    public static void sendMessage(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, TITLE + message));
    }

    public static void sendMessage(Player player, TextComponent... textComponent) {
        TextComponent titleComponent = new TextComponent(
                ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, TITLE));
        titleComponent.setColor(ChatColor.BLUE);

        ArrayList<TextComponent> components = new ArrayList<>();
        components.add(titleComponent);
        for (TextComponent component : textComponent) {
            components.add(component);
        }

        TextComponent[] componentsArray = components.toArray(new TextComponent[0]);
        player.spigot().sendMessage(componentsArray);
    }

    public static void broadcastMessage(String message) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, TITLE + message));
    }
}

package com.boomchanotai.mine3Lib.repositories;

import static com.boomchanotai.mine3Lib.config.Config.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.boomchanotai.core.entities.Address;
import com.boomchanotai.core.logger.Logger;
import com.boomchanotai.core.repositories.RedisRepository;
import com.boomchanotai.mine3Lib.Mine3Lib;
import com.boomchanotai.mine3Lib.events.PlayerAuthEvent;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerRepository {
    private static HashMap<UUID, Boolean> playerList = new HashMap<>();
    private static HashMap<Address, Boolean> addressList = new HashMap<>();

    // Plugin PlayerList

    // Get All Player in this server
    public static Set<Address> getOnlinePlayers() {
        return addressList.keySet();
    }

    public static Set<UUID> getOnlineUUIDs() {
        return playerList.keySet();
    }

    public static HashMap<Address, Boolean> getAddressList() {
        return addressList;
    }

    public static HashMap<UUID, Boolean> getUUIDList() {
        return playerList;
    }

    public static void addPlayerList(Address address, Player player) {
        addressList.put(address, true);
        playerList.put(player.getUniqueId(), true);
    }

    public static void removePlayerList(Address address, Player player) {
        addressList.remove(address);
        playerList.remove(player.getUniqueId());
    }

    public static void clearPlayerList() {
        addressList.clear();
        playerList.clear();
    }

    // Redis PlayerList

    // Get All Player in Redis
    public static ArrayList<Address> getAllAddress() {
        return RedisRepository.getAllAddress();
    }

    public static Address getAddress(UUID uuid) {
        return RedisRepository.getAddress(uuid);
    }

    public static Player getPlayer(Address address) {
        UUID playerUUID = RedisRepository.getPlayerUUID(address);
        if (playerUUID == null) {
            return null;
        }

        Player player = Mine3Lib.getPlugin().getServer().getPlayer(playerUUID);
        return player;
    }

    public static void setPlayer(Address address, Player player, boolean forceRespawn) {
        RedisRepository.setPlayer(address, player);
        addPlayerList(address, player);

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
            Logger.warning("unexpected event",
                    "PlayerRepository",
                    "removePlayer",
                    "Can't resolve player from address",
                    address.getValue());
            return;
        }

        RedisRepository.removePlayer(address);
        removePlayerList(address, player);
    }

    public static void clearPlayer() {
        RedisRepository.clearPlayer();
        clearPlayerList();
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

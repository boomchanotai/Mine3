package com.boomchanotai.mine3Lib.repository;

import static com.boomchanotai.mine3Lib.config.Config.*;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import com.boomchanotai.mine3Lib.Mine3Lib;
import com.boomchanotai.mine3Lib.address.Address;
import com.boomchanotai.mine3Lib.logger.Logger;
import com.boomchanotai.mine3Lib.redis.Redis;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import redis.clients.jedis.Jedis;

public class PlayerRepository {
    private static ArrayList<Address> playerList = new ArrayList<>();

    // Get All Address in Redis
    public static ArrayList<Address> getAllAddress() {
        ArrayList<Address> allAddress = new ArrayList<>();
        try (Jedis j = Redis.getPool().getResource()) {
            Set<String> addresses = j.hkeys(PLAYER_ADDRESS_KEY);
            for (String address : addresses) {
                allAddress.add(new Address(address));
            }
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "failed to get all address");
            return null;
        }

        return allAddress;
    }

    // Get All Player in this server
    public static ArrayList<Address> getOnlinePlayers() {
        return playerList;
    }

    public static Address getAddress(UUID uuid) {
        String playerInfo = null;
        try (Jedis j = Redis.getPool().getResource()) {
            playerInfo = j.hget(PLAYER_PLAYER_KEY, uuid.toString());
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "failed to get player from uuid", uuid.toString());
            return null;
        }

        if (playerInfo == null || playerInfo.isEmpty())
            return null;

        JSONObject player = new JSONObject(playerInfo);
        String address = player.getString("address");
        return new Address(address);
    }

    public static Player getPlayer(Address address) {
        String playerUUID = null;
        try (Jedis j = Redis.getPool().getResource()) {
            playerUUID = j.hget(PLAYER_ADDRESS_KEY, address.getValue());
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "failed to get player from address", address.getValue());
            return null;
        }

        if (playerUUID == null || playerUUID.isEmpty())
            return null;

        UUID uuid = UUID.fromString(playerUUID);
        Player player = Mine3Lib.getPlugin().getServer().getPlayer(uuid);
        return player;
    }

    public static void setPlayer(Address address, Player player) {
        // 1. Set player pair of address, (string) uuid
        try (Jedis j = Redis.getPool().getResource()) {
            j.hset(PLAYER_ADDRESS_KEY, address.getValue(), player.getUniqueId().toString());
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "failed to set player address key", address.getValue());
        }

        // 2. Set player pair of uuid, (json) { address }
        JSONObject playerInfo = new JSONObject();
        playerInfo.put("address", address);
        playerInfo.put("name", player.getName());
        playerInfo.put("ipAddress", player.getAddress().getAddress().getHostAddress());
        try (Jedis j = Redis.getPool().getResource()) {
            j.hset(PLAYER_PLAYER_KEY, player.getUniqueId().toString(), playerInfo.toString());
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "failed to set player player key", player.getUniqueId().toString());
        }

        playerList.add(address);
    }

    public static void removePlayer(Address address) {
        Player player = getPlayer(address);

        try (Jedis j = Redis.getPool().getResource()) {
            j.hdel(PLAYER_ADDRESS_KEY, address.getValue());
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "failed to remove player address key", address.getValue());
        }

        try (Jedis j = Redis.getPool().getResource()) {
            j.hdel(PLAYER_PLAYER_KEY, player.getUniqueId().toString());
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "failed to remove player player key", player.getUniqueId().toString());
        }

        playerList.remove(address);
    }

    public static void clearPlayer() {
        try (Jedis j = Redis.getPool().getResource()) {
            j.del(PLAYER_ADDRESS_KEY);
            j.del(PLAYER_PLAYER_KEY);
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "failed to clear player");
        }

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

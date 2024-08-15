package com.boomchanotai.core.repositories;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.json.JSONObject;

import com.boomchanotai.core.entities.Address;
import com.boomchanotai.core.logger.Logger;
import com.boomchanotai.core.redis.Redis;

import redis.clients.jedis.Jedis;

public class RedisRepository {
    public static String addressKey;
    public static String playerKey;

    public static void init(String addressKey, String playerKey) {
        RedisRepository.addressKey = addressKey;
        RedisRepository.playerKey = playerKey;
    }

    public static ArrayList<Address> getAllAddress() {
        ArrayList<Address> allAddress = new ArrayList<>();
        try (Jedis j = Redis.getPool().getResource()) {
            Set<String> addresses = j.hkeys(addressKey);
            for (String address : addresses) {
                allAddress.add(new Address(address));
            }
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "failed to get all address");
            return null;
        }

        return allAddress;
    }

    public static Address getAddress(UUID uuid) {
        String playerInfo = null;
        try (Jedis j = Redis.getPool().getResource()) {
            playerInfo = j.hget(playerKey, uuid.toString());
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

    public static UUID getPlayerUUID(Address address) {
        String playerUUID = null;
        try (Jedis j = Redis.getPool().getResource()) {
            playerUUID = j.hget(addressKey, address.getValue());
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "failed to get player from address", address.getValue());
            return null;
        }

        if (playerUUID == null || playerUUID.isEmpty())
            return null;

        return UUID.fromString(playerUUID);
    }

    public static void setPlayer(Address address, Player player, boolean forceRespawn) {
        // 1. Set player pair of address, (string) uuid
        try (Jedis j = Redis.getPool().getResource()) {
            j.hset(addressKey, address.getValue(), player.getUniqueId().toString());
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "failed to set player address key", address.getValue());
        }

        // 2. Set player pair of uuid, (json) { address }
        JSONObject playerInfo = new JSONObject();
        playerInfo.put("address", address);
        playerInfo.put("name", player.getName());
        playerInfo.put("ipAddress", player.getAddress().getAddress().getHostAddress());
        try (Jedis j = Redis.getPool().getResource()) {
            j.hset(playerKey, player.getUniqueId().toString(), playerInfo.toString());
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "failed to set player player key",
                    player.getUniqueId().toString());
        }
    }

    public static void setPlayer(Address address, Player player) {
        setPlayer(address, player, false);
    }

    public static void removePlayer(Address address) {
        UUID playerUUID = getPlayerUUID(address);
        if (playerUUID == null) {
            return;
        }

        try (Jedis j = Redis.getPool().getResource()) {
            j.hdel(addressKey, address.getValue());
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "failed to remove player address key", address.getValue());
        }

        try (
                Jedis j = Redis.getPool().getResource()) {
            j.hdel(playerKey, playerUUID.toString());
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "failed to remove player player key",
                    playerUUID.toString());
        }
    }

    public static void clearPlayer() {
        try (Jedis j = Redis.getPool().getResource()) {
            j.del(addressKey);
            j.del(playerKey);
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "failed to clear player");
        }
    }
}

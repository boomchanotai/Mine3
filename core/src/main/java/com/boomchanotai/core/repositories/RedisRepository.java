package com.boomchanotai.core.repositories;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.json.JSONObject;

import com.boomchanotai.core.entities.Address;
import com.boomchanotai.core.redis.Redis;

import redis.clients.jedis.Jedis;

public class RedisRepository {
    private Redis redis;

    private String addressKey;
    private String playerKey;

    public RedisRepository(Redis redis, String addressKey, String playerKey) {
        this.redis = redis;
        this.addressKey = addressKey;
        this.playerKey = playerKey;
    }

    // Get All Address in Redis
    public ArrayList<Address> getAllAddress() {
        ArrayList<Address> allAddress = new ArrayList<>();
        Jedis j = redis.getPool().getResource();

        Set<String> addresses = j.hkeys(addressKey);
        for (String address : addresses) {
            allAddress.add(new Address(address));
        }

        return allAddress;
    }

    public Address getAddress(UUID uuid) {
        String playerInfo = null;
        Jedis j = redis.getPool().getResource();
        playerInfo = j.hget(playerKey, uuid.toString());

        if (playerInfo == null || playerInfo.isEmpty())
            return null;

        JSONObject player = new JSONObject(playerInfo);
        String address = player.getString("address");
        return new Address(address);
    }

    public UUID getPlayerUUID(Address address) {
        String playerUUID = null;
        Jedis j = redis.getPool().getResource();
        playerUUID = j.hget(addressKey, address.getValue());

        if (playerUUID == null || playerUUID.isEmpty())
            return null;

        return UUID.fromString(playerUUID);
    }

    public void setPlayer(Address address, Player player, boolean forceRespawn) {

        Jedis j = redis.getPool().getResource();

        // 1. Set player pair of address, (string) uuid
        j.hset(addressKey, address.getValue(), player.getUniqueId().toString());

        // 2. Set player pair of uuid, (json) { address }
        JSONObject playerInfo = new JSONObject();
        playerInfo.put("address", address);
        playerInfo.put("name", player.getName());
        playerInfo.put("ipAddress", player.getAddress().getAddress().getHostAddress());
        j.hset(playerKey, player.getUniqueId().toString(), playerInfo.toString());
    }

    public void setPlayer(Address address, Player player) {
        setPlayer(address, player, false);
    }

    public void removePlayer(Address address) {
        UUID playerUUID = getPlayerUUID(address);
        if (playerUUID == null) {
            return;
        }

        Jedis j = redis.getPool().getResource();
        j.hdel(addressKey, address.getValue());
        j.hdel(playerKey, playerUUID.toString());
    }

    public void clearPlayer() {
        Jedis j = redis.getPool().getResource();
        j.del(addressKey);
        j.del(playerKey);
    }
}

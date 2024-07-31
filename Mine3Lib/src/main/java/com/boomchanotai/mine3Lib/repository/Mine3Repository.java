package com.boomchanotai.mine3Lib.repository;

import static com.boomchanotai.mine3Lib.config.Config.PLAYER_ADDRESS_KEY;
import static com.boomchanotai.mine3Lib.config.Config.PLAYER_PLAYER_KEY;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.json.JSONObject;
import org.web3j.crypto.Keys;

import com.boomchanotai.mine3Lib.Mine3Lib;
import com.boomchanotai.mine3Lib.logger.Logger;
import com.boomchanotai.mine3Lib.redis.Redis;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;

public class Mine3Repository {
    ObjectMapper mapper = new ObjectMapper();

    public static Player getPlayer(String address) {
        String parsedAddress = Keys.toChecksumAddress(address);

        String playerUUID = null;
        try (Jedis j = Redis.getPool().getResource()) {
            playerUUID = j.hget(PLAYER_ADDRESS_KEY, parsedAddress);
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "failed to get player from address", parsedAddress);
            return null;
        }

        if (playerUUID == null || playerUUID.isEmpty())
            return null;

        UUID uuid = UUID.fromString(playerUUID);
        Player player = Mine3Lib.getPlugin().getServer().getPlayer(uuid);
        return player;
    }

    public static void setPlayer(String address, Player player) {
        String parsedAddress = Keys.toChecksumAddress(address);

        // 1. Set player pair of address, (string) uuid
        try (Jedis j = Redis.getPool().getResource()) {
            j.hset(PLAYER_ADDRESS_KEY, parsedAddress, player.getUniqueId().toString());
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "failed to set player address key", parsedAddress);
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
    }

    public static void clearPlayer() {
        try (Jedis j = Redis.getPool().getResource()) {
            j.del(PLAYER_ADDRESS_KEY);
            j.del(PLAYER_PLAYER_KEY);
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "failed to clear player");
        }
    }
}

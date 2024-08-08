package com.boomchanotai.mine3Auth.repositories;

import com.boomchanotai.mine3Auth.entities.PlayerCacheData;
import com.boomchanotai.mine3Auth.logger.Logger;
import com.boomchanotai.mine3Auth.redis.Redis;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.boomchanotai.mine3Lib.address.Address;
import redis.clients.jedis.Jedis;

import static com.boomchanotai.mine3Auth.config.Config.*;

import java.util.UUID;

public class RedisRepository {
    public UUID getPlayerUUIDFromToken(String token) {
        String tokenKey = AUTH_TOKEN_PREFIX_KEY + ":" + token;
        try (Jedis j = Redis.getPool().getResource()) {
            String playerUUIDStr = j.get(tokenKey);
            if (playerUUIDStr == null)
                return null;

            return UUID.fromString(playerUUIDStr);
        } catch (Exception e) {
            Logger.warning(e.getMessage());
        }

        return null;
    }

    public void setToken(String token, UUID playerUUID) {
        String tokenKey = AUTH_TOKEN_PREFIX_KEY + ":" + token;
        try (Jedis j = Redis.getPool().getResource()) {
            j.setex(tokenKey, AUTH_LOGIN_SESSION_TIMEOUT, playerUUID.toString());
        } catch (Exception e) {
            Logger.warning(e.getMessage());
        }
    }

    public void deleteToken(String token) {
        String tokenKey = AUTH_TOKEN_PREFIX_KEY + ":" + token;
        try (Jedis j = Redis.getPool().getResource()) {
            j.del(tokenKey);
        } catch (Exception e) {
            Logger.warning(e.getMessage());
        }
    }

    public PlayerCacheData getPlayerInfo(UUID playerUUID) {

        try (Jedis j = Redis.getPool().getResource()) {
            String info = j.hget(AUTH_PLAYER_KEY, playerUUID.toString());
            if (info == null)
                return null;

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(info);
            String addr = node.get("address").asText();
            Address address = new Address(addr);

            return new PlayerCacheData(address);
        } catch (Exception e) {
            Logger.warning(e.getMessage());
        }

        return null;
    }

    public void setPlayerInfo(UUID playerUUID, String data) {
        try (Jedis j = Redis.getPool().getResource()) {
            j.hset(AUTH_PLAYER_KEY, playerUUID.toString(), data);
        } catch (Exception e) {
            Logger.warning(e.getMessage());
        }
    }

    public void deletePlayerInfo(UUID playerUUID) {
        try (Jedis j = Redis.getPool().getResource()) {
            j.hdel(AUTH_PLAYER_KEY, playerUUID.toString());
        } catch (Exception e) {
            Logger.warning(e.getMessage());
        }
    }

    public UUID getPlayerFromAddress(Address address) {
        try (Jedis j = Redis.getPool().getResource()) {
            String playerUUID = j.hget(AUTH_ADDRESS_KEY, address.getValue());
            if (playerUUID == null || playerUUID.isEmpty())
                return null;

            return UUID.fromString(playerUUID);
        } catch (Exception e) {
            Logger.warning(e.getMessage());
        }

        return null;
    }

    public void setAddress(UUID playerUUID, Address address) {
        try (Jedis j = Redis.getPool().getResource()) {
            j.hset(AUTH_ADDRESS_KEY, address.getValue(), playerUUID.toString());
        } catch (Exception e) {
            Logger.warning(e.getMessage());
        }
    }

    public void deleteAddress(Address address) {
        try (Jedis j = Redis.getPool().getResource()) {
            j.hdel(AUTH_ADDRESS_KEY, address.getValue());
        } catch (Exception e) {
            Logger.warning(e.getMessage());
        }
    }
}
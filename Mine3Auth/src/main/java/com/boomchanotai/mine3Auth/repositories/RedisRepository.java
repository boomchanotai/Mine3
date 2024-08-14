package com.boomchanotai.mine3Auth.repositories;

import com.boomchanotai.core.logger.Logger;
import com.boomchanotai.core.redis.Redis;

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
}

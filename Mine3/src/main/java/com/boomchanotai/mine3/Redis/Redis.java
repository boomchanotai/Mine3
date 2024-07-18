package com.boomchanotai.mine3.Redis;

import com.boomchanotai.mine3.Logger.Logger;
import com.boomchanotai.mine3.Mine3;
import org.bukkit.configuration.file.FileConfiguration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import static com.boomchanotai.mine3.Config.Config.*;

public class Redis {
    private static JedisPool pool;

    public static JedisPool getPool() {
        if (pool == null || pool.isClosed()) {
            connect();
            return pool;
        }

        return pool;
    }

    public static void connect() {
        pool = new JedisPool(REDIS_HOST, REDIS_PORT);

        Logger.info("Connected to redis!");
    }

    public static void close() {
        // Destroy Redis
        try (Jedis j = Redis.getPool().getResource()) {
            j.del(AUTH_ADDRESS_KEY);
            j.del(AUTH_PLAYER_KEY);
        } catch (Exception e) {
            Logger.warning(e.getMessage());
        }

        getPool().close();

        Logger.info("Disconnected to redis!");
    }

}

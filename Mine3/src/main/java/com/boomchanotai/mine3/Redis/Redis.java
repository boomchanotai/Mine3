package com.boomchanotai.mine3.Redis;

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

        Mine3.getInstance().getLogger().info(LOG_TITLE + "Connected to redis!");
    }

    public static void close() {
        try (Jedis j = Redis.getPool().getResource()) {
            j.del(AUTH_ADDRESS_KEY);
            j.del(AUTH_PLAYER_KEY);
        } catch (Exception e) {
            Mine3.getInstance().getServer().getLogger().warning(e.getMessage());
        }

        getPool().close();

        Mine3.getInstance().getLogger().info(LOG_TITLE + "Disconnected to redis!");
    }

}

package com.boomchanotai.mine3.Redis;

import com.boomchanotai.mine3.Mine3;
import org.bukkit.configuration.file.FileConfiguration;
import redis.clients.jedis.JedisPool;

import static com.boomchanotai.mine3.Config.Config.REDIS_HOST;
import static com.boomchanotai.mine3.Config.Config.REDIS_PORT;

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
    }

}

package com.boomchanotai.mine3Velocity.redis;

import redis.clients.jedis.JedisPool;
import static com.boomchanotai.mine3Velocity.config.Config.*;

import com.boomchanotai.mine3Velocity.logger.Logger;

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
        getPool().close();
        Logger.info("Disconnected to redis!");
    }

}

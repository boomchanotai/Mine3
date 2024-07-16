package com.boomchanotai.mine3.Redis;

import com.boomchanotai.mine3.Mine3;
import org.bukkit.configuration.file.FileConfiguration;
import redis.clients.jedis.JedisPool;

public class Redis {
    public static JedisPool pool;

    public static JedisPool getPool() {
        if (pool == null || pool.isClosed()) {
            connect();
            return pool;
        }

        return pool;
    }

    public static void connect() {
        FileConfiguration config = Mine3.getInstance().getConfig();
        String host = config.getString("redis.host");
        int port = config.getInt("redis.port");

        pool = new JedisPool(host, port);
    }

}

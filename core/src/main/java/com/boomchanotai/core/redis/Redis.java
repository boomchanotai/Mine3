package com.boomchanotai.core.redis;

import redis.clients.jedis.JedisPool;

public class Redis {
    private static JedisPool pool = null;
    private static String host;
    private static int port;

    public static JedisPool getPool() {
        if (pool == null || pool.isClosed()) {
            connect();
            return pool;
        }

        return pool;
    }

    public static void init(String host, int port) {
        Redis.host = host;
        Redis.port = port;
        connect();
    }

    public static void connect() {
        pool = new JedisPool(host, port);
    }

    public static void disconnect() {
        getPool().close();
    }
}

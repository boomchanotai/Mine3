package com.boomchanotai.core.redis;

import redis.clients.jedis.JedisPool;

public class Redis {
    private String host;
    private int port;
    private JedisPool pool;

    public Redis(String host, int port) {
        this.host = host;
        this.port = port;
        connect();
    }

    public JedisPool getPool() {
        if (pool == null || pool.isClosed()) {
            connect();
            return pool;
        }

        return pool;
    }

    public void connect() {
        pool = new JedisPool(host, port);
    }

    public void close() {
        getPool().close();
    }
}

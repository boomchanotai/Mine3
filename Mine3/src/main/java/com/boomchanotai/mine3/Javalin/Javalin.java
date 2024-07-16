package com.boomchanotai.mine3.Javalin;

import com.boomchanotai.mine3.Mine3;
import com.boomchanotai.mine3.Redis.Redis;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.http.HttpStatus;
import org.bukkit.Bukkit;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import static com.boomchanotai.mine3.Listeners.PlayerJoinQuitEvent.PLAYER_PREFIX_KEY;
import static com.boomchanotai.mine3.Listeners.PlayerJoinQuitEvent.TOKEN_PREFIX_KEY;
import static com.boomchanotai.mine3.Mine3.LOG_TITLE;

public class Javalin {
    public static io.javalin.Javalin app = null;

    public static io.javalin.Javalin getApp() {
        return app;
    }

    public static void startServer() {
        int port = Mine3.getInstance().getConfig().getInt("httpserver.port");

        try {
            // Temporarily switch the plugin classloader to load Javalin.
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            // BungeeCord:  Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            Thread.currentThread().setContextClassLoader(Mine3.getInstance().getClass().getClassLoader());
            // Create a Javalin instance.
            app = io.javalin.Javalin.create().start(port);
            // Restore default loader.
            Thread.currentThread().setContextClassLoader(classLoader);
            // The created instance can be used outside the class loader.
            app
            .get("/", ctx -> ctx.result("Server is running!"))
            .post("/login", ctx -> {
                ObjectMapper objectMapper = new ObjectMapper();

                JsonNode body = objectMapper.readTree(ctx.body());

                String token = body.findPath("token").asText();
                String walletAddress = body.findPath("walletAddress").asText();

                if (token.isEmpty() || walletAddress.isEmpty()) {
                    ObjectNode res = objectMapper.createObjectNode();
                    res.put("error", "BAD_REQUEST");

                    ctx.status(HttpStatus.BAD_REQUEST).json(res);
                    return;
                }

                String tokenKey = TOKEN_PREFIX_KEY + ":" + token;

                try (Jedis j = Redis.getPool().getResource()) {
                    // 1. Get Player UUID
                    String playerUUID = j.get(tokenKey);
                    if (playerUUID == null) {
                        ObjectNode res = objectMapper.createObjectNode();
                        res.put("error", "TIMEOUT");

                        ctx.status(HttpStatus.BAD_REQUEST).json(res);
                        return;
                    }

                    // 2. Store Player wallet in hash
                    ObjectNode playerInfo = objectMapper.createObjectNode();
                    playerInfo.put("walletAddress", walletAddress);
                    j.hset(PLAYER_PREFIX_KEY, playerUUID, playerInfo.toString());

                    // 3. Delete Token
                    j.del(tokenKey);

                } catch (Exception e) {
                    ObjectNode res = objectMapper.createObjectNode();
                    res.put("error", "INTERNAL_SERVER_ERROR");

                    ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(res);
                }

                ObjectNode res = objectMapper.createObjectNode();
                res.put("result", "success");

                ctx.json(res);
            });
        } catch (Exception e) {
            Mine3.getInstance().getServer().getLogger().warning(LOG_TITLE + e.getMessage());
            Mine3.getInstance().getServer().shutdown();
        }
    }
}

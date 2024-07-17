package com.boomchanotai.mine3.Server;

import com.boomchanotai.mine3.Mine3;
import com.boomchanotai.mine3.Redis.Redis;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.bukkit.entity.Player;
import org.json.JSONObject;
import org.web3j.crypto.Keys;
import redis.clients.jedis.Jedis;

import java.util.UUID;

import static com.boomchanotai.mine3.Config.Config.*;

public class Server {
    private static Javalin app = null;

    public static Javalin getApp() {
        return app;
    }

    public static class LoginRequest {
        public String token;
        public String address;
        public String signature;
        public long timestamp;
    }

    public static void startServer() {
        try {
            // Temporarily switch the plugin classloader to load Javalin.
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            // BungeeCord:  Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            Thread.currentThread().setContextClassLoader(Mine3.getInstance().getClass().getClassLoader());
            // Create a Javalin instance.
            app = Javalin.create(config -> {
                config.bundledPlugins.enableCors(cors -> {
                    cors.addRule(CorsPluginConfig.CorsRule::anyHost);
                });
            }).start(HTTPSERVER_PORT);
            // Restore default loader.
            Thread.currentThread().setContextClassLoader(classLoader);
            // The created instance can be used outside the class loader.
            app.get("/", ctx -> ctx.result("Server is running!"));
            app.post("/login", ctx -> {
                LoginRequest loginRequest = ctx.bodyAsClass(LoginRequest.class);

                if (loginRequest.token.isEmpty() || loginRequest.address.isEmpty() || loginRequest.signature.isEmpty() || loginRequest.timestamp == 0) {
                    JSONObject res = new JSONObject();
                    res.put("error", "BAD_REQUEST");

                    ctx.status(HttpStatus.BAD_REQUEST).json(res.toString());
                    return;
                }

                // Verify Signature with address and timestamp
                String msg = "Sign in to Mine3 and this is my wallet address: " + loginRequest.address + " to sign in " + loginRequest.token + ". now is " + loginRequest.timestamp;
                String recoveredAddress = EthersUtils.verifyMessage(msg, loginRequest.signature);
                if (!Keys.toChecksumAddress(recoveredAddress).equals(Keys.toChecksumAddress(loginRequest.address))) {
                    JSONObject res = new JSONObject();
                    res.put("error", "CANT_VERIFY_SIGNATURE");

                    ctx.status(HttpStatus.BAD_REQUEST).json(res.toString());
                    return;
                }

                UUID playerUUID = null;
                try (Jedis j = Redis.getPool().getResource()) {
                    // 1. Get Player UUID
                    String tokenKey = AUTH_TOKEN_PREFIX_KEY + ":" + loginRequest.token;
                    String playerUUIDStr = j.get(tokenKey);
                    if (playerUUIDStr == null) {
                        JSONObject res = new JSONObject();
                        res.put("error", "SESSION_TIMEOUT");

                        ctx.status(HttpStatus.BAD_REQUEST).json(res.toString());
                        return;
                    }
                    playerUUID = UUID.fromString(playerUUIDStr);

                    // 2. Store Player wallet in hash
                    JSONObject playerInfo = new JSONObject();
                    playerInfo.put("walletAddress", loginRequest.address);
                    j.hset(AUTH_PLAYER_PREFIX_KEY, playerUUIDStr, playerInfo.toString());

                    // 3. Delete Token
                    j.del(tokenKey);
                } catch (Exception e) {
                    JSONObject res = new JSONObject();
                    res.put("error", "INTERNAL_SERVER_ERROR");

                    ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(res.toString());
                }


                if (playerUUID == null) return;
                // Game Logic
                Player player = Mine3.getInstance().getServer().getPlayer(playerUUID);
                if (player == null) return;
                player.setWalkSpeed(0.2F);
                player.sendTitle(
                        org.bukkit.ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, AUTH_LOGGED_IN_TITLE_TITLE),
                        org.bukkit.ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, AUTH_LOGGED_IN_TITLE_SUBTITLE),
                        AUTH_LOGGED_IN_TITLE_FADE_IN,
                        AUTH_LOGGED_IN_TITLE_STAY,
                        AUTH_LOGGED_IN_TITLE_FADE_OUT);

                JSONObject res = new JSONObject();
                res.put("result", "success");

                ctx.json(res.toString());
            });
        } catch (Exception e) {
            Mine3.getInstance().getServer().getLogger().warning(LOG_TITLE + e.getMessage());
            Mine3.getInstance().getServer().shutdown();
        }
    }
}

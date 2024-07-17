package com.boomchanotai.mine3.Server;

import com.boomchanotai.mine3.Mine3;
import com.boomchanotai.mine3.Redis.Redis;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.bukkit.GameMode;
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
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(Mine3.getInstance().getClass().getClassLoader());

            app = Javalin.create(config -> {
                config.bundledPlugins.enableCors(cors -> {
                    cors.addRule(CorsPluginConfig.CorsRule::anyHost);
                });
            }).start(HTTPSERVER_PORT);

            Thread.currentThread().setContextClassLoader(classLoader);

            app.get("/", Server::getServerStatus);
            app.post("/login", Server::login);
        } catch (Exception e) {
            Mine3.getInstance().getServer().getLogger().warning(LOG_TITLE + e.getMessage());
            Mine3.getInstance().getServer().shutdown();
        }
    }

    public static void stopServer() {
        getApp().stop();
    }

    public static void getServerStatus(Context ctx) {
        JSONObject res = new JSONObject();
        res.put("result", "Server is running!");

        ctx.json(res.toString());
    }

    public static void login(Context ctx) {
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

        String address = Keys.toChecksumAddress(recoveredAddress);

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

            // 2. Check Address already exist
            String addressInfo = j.hget(AUTH_ADDRESS_KEY, address);
            if (addressInfo != null) {
                JSONObject res = new JSONObject();
                res.put("error", "ADDRESS_ALREADY_USED");

                ctx.status(HttpStatus.BAD_REQUEST).json(res.toString());
                return;
            }

            // 3. Store player_key (hash) (Player : (JSON) address)
            JSONObject playerInfo = new JSONObject();
            playerInfo.put("address", address);
            j.hset(AUTH_PLAYER_KEY, playerUUID.toString(), playerInfo.toString());

            // 4. Store address_key (hash) (Address : UUID)
            j.hset(AUTH_ADDRESS_KEY, address, playerUUID.toString());

            // 5. Delete Token
            j.del(tokenKey);
        } catch (Exception e) {
            JSONObject res = new JSONObject();
            res.put("error", "INTERNAL_SERVER_ERROR");

            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(res.toString());
        }

        // Game Logic
        if (playerUUID == null) return;
        Player player = Mine3.getInstance().getServer().getPlayer(playerUUID);
        if (player == null) return;

        player.sendTitle(
                org.bukkit.ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, AUTH_LOGGED_IN_TITLE_TITLE),
                org.bukkit.ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, AUTH_LOGGED_IN_TITLE_SUBTITLE),
                AUTH_LOGGED_IN_TITLE_FADE_IN,
                AUTH_LOGGED_IN_TITLE_STAY,
                AUTH_LOGGED_IN_TITLE_FADE_OUT);

        JSONObject res = new JSONObject();
        res.put("result", "success");

        ctx.json(res.toString());
    }
}

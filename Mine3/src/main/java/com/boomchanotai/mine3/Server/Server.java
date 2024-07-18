package com.boomchanotai.mine3.Server;

import com.boomchanotai.mine3.Listeners.PreventPlayerActionWhenNotLoggedIn;
import com.boomchanotai.mine3.Mine3;
import com.boomchanotai.mine3.Repository.PlayerRepository;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.bukkit.entity.Player;
import org.json.JSONObject;
import org.web3j.crypto.Keys;

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

        Mine3.getInstance().getLogger().info(LOG_TITLE + "Http server is running!");
    }

    public static void stopServer() {
        getApp().stop();
        Mine3.getInstance().getLogger().info(LOG_TITLE + "Http server has been stopped!");
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

        // 1. Verify Signature with address and timestamp
        String msg = "Sign in to Mine3 and this is my wallet address: " + loginRequest.address + " to sign in " + loginRequest.token + ". now is " + loginRequest.timestamp;
        String recoveredAddress = EthersUtils.verifyMessage(msg, loginRequest.signature);
        if (!Keys.toChecksumAddress(recoveredAddress).equals(Keys.toChecksumAddress(loginRequest.address))) {
            JSONObject res = new JSONObject();
            res.put("error", "CANT_VERIFY_SIGNATURE");

            ctx.status(HttpStatus.BAD_REQUEST).json(res.toString());
            return;
        }

        // 2. Get Player UUID
        UUID playerUUID = PlayerRepository.getPlayerUUIDFromToken(loginRequest.token);
        if (playerUUID == null) {
            JSONObject res = new JSONObject();
            res.put("error", "INVALID_TOKEN");

            ctx.status(HttpStatus.BAD_REQUEST).json(res.toString());
            return;
        };

        // 3. Check address already exist
        UUID checkPlayer = PlayerRepository.getPlayerFromAddress(recoveredAddress);
        if (checkPlayer != null) {
            JSONObject res = new JSONObject();
            res.put("error", "ADDRESS_ALREADY_USED");

            ctx.status(HttpStatus.BAD_REQUEST).json(res.toString());
            return;
        };

        // 4. Store Player: (json) address
        JSONObject playerInfo = new JSONObject();
        playerInfo.put("address", recoveredAddress);
        PlayerRepository.setPlayerInfo(playerUUID, playerInfo.toString());

        // 5. Store Address: PlayerUUID
        PlayerRepository.setAddress(playerUUID, recoveredAddress);

        // 6. Delete Token
        PlayerRepository.deleteToken(loginRequest.token);

        // Game Logic
        PreventPlayerActionWhenNotLoggedIn.playerConnected(playerUUID);

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

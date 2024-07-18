package com.boomchanotai.mine3.Server;

import com.boomchanotai.mine3.Listeners.PreventPlayerActionWhenNotLoggedIn;
import com.boomchanotai.mine3.Logger.Logger;
import com.boomchanotai.mine3.Mine3;
import com.boomchanotai.mine3.Repository.RedisRepository;
import com.boomchanotai.mine3.Service.PlayerService;
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
            Logger.warning(e.getMessage());
            Mine3.getInstance().getServer().shutdown();
        }

        Logger.info("Http server is running!");
    }

    public static void stopServer() {
        getApp().stop();
        Logger.info("Http server has been stopped!");
    }

    public static void getServerStatus(Context ctx) {
        JSONObject res = new JSONObject();
        res.put("result", "Server is running!");

        ctx.json(res.toString());
    }

    public static void login(Context ctx) {
        PlayerService.LoginRequest loginRequest = ctx.bodyAsClass(PlayerService.LoginRequest.class);

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

        try {
            PlayerService.playerLogin(loginRequest);
        } catch (Exception e) {
            JSONObject res = new JSONObject();
            res.put("error", e.getMessage());

            ctx.status(HttpStatus.BAD_REQUEST).json(res.toString());
            return;
        }

        JSONObject res = new JSONObject();
        res.put("result", "success");

        ctx.json(res.toString());
    }
}

package com.boomchanotai.mine3Auth.server;

import com.boomchanotai.mine3Lib.address.Address;
import com.boomchanotai.mine3Auth.Mine3Auth;
import com.boomchanotai.mine3Auth.logger.Logger;
import com.boomchanotai.mine3Auth.services.AuthService;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.plugin.bundled.CorsPluginConfig;

import static com.boomchanotai.mine3Auth.config.Config.*;

import org.json.JSONObject;

public class Server {
    private static Javalin app = null;
    private static AuthService authService;

    public Server(AuthService authService) {
        Server.authService = authService;
    }

    public static Javalin getApp() {
        return app;
    }

    public void startServer() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(Mine3Auth.getPlugin().getClass().getClassLoader());

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
            Mine3Auth.getPlugin().getServer().shutdown();
        }

        Logger.info("Http server is running!");
    }

    public static void stopServer() {
        Javalin app = getApp();
        if (app == null) {
            return;
        }

        getApp().stop();
        Logger.info("Http server has been stopped!");
    }

    public static void getServerStatus(Context ctx) {
        JSONObject res = new JSONObject();
        res.put("result", "Server is running!");

        ctx.json(res.toString());
    }

    public static class LoginRequest {
        public String token;
        public String address;
        public String signature;
        public long timestamp;
    }

    public static void login(Context ctx) {
        LoginRequest loginRequest = ctx.bodyAsClass(LoginRequest.class);

        if (loginRequest.token.isEmpty() || loginRequest.address.isEmpty() || loginRequest.signature.isEmpty()
                || loginRequest.timestamp == 0) {
            JSONObject res = new JSONObject();
            res.put("error", "BAD_REQUEST");

            ctx.status(HttpStatus.BAD_REQUEST).json(res.toString());
            return;
        }

        // Verify Signature with address and timestamp
        String msg = "Sign in to Mine3 and this is my wallet address: " + loginRequest.address + " to sign in "
                + loginRequest.token + ". now is " + loginRequest.timestamp;
        String recoveredAddr = EthersUtils.verifyMessage(msg, loginRequest.signature);
        Address recoveredAddress = new Address(recoveredAddr);
        Address address = new Address(loginRequest.address);
        if (!recoveredAddress.equals(address)) {
            JSONObject res = new JSONObject();
            res.put("error", "CANT_VERIFY_SIGNATURE");

            ctx.status(HttpStatus.BAD_REQUEST).json(res.toString());
            return;
        }

        try {
            authService.authenticate(loginRequest.token, address);
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

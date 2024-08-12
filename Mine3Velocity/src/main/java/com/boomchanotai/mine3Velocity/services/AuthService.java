package com.boomchanotai.mine3Velocity.services;

import java.util.Random;

import com.boomchanotai.mine3Velocity.repositories.RedisRepository;
import com.velocitypowered.api.proxy.Player;

import net.kyori.adventure.title.TitlePart;

public class AuthService {
    private RedisRepository redisRepo;

    private static final int TOKEN_LENGTH = 32;

    public AuthService(RedisRepository redisRepo) {
        this.redisRepo = redisRepo;
    }

    private static String getRandomHexString(int numchars) {
        Random r = new Random();
        StringBuilder stringBuffer = new StringBuilder();
        while (stringBuffer.length() < numchars) {
            stringBuffer.append(Integer.toHexString(r.nextInt()));
        }

        return stringBuffer.substring(0, numchars);
    }

    public void connect(Player player) {
        String token = getRandomHexString(TOKEN_LENGTH);
        redisRepo.setToken(token, player.getUniqueId());

        // Send login URL
        player.sendTitlePart(TitlePart.TITLE, null);

        player.sendMessage(null);
    }

    public void authenticate() {

    }

    public void disconnect() {

    }
}

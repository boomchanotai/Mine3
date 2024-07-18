package com.boomchanotai.mine3.Service;

import com.boomchanotai.mine3.Mine3;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerService {
    public static boolean isPlayerInGame(UUID playerUUID) {
        Player player = Mine3.getInstance().getServer().getPlayer(playerUUID);
        return player != null;
    }
}

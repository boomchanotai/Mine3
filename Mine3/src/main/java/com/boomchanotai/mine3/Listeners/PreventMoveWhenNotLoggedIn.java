package com.boomchanotai.mine3.Listeners;

import com.boomchanotai.mine3.Redis.Redis;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import redis.clients.jedis.Jedis;

import static com.boomchanotai.mine3.Config.Config.AUTH_PLAYER_KEY;

public class PreventMoveWhenNotLoggedIn implements Listener {
    private boolean isPlayerLoggedIn = false;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (isPlayerLoggedIn) return;

        try (Jedis j = Redis.getPool().getResource()) {
            String playerInfo = j.hget(AUTH_PLAYER_KEY, player.getUniqueId().toString());
            if (playerInfo == null) {
                event.setCancelled(true);
            } else {
                isPlayerLoggedIn = true;
            }
        }
    }
}

package com.boomchanotai.mine3Permission.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.boomchanotai.mine3Lib.events.PlayerAuthEvent;
import com.boomchanotai.mine3Permission.repositories.PostgresRepository;
import com.boomchanotai.mine3Permission.services.PermissionManager;

public class PlayerAuth implements Listener {
    private PostgresRepository pgRepo;
    private PermissionManager permissionManager;

    public PlayerAuth(PostgresRepository pgRepo, PermissionManager permissionManager) {
        this.pgRepo = pgRepo;
        this.permissionManager = permissionManager;
    }

    @EventHandler
    public void onPlayerAuth(PlayerAuthEvent event) {
        String group = pgRepo.getGroup(event.getAddress());
        if (group == null) {
            permissionManager.getDefaultGroup();
        }
        permissionManager.attachPermissionGroup(event.getAddress(), group);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        permissionManager.unattachPermissionGroup(event.getPlayer());
    }

}

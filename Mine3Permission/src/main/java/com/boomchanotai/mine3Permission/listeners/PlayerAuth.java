package com.boomchanotai.mine3Permission.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
        pgRepo.createGroup(event.getAddress(), permissionManager.getDefaultGroup());
        System.out.println(event.getAddress() + " authenticated !");
    }

}

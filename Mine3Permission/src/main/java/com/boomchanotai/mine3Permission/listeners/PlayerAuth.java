package com.boomchanotai.mine3Permission.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.boomchanotai.mine3Lib.events.PlayerAuthEvent;

public class PlayerAuth implements Listener {

    @EventHandler
    public void onPlayerAuth(PlayerAuthEvent event) {
        System.out.println(event.getAddress() + " authenticated !");
    }

}

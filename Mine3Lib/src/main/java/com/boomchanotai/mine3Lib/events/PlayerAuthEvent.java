package com.boomchanotai.mine3Lib.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import com.boomchanotai.mine3Lib.address.Address;

public class PlayerAuthEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Address address;
    private Player player;
    private boolean forceRespawn;

    public PlayerAuthEvent(Address address, Player player) {
        this.player = player;
        this.address = address;
        this.forceRespawn = false;
    }

    public PlayerAuthEvent(Address address, Player player, boolean forceRespawn) {
        this.player = player;
        this.address = address;
        this.forceRespawn = forceRespawn;
    }

    public Address getAddress() {
        return address;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isForceRespawn() {
        return forceRespawn;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

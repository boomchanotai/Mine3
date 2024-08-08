package com.boomchanotai.mine3Lib.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import com.boomchanotai.mine3Lib.address.Address;

public class PlayerAuthEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Address address;
    private Player player;

    public PlayerAuthEvent(Address address, Player player) {
        this.player = player;
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}

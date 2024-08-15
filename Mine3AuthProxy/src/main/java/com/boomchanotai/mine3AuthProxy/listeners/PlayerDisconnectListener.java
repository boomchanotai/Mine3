package com.boomchanotai.mine3AuthProxy.listeners;

import javax.annotation.Nullable;

import com.boomchanotai.core.entities.Address;
import com.boomchanotai.core.repositories.RedisRepository;
import com.velocitypowered.api.event.AwaitingEventExecutor;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.connection.DisconnectEvent;

public class PlayerDisconnectListener implements AwaitingEventExecutor<DisconnectEvent> {

    @Override
    public @Nullable EventTask executeAsync(DisconnectEvent event) {
        if (event.getLoginStatus() == DisconnectEvent.LoginStatus.CONFLICTING_LOGIN) {
            return null;
        }

        return EventTask.async(() -> {
            Address address = RedisRepository.getAddress(event.getPlayer().getUniqueId());
            if (address == null) 
                return;
            

            RedisRepository.removePlayer(address);
        });
    }

}

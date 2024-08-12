package com.boomchanotai.listeners;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;

import net.kyori.adventure.text.Component;

public class ConnectionEvent {

    @Subscribe(order = PostOrder.NORMAL)
    public void onPostLogin(PostLoginEvent event) {
        System.out.println("PostLogin event");
        Player player = event.getPlayer();
        Component component = Component.text("Welcome, ").append(Component.text(player.getUsername()))
                .append(Component.text("!"));
        player.sendMessage(component);
    }

    @Subscribe(order = PostOrder.NORMAL)
    public void onServerConnect(ServerPostConnectEvent event) {
        if (event.getPreviousServer() == null) {
            System.out.println("ServerPostConnectEvent: Previous server is null");
        } else {
            System.out.println(
                    "ServerPostConnectEvent: Previous server is "
                            + event.getPreviousServer().getServerInfo().getName());
        }

        System.out.println("ServerPostConnectEvent: Current server is "
                + event.getPlayer().getCurrentServer().get().getServerInfo().getName());
    }

    @Subscribe(order = PostOrder.NORMAL)
    public void onDisconnect(DisconnectEvent event) {
        // Player player = event.player();
        System.out.println("Disconnected !!!");
        // Address address = PlayerRepository.getAddress(player.getUniqueId());
        // if (address == null) {
        // event.setResult(PreTransferEvent.TransferResult.denied());
        // }

        // event.setResult(PreTransferEvent.TransferResult.allowed());
    }
}

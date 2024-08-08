package com.boomchanotai.mine3Standard.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Lib.address.Address;
import com.boomchanotai.mine3Lib.repositories.PlayerRepository;
import com.boomchanotai.mine3Standard.utils.Utils;

import net.md_5.bungee.api.ChatColor;

public class TpHereCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args.length > 1) {
            return false;
        }

        Player fromPlayer = null;
        Player toPlayer = null;

        // tphere <address> - (Player)
        if (args.length == 1) {
            if (!Utils.isPlayerUsingCommand(sender)) {
                return true;
            }

            if (!Utils.hasPermission(sender, "mine3.tphere")) {
                return true;
            }

            Address toAddress = new Address(args[0]);

            fromPlayer = PlayerRepository.getPlayer(toAddress);
            toPlayer = (Player) sender;
        }

        if (fromPlayer == null || toPlayer == null) {
            Utils.sendCommandReturnMessage(sender, "Address not found.");
            return true;
        }

        if (fromPlayer == toPlayer) {
            Utils.sendCommandReturnMessage(sender, ChatColor.RED + "You can't teleport to same address.");
            return true;
        }

        fromPlayer.teleport(toPlayer);
        PlayerRepository.sendMessage(fromPlayer, "You have been teleported.");

        return true;
    }

}

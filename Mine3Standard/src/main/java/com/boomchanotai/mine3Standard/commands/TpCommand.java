package com.boomchanotai.mine3Standard.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.web3j.crypto.Keys;

import com.boomchanotai.mine3Lib.repository.PlayerRepository;
import com.boomchanotai.mine3Standard.utils.Utils;

import net.md_5.bungee.api.ChatColor;

public class TpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args.length > 2) {
            return false;
        }

        Player fromPlayer = null;
        Player toPlayer = null;

        // tp <address> - (Player)
        if (args.length == 1) {
            if (!Utils.isPlayerUsingCommand(sender)) {
                return true;
            }

            if (!Utils.hasPermission(sender, "mine3.tp")) {
                return true;
            }

            String toAddress = Keys.toChecksumAddress(args[0]);

            fromPlayer = (Player) sender;
            toPlayer = PlayerRepository.getPlayer(toAddress);
        }

        // tp <address> <address> - (Player, Console)
        if (args.length == 2) {
            if (!Utils.hasPermission(sender, "mine3.tp.others")) {
                return true;
            }

            String fromAddress = Keys.toChecksumAddress(args[0]);
            String toAddress = Keys.toChecksumAddress(args[1]);

            fromPlayer = PlayerRepository.getPlayer(fromAddress);
            toPlayer = PlayerRepository.getPlayer(toAddress);
        }

        if (fromPlayer == null || toPlayer == null) {
            Utils.sendCommandReturnMessage(sender, "Address not found.");
            return true;
        }

        if (fromPlayer.equals(toPlayer)) {
            if (fromPlayer.equals(sender)) {
                Utils.sendCommandReturnMessage(sender, ChatColor.RED + "You can't teleport to yourself.");
            } else {
                Utils.sendCommandReturnMessage(sender, ChatColor.RED + "You can't teleport to same address.");
            }

            return true;
        }

        fromPlayer.teleport(toPlayer);
        PlayerRepository.sendMessage(fromPlayer, "You have been teleported.");

        return true;
    }

}

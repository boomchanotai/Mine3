package com.boomchanotai.mine3Standard.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Lib.repository.PlayerRepository;
import com.boomchanotai.mine3Standard.utils.Utils;

public class FlyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 1) {
            return false;
        }

        Player targetPlayer = null;

        // fly - (Player)
        if (args.length == 0) {
            if (!Utils.isPlayerUsingCommand(sender)) {
                return true;
            }

            if (!Utils.hasPermission(sender, "mine3.fly")) {
                return true;
            }

            targetPlayer = (Player) sender;
        }

        // fly <address> - (Player, Console)
        if (args.length == 1) {
            if (!Utils.hasPermission(sender, "mine3.fly.others")) {
                return true;
            }

            targetPlayer = PlayerRepository.getPlayer(args[0]);
        }

        if (targetPlayer == null) {
            Utils.sendCommandReturnMessage(sender, "Address not found.");
            return true;
        }

        targetPlayer.setAllowFlight(!targetPlayer.getAllowFlight());
        if (targetPlayer.getAllowFlight()) {
            PlayerRepository.sendMessage(targetPlayer, "You are now in fly mode.");
        } else {
            PlayerRepository.sendMessage(targetPlayer, "You are no longer in fly mode.");
        }

        return true;
    }

}

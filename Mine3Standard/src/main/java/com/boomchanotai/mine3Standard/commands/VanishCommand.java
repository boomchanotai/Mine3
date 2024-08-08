package com.boomchanotai.mine3Standard.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Lib.address.Address;
import com.boomchanotai.mine3Lib.repositories.PlayerRepository;
import com.boomchanotai.mine3Standard.utils.Utils;

public class VanishCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 1) {
            return false;
        }

        Player targetPlayer = null;

        // vanish - (Player)
        if (args.length == 0) {
            if (!Utils.isPlayerUsingCommand(sender)) {
                return true;
            }

            if (!Utils.hasPermission(sender, "mine3.vanish")) {
                return true;
            }

            targetPlayer = (Player) sender;
        }

        // vanish <address> - (Player, Console)
        if (args.length == 1) {
            if (!Utils.hasPermission(sender, "mine3.vanish.others")) {
                return true;
            }

            Address address = new Address(args[0]);
            targetPlayer = PlayerRepository.getPlayer(address);
        }

        if (targetPlayer == null) {
            Utils.sendCommandReturnMessage(sender, "Address not found.");
            return true;
        }

        targetPlayer.setInvisible(!targetPlayer.isInvisible());
        if (targetPlayer.isInvisible()) {
            PlayerRepository.sendMessage(targetPlayer, "You are now in vanish mode.");
        } else {
            PlayerRepository.sendMessage(targetPlayer, "You are no longer in vanish mode.");
        }

        return true;
    }

}

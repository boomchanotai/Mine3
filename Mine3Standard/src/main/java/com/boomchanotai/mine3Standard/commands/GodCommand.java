package com.boomchanotai.mine3Standard.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Lib.address.Address;
import com.boomchanotai.mine3Lib.repositories.PlayerRepository;
import com.boomchanotai.mine3Standard.utils.Utils;

public class GodCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 1) {
            return false;
        }

        Player targetPlayer = null;

        // god - (Player)
        if (args.length == 0) {
            if (!Utils.isPlayerUsingCommand(sender)) {
                return true;
            }

            if (!Utils.hasPermission(sender, "mine3.god")) {
                return true;
            }

            targetPlayer = (Player) sender;
        }

        // god <address> - (Player, Console)
        if (args.length == 1) {
            if (!Utils.hasPermission(sender, "mine3.god.others")) {
                return true;
            }

            Address address = new Address(args[0]);
            targetPlayer = PlayerRepository.getPlayer(address);
        }

        if (targetPlayer == null) {
            Utils.sendCommandReturnMessage(sender, "Address not found.");
            return true;
        }

        targetPlayer.setInvulnerable(!targetPlayer.isInvulnerable());
        if (targetPlayer.isInvulnerable()) {
            PlayerRepository.sendMessage(targetPlayer, "You are now in god mode.");
        } else {
            PlayerRepository.sendMessage(targetPlayer, "You are no longer in god mode.");
        }

        return true;
    }

}

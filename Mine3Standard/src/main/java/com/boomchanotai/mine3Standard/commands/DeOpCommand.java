package com.boomchanotai.mine3Standard.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Lib.core.entities.Address;
import com.boomchanotai.mine3Lib.repositories.PlayerRepository;
import com.boomchanotai.mine3Standard.utils.Utils;

public class DeOpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            return false;
        }

        // deop <player> - (Player, Console)
        if (!Utils.hasPermission(sender, "mine3.op")) {
            return true;
        }

        Address address = new Address(args[0]);

        Player targetPlayer = PlayerRepository.getPlayer(address);
        if (targetPlayer == null) {
            Utils.sendCommandReturnMessage(sender, "Player not found.");
            return true;
        }

        if (!targetPlayer.isOp()) {
            Utils.sendCommandReturnMessage(sender,
                    address.getShortAddress() + " is already not an operator.");
            return true;
        }

        targetPlayer.setOp(false);
        PlayerRepository.sendMessage(targetPlayer,
                "You are no longer an operator.");
        return true;
    }

}

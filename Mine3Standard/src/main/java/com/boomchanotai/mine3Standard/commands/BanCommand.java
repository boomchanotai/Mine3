package com.boomchanotai.mine3Standard.commands;

import java.util.Date;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Lib.repository.PlayerRepository;
import com.boomchanotai.mine3Standard.utils.Utils;

public class BanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        // ban <address> - (Player, Console)
        if (!Utils.hasPermission(sender, "mine3.ban")) {
            return true;
        }

        String reason = "You have been banned.";
        if (args.length > 1) {
            // reason = args[1] + args[2] + ... + args[n]
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                sb.append(args[i]);
                sb.append(" ");
            }
            reason = sb.toString();
        }

        Player targetPlayer = PlayerRepository.getPlayer(args[0]);
        if (targetPlayer == null) {
            Utils.sendCommandReturnMessage(sender, "Address not found.");
            return true;
        }

        targetPlayer.ban(reason, (Date) null, null);
        Utils.sendCommandReturnMessage(sender, args[0] + " has been banned.");

        return true;
    }

}

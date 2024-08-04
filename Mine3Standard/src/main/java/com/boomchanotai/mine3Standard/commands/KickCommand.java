package com.boomchanotai.mine3Standard.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Lib.address.Address;
import com.boomchanotai.mine3Lib.repository.PlayerRepository;
import com.boomchanotai.mine3Standard.Mine3Standard;
import com.boomchanotai.mine3Standard.utils.Utils;

public class KickCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        // kick <address> - (Player, Console)
        if (!Utils.hasPermission(sender, "mine3.kick")) {
            return true;
        }

        String reason = "You have been kicked.";
        if (args.length > 1) {
            // reason = args[1] + args[2] + ... + args[n]
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                sb.append(args[i]);
                sb.append(" ");
            }
            reason = sb.toString();
        }

        if (args[0].toLowerCase().equals("all")) {
            for (Player p : Mine3Standard.getPlugin().getServer().getOnlinePlayers()) {
                p.kickPlayer(reason);
            }
            Utils.sendCommandReturnMessage(sender, "All players have been kicked.");

            return true;
        }

        Address address = new Address(args[0]);
        Player targetPlayer = PlayerRepository.getPlayer(address);
        if (targetPlayer == null) {
            Utils.sendCommandReturnMessage(sender, "Address not found.");
            return true;
        }

        targetPlayer.kickPlayer(reason);
        Utils.sendCommandReturnMessage(sender, args[0] + " has been kicked.");

        return true;
    }

}

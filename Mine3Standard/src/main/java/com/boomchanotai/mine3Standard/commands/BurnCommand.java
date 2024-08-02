package com.boomchanotai.mine3Standard.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Lib.repository.PlayerRepository;
import com.boomchanotai.mine3Standard.utils.Utils;

public class BurnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 1) {
            return false;
        }

        // burn - (Player)
        if (args.length == 0) {
            if (!Utils.isPlayerUsingCommand(sender)) {
                return true;
            }

            if (!Utils.hasPermission(sender, "mine3.burn")) {
                return true;
            }

            Player player = (Player) sender;
            player.setFireTicks(100);
            return true;
        }

        // burn <address> - (Player, Console)
        if (args.length == 1) {
            if (!Utils.hasPermission(sender, "mine3.burn.others")) {
                return true;
            }

            Player targetPlayer = PlayerRepository.getPlayer(args[0]);
            if (targetPlayer == null) {
                Utils.sendCommandReturnMessage(sender, "Address not found.");
                return true;
            }

            targetPlayer.setFireTicks(100);
            return true;
        }

        return false;
    }
}

package com.boomchanotai.mine3Standard.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Lib.logger.Logger;
import com.boomchanotai.mine3Lib.repository.PlayerRepository;

public class BurnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 1) {
            return false;
        }

        // burn - Burn the player
        if (args.length == 0 && sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("mine3.burn")) {
                player.sendMessage("You don't have permission to use this command.");
                return true;
            }

            player.setFireTicks(100);
            return true;
        }

        // burn <address> - Burn the player
        if (args.length == 1) {
            if (sender instanceof Player && !sender.hasPermission("mine3.burn.others")) {
                ((Player) sender).sendMessage("You don't have permission to use this command.");
                return true;
            }

            Player targetPlayer = PlayerRepository.getPlayer(args[0]);
            if (targetPlayer == null) {
                if (sender instanceof Player) {
                    PlayerRepository.sendMessage((Player) sender, "Address not found.");
                } else {
                    Logger.warning("Address not found.");
                }
                return true;
            }

            targetPlayer.setFireTicks(100);
            return true;
        }

        return false;
    }
}

package com.boomchanotai.mine3Auth.commands;

import java.util.Date;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Auth.logger.Logger;
import com.boomchanotai.mine3Lib.address.Address;
import com.boomchanotai.mine3Lib.repositories.PlayerRepository;

import net.md_5.bungee.api.ChatColor;

public class BanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        // ban <address> - (Player, Console)
        if (!sender.hasPermission("mine3.ban")) {
            PlayerRepository.sendMessage((Player) sender,
                    ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        Address address = new Address(args[0]);

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

        Player targetPlayer = PlayerRepository.getPlayer(address);
        if (targetPlayer == null) {
            if (sender instanceof Player) {
                PlayerRepository.sendMessage((Player) sender, ChatColor.RED + "Player not found.");
            } else {
                Logger.warning("Player not found.");
            }
            return true;
        }

        targetPlayer.ban(reason, (Date) null, null);
        if (sender instanceof Player) {
            PlayerRepository.sendMessage((Player) sender, args[0] + " has been banned.");
        } else {
            Logger.warning(args[0] + " has been banned.");
        }

        return true;
    }

}

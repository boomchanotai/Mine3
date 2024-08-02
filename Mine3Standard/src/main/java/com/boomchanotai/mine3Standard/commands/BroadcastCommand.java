package com.boomchanotai.mine3Standard.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Lib.repository.PlayerRepository;

public class BroadcastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args.length > 1) {
            return false;
        }

        if (sender instanceof Player && !sender.hasPermission("mine3.broadcast")) {
            PlayerRepository.sendMessage((Player) sender, "You don't have permission to use this command.");
            return true;
        }

        String message = args[0];
        PlayerRepository.broadcastMessage(message);

        return true;
    }

}

package com.boomchanotai.mine3Standard.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.boomchanotai.mine3Lib.repository.PlayerRepository;

public class BroadcastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args.length > 1) {
            return false;
        }

        String message = args[0];
        PlayerRepository.broadcastMessage(message);

        return true;
    }

}

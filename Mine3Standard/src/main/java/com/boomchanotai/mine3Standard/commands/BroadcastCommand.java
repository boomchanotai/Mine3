package com.boomchanotai.mine3Standard.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.boomchanotai.mine3Lib.repositories.PlayerRepository;
import com.boomchanotai.mine3Standard.utils.Utils;

public class BroadcastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args.length > 1) {
            return false;
        }

        // broadcast <message> - Broadcast a message to all players
        if (!Utils.hasPermission(sender, "mine3.broadcast")) {
            return true;
        }

        String message = args[0];
        PlayerRepository.broadcastMessage(message);

        return true;
    }

}

package com.boomchanotai.mine3Standard.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Lib.logger.Logger;
import com.boomchanotai.mine3Lib.repository.PlayerRepository;

public class Utils {
    public static boolean hasPermission(CommandSender sender, String permission) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission(permission)) {
            PlayerRepository.sendMessage(player, "You don't have permission to use this command.");
            return false;
        }

        return true;
    }

    public static boolean isPlayerUsingCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            Logger.info("Only player can use this command!");
            return false;
        }

        return true;
    }

    public static void sendCommandReturnMessage(CommandSender sender, String message) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayerRepository.sendMessage(player, message);
            return;
        }

        Logger.info("Address not found.");
    }
}

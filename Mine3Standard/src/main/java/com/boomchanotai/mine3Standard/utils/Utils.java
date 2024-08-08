package com.boomchanotai.mine3Standard.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Standard.logger.Logger;

import net.md_5.bungee.api.ChatColor;

import com.boomchanotai.mine3Lib.repositories.PlayerRepository;

public class Utils {
    public static boolean hasPermission(CommandSender sender, String permission) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission(permission)) {
            PlayerRepository.sendMessage(player, ChatColor.RED + "You don't have permission to use this command.");
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

        Logger.info(message);
    }
}

package com.boomchanotai.mine3Standard.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
}

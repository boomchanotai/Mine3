package com.boomchanotai.mine3Lib.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Lib.config.Config;
import com.boomchanotai.mine3Lib.logger.Logger;
import com.boomchanotai.mine3Lib.repository.PlayerRepository;

import net.md_5.bungee.api.ChatColor;

public class LibCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args.length > 1) {
            return false;
        }

        if (args[0].equals("reload")) {
            if (!sender.hasPermission("mine3.lib.reload")) {
                PlayerRepository.sendMessage((Player) sender,
                        ChatColor.RED + "You don't have permission to use this command.");
                return true;
            }

            Config.reloadConfig();

            if (sender instanceof Player) {
                PlayerRepository.sendMessage((Player) sender, "Mine3Auth config has been reloaded.");
            } else {
                Logger.info("Mine3Auth config has been reloaded.");
            }

            return true;
        }

        return false;
    }

}
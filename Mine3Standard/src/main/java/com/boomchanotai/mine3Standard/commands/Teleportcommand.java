package com.boomchanotai.mine3Standard.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Lib.logger.Logger;
import com.boomchanotai.mine3Lib.repository.Mine3Repository;
import com.boomchanotai.mine3Standard.repository.SpigotRepository;

public class Teleportcommand implements CommandExecutor {
    SpigotRepository spigotRepository;

    public Teleportcommand(SpigotRepository spigotRepository) {
        this.spigotRepository = spigotRepository;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        if (args.length == 1 && sender instanceof Player) {
            String toAddress = args[0];

            // Teleport to the player
            Player player = (Player) sender;

            Player toPlayer = Mine3Repository.getPlayer(toAddress);
            if (toPlayer == null) {
                spigotRepository.sendMessage(sender, ChatColor.RED + "Player not found.");
                return true;
            }

            if (player == toPlayer) {
                spigotRepository.sendMessage(sender, ChatColor.RED + "You can't teleport to yourself.");
                return true;
            }

            player.teleport(toPlayer);

            return true;
        }

        if (args.length == 2) {
            String fromAddress = args[0];
            String toAddress = args[1];

            // Teleport from the player to the player
            Player fromPlayer = Mine3Repository.getPlayer(fromAddress);
            Player toPlayer = Mine3Repository.getPlayer(toAddress);

            if (fromPlayer == null || toPlayer == null) {
                if (sender instanceof Player) {
                    spigotRepository.sendMessage(sender, ChatColor.RED + "Player not found.");
                } else {
                    Logger.warning("Player not found.");
                }
                return true;
            }

            if (fromPlayer == toPlayer) {
                if (sender instanceof Player) {
                    spigotRepository.sendMessage(sender, ChatColor.RED + "You can't teleport to same player.");
                } else {
                    Logger.warning("You can't teleport to same player.");
                }
                return true;
            }

            fromPlayer.teleport(toPlayer);

            return true;
        }

        return false;
    }

}

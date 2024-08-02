package com.boomchanotai.mine3Standard.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.web3j.crypto.Keys;

import com.boomchanotai.mine3Lib.logger.Logger;
import com.boomchanotai.mine3Lib.repository.PlayerRepository;
import com.boomchanotai.mine3Standard.repository.SpigotRepository;

public class TeleportCommand implements CommandExecutor {
    SpigotRepository spigotRepository;

    public TeleportCommand(SpigotRepository spigotRepository) {
        this.spigotRepository = spigotRepository;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        // tp <address> - Teleport to the player
        if (args.length == 1 && sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("mine3.tp")) {
                spigotRepository.sendMessage(sender, ChatColor.RED + "You don't have permission to use this command.");
                return true;
            }

            String toAddress = Keys.toChecksumAddress(args[0]);

            // Teleport to the player
            Player toPlayer = PlayerRepository.getPlayer(toAddress);
            if (toPlayer == null) {
                spigotRepository.sendMessage(player, ChatColor.RED + "Player not found.");
                return true;
            }

            if (player == toPlayer) {
                spigotRepository.sendMessage(player, ChatColor.RED + "You can't teleport to yourself.");
                return true;
            }

            player.teleport(toPlayer);

            return true;
        }

        // tp <address> <address> - Teleport from the player to the player
        if (args.length == 2) {
            if (sender instanceof Player && !sender.hasPermission("mine3.tp.others")) {
                spigotRepository.sendMessage(sender, ChatColor.RED + "You don't have permission to use this command.");
                return true;
            }

            String fromAddress = Keys.toChecksumAddress(args[0]);
            String toAddress = Keys.toChecksumAddress(args[1]);

            // Teleport from the player to the player
            Player fromPlayer = PlayerRepository.getPlayer(fromAddress);
            Player toPlayer = PlayerRepository.getPlayer(toAddress);

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

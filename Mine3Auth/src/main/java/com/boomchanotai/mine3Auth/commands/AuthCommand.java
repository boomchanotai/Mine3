package com.boomchanotai.mine3Auth.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Auth.config.Config;
import com.boomchanotai.mine3Auth.logger.Logger;
import com.boomchanotai.mine3Auth.services.SpawnService;
import com.boomchanotai.mine3Lib.repository.PlayerRepository;

import net.md_5.bungee.api.ChatColor;

public class AuthCommand implements CommandExecutor {
    private SpawnService spawnService;

    public AuthCommand(SpawnService spawnService) {
        this.spawnService = spawnService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args.length > 1) {
            return false;
        }

        if (args[0].equals("setspawn")) {
            if (!(sender instanceof Player)) {
                Logger.info("This command can only be run by a player.");
                return false;
            }

            Player player = (Player) sender;
            if (!player.hasPermission("mine3.auth.setspawn")) {
                PlayerRepository.sendMessage(player, ChatColor.RED + "You don't have permission to use this command.");
                return true;
            }

            spawnService.setSpawnLocation(player.getLocation());
            PlayerRepository.sendMessage(player, "Spawn location has been set.");

            return true;
        }

        if (args[0].equals("spawn")) {
            if (!(sender instanceof Player)) {
                Logger.info("This command can only be run by a player.");
                return false;
            }

            Player player = (Player) sender;

            Location spawnLocation = spawnService.getSpawnLocation();
            player.teleport(spawnLocation);

            PlayerRepository.sendMessage(player, "Teleported to spawn location.");

            return true;
        }

        if (args[0].equals("reload")) {
            if (!sender.hasPermission("mine3.auth.reload")) {
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

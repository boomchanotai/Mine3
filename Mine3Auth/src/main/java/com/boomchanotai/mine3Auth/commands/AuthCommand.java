package com.boomchanotai.mine3Auth.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Auth.service.SpawnService;
import com.boomchanotai.mine3Lib.repository.PlayerRepository;

import net.md_5.bungee.api.ChatColor;

public class AuthCommand implements CommandExecutor {
    private SpawnService spawnService;

    public AuthCommand(SpawnService spawnService) {
        this.spawnService = spawnService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        if (args.length == 0) {
            return false;
        }

        if (args.length > 1) {
            return false;
        }

        if (args[0].equals("setspawn")) {
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
            Player player = (Player) sender;

            Location spawnLocation = spawnService.getSpawnLocation();
            player.teleport(spawnLocation);

            PlayerRepository.sendMessage(player, "Teleported to spawn location.");

            return true;
        }

        return false;
    }

}

package com.boomchanotai.mine3.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3.config.SpawnConfig;
import com.boomchanotai.mine3.repository.SpigotRepository;

import net.md_5.bungee.api.ChatColor;

public class Mine3Command implements CommandExecutor {
    private SpigotRepository spigotRepo;

    public Mine3Command(SpigotRepository spigotRepo) {
        this.spigotRepo = spigotRepo;
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
            if (!player.hasPermission("mine3.setspawn")) {
                spigotRepo.sendMessage(player, ChatColor.RED + "You don't have permission to use this command.");
                return true;
            }

            SpawnConfig.setSpawnLocation(player.getLocation());
            spigotRepo.sendMessage(player, "Spawn location has been set.");

            return true;
        }

        if (args[0].equals("spawn")) {
            Player player = (Player) sender;

            Location spawnLocation = SpawnConfig.getSpawnLocation();
            player.teleport(spawnLocation);

            spigotRepo.sendMessage(player, "Teleported to spawn location.");

            return true;
        }

        return false;
    }

}

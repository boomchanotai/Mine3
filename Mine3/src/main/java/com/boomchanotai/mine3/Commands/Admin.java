package com.boomchanotai.mine3.Commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3.Mine3;
import com.boomchanotai.mine3.Config.SpawnConfig;
import com.boomchanotai.mine3.Repository.SpigotRepository;

import net.md_5.bungee.api.ChatColor;

public class Admin implements CommandExecutor {
    private SpigotRepository spigotRepo;

    public Admin(SpigotRepository spigotRepo) {
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

            FileConfiguration spawnConfig = SpawnConfig.getSpawnConfig();
            spawnConfig.set("spawn.x", player.getLocation().getX());
            spawnConfig.set("spawn.y", player.getLocation().getY());
            spawnConfig.set("spawn.z", player.getLocation().getZ());
            spawnConfig.set("spawn.yaw", player.getLocation().getYaw());
            spawnConfig.set("spawn.pitch", player.getLocation().getPitch());
            spawnConfig.set("spawn.world", player.getLocation().getWorld().getName());
            SpawnConfig.saveSpawnConfig();

            System.out.println(player.getLocation());

            spigotRepo.sendMessage(player, "Spawn location has been set.");

            return true;
        }

        if (args[0].equals("spawn")) {
            Player player = (Player) sender;

            World world = Mine3.getInstance().getServer()
                    .getWorld(SpawnConfig.getSpawnConfig().getString("spawn.world"));
            Location spawnLocation = new Location(
                    world,
                    SpawnConfig.getSpawnConfig().getDouble("spawn.x"),
                    SpawnConfig.getSpawnConfig().getDouble("spawn.y"),
                    SpawnConfig.getSpawnConfig().getDouble("spawn.z"),
                    (float) SpawnConfig.getSpawnConfig().getDouble("spawn.yaw"),
                    (float) SpawnConfig.getSpawnConfig().getDouble("spawn.pitch"));
            player.teleport(spawnLocation);

            spigotRepo.sendMessage(player, "Teleported to spawn location.");

            return true;

        }

        return false;
    }

}

package com.boomchanotai.mine3.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

            System.out.println(player.getLocation());
            return true;
        }

        return false;
    }

}

package com.boomchanotai.mine3Standard.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.web3j.crypto.Keys;

import com.boomchanotai.mine3Lib.repository.Mine3Repository;
import com.boomchanotai.mine3Standard.repository.SpigotRepository;

public class GiveCommand implements CommandExecutor {
    SpigotRepository spigotRepository;

    public GiveCommand(SpigotRepository spigotRepository) {
        this.spigotRepository = spigotRepository;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            return false;
        }

        if (sender instanceof Player && !sender.hasPermission("mine3.give")) {
            spigotRepository.sendMessage(sender, "You don't have permission to use this command.");
            return true;
        }

        // give <address> <item> <amount> - Give the player an amount of money
        String targetAddress = Keys.toChecksumAddress(args[0]);
        String item = args[1];
        int amount = Integer.parseInt(args[2]);

        Player targetPlayer = Mine3Repository.getPlayer(targetAddress);

        Material material = Material.getMaterial(item);
        ItemStack itemStack = new ItemStack(material, amount);
        targetPlayer.getInventory().addItem(itemStack);

        return true;
    }

}

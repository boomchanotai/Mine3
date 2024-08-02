package com.boomchanotai.mine3Standard.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.web3j.crypto.Keys;

import com.boomchanotai.mine3Lib.logger.Logger;
import com.boomchanotai.mine3Lib.repository.PlayerRepository;

public class GiveCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            return false;
        }

        if (sender instanceof Player && !sender.hasPermission("mine3.give")) {
            PlayerRepository.sendMessage(sender, "You don't have permission to use this command.");
            return true;
        }

        // give <address> <item> <amount> - Give the player an amount of money
        String targetAddress = Keys.toChecksumAddress(args[0]);
        String item = args[1];
        int amount = Integer.parseInt(args[2]);

        Player targetPlayer = PlayerRepository.getPlayer(targetAddress);
        Material material = Material.getMaterial(item.toUpperCase());

        if (material == null) {
            if (sender instanceof Player) {
                PlayerRepository.sendMessage(sender, "Invalid item.");
            } else {
                Logger.warning("Invalid item.");
            }

            return true;
        }

        ItemStack itemStack = new ItemStack(material, amount);
        targetPlayer.getInventory().addItem(itemStack);

        return true;
    }

}

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
import com.boomchanotai.mine3Standard.utils.Utils;

public class GiveCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3 || args.length > 3) {
            return false;
        }

        if (!Utils.hasPermission(sender, "mine3.give")) {
            return true;
        }

        // give <address> <item> <amount> - Give the player an amount of money
        String targetAddress = Keys.toChecksumAddress(args[0]);
        String item = args[1];
        int amount = Integer.parseInt(args[2]);

        Player targetPlayer = PlayerRepository.getPlayer(targetAddress);
        if (targetPlayer == null) {
            if (sender instanceof Player) {
                PlayerRepository.sendMessage((Player) sender, "Address not found.");
            } else {
                Logger.warning("Address not found.");
            }

            return true;
        }

        Material material = Material.getMaterial(item.toUpperCase());
        if (material == null) {
            if (sender instanceof Player) {
                PlayerRepository.sendMessage((Player) sender, "Invalid item.");
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

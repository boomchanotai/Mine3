package com.boomchanotai.mine3Standard.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.boomchanotai.mine3Lib.address.Address;
import com.boomchanotai.mine3Lib.repositories.PlayerRepository;
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

        // give <address> <item> <amount> - (Player, Console)
        Address address = new Address(args[0]);
        String item = args[1];
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            Utils.sendCommandReturnMessage(sender, "Invalid amount.");
            return true;
        }

        Player targetPlayer = PlayerRepository.getPlayer(address);
        if (targetPlayer == null) {
            Utils.sendCommandReturnMessage(sender, "Address not found.");
            return true;
        }

        Material material = Material.getMaterial(item.toUpperCase());
        if (material == null) {
            Utils.sendCommandReturnMessage(sender, "Invalid item.");
            return true;
        }

        ItemStack itemStack = new ItemStack(material, amount);
        targetPlayer.getInventory().addItem(itemStack);
        PlayerRepository.sendMessage(targetPlayer, "You have been given " + amount + " " + item.toLowerCase() + ".");

        return true;
    }

}

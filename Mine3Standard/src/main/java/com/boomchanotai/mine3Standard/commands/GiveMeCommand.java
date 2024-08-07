package com.boomchanotai.mine3Standard.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.boomchanotai.mine3Lib.repositories.PlayerRepository;
import com.boomchanotai.mine3Standard.utils.Utils;

public class GiveMeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            return false;
        }

        // i <item> <amount> - (Player)
        if (!Utils.hasPermission(sender, "mine3.give")) {
            return true;
        }

        if (!Utils.isPlayerUsingCommand(sender)) {
            return true;
        }

        String item = args[0];
        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            Utils.sendCommandReturnMessage(sender, "Invalid amount.");
            return true;
        }

        Player player = (Player) sender;

        Material material = Material.getMaterial(item.toUpperCase());
        if (material == null) {
            Utils.sendCommandReturnMessage(sender, "Invalid item.");
            return true;
        }

        ItemStack itemStack = new ItemStack(material, amount);
        player.getInventory().addItem(itemStack);
        PlayerRepository.sendMessage(player, "You have been given " + amount + " " + item.toLowerCase() + ".");

        return true;
    }

}

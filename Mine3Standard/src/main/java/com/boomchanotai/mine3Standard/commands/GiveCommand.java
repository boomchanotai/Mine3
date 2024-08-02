package com.boomchanotai.mine3Standard.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.web3j.crypto.Keys;

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

        // give <address> <item> <amount> - (Player, Console)
        String targetAddress = Keys.toChecksumAddress(args[0]);
        String item = args[1];
        int amount = Integer.parseInt(args[2]);

        Player targetPlayer = PlayerRepository.getPlayer(targetAddress);
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

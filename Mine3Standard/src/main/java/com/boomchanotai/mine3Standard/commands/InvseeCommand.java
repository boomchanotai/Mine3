package com.boomchanotai.mine3Standard.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.web3j.crypto.Keys;

import com.boomchanotai.mine3Standard.logger.Logger;
import com.boomchanotai.mine3Lib.repository.PlayerRepository;
import com.boomchanotai.mine3Lib.utils.AddressUtils;
import com.boomchanotai.mine3Standard.utils.Utils;

public class InvseeCommand implements CommandExecutor, Listener {
    private static ArrayList<Inventory> inventoryList = new ArrayList<Inventory>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args.length > 1) {
            return false;
        }

        // invsee <address> - (Player, Console)
        if (!Utils.hasPermission(sender, "mine3.invsee")) {
            return true;
        }

        String address = Keys.toChecksumAddress(args[0]);
        Player targetPlayer = PlayerRepository.getPlayer(args[0]);
        if (targetPlayer == null) {
            Utils.sendCommandReturnMessage(sender, "Address not found.");
            return true;
        }

        ItemStack[] items = targetPlayer.getInventory().getContents();
        String title = AddressUtils.getShortAddress(address) + "'s Inventory";

        if (sender instanceof Player) {
            Player player = (Player) sender;

            Inventory inventory = Bukkit.createInventory(null, 45, title);
            inventory.setContents(items);

            inventoryList.add(inventory);
            player.openInventory(inventory);
        } else {
            String message = title + ": ";
            for (ItemStack item : items) {
                if (item == null) {
                    continue;
                }

                message += item.getAmount() + "x" + item.getType().name() + ", ";
            }

            Logger.info(message);
        }

        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (inventoryList.contains(event.getInventory())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (inventoryList.contains(event.getInventory())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event) {
        if (inventoryList.contains(event.getInventory())) {
            inventoryList.remove(event.getInventory());
        }
    }
}

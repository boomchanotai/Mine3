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

import com.boomchanotai.mine3Standard.logger.Logger;
import com.boomchanotai.mine3Lib.address.Address;
import com.boomchanotai.mine3Lib.repository.PlayerRepository;
import com.boomchanotai.mine3Standard.utils.Utils;

public class EnderChestCommand implements CommandExecutor, Listener {
    private static ArrayList<Inventory> enderChestList = new ArrayList<Inventory>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args.length > 1) {
            return false;
        }

        // enderchest <address> - (Player, Console)
        if (!Utils.hasPermission(sender, "mine3.enderchest")) {
            return true;
        }

        Address address = new Address(args[0]);
        Player targetPlayer = PlayerRepository.getPlayer(address);
        if (targetPlayer == null) {
            Utils.sendCommandReturnMessage(sender, "Address not found.");
            return true;
        }

        ItemStack[] items = targetPlayer.getEnderChest().getContents();
        String title = address.getShortAddress() + "'s Enderchest";

        if (sender instanceof Player) {
            Player player = (Player) sender;

            Inventory enderChest = Bukkit.createInventory(null, 27, title);
            enderChest.setContents(items);

            enderChestList.add(enderChest);
            player.openInventory(enderChest);
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
        if (enderChestList.contains(event.getInventory())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (enderChestList.contains(event.getInventory())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event) {
        if (enderChestList.contains(event.getInventory())) {
            enderChestList.remove(event.getInventory());
        }
    }
}

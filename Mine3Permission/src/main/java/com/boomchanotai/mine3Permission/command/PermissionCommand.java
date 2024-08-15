package com.boomchanotai.mine3Permission.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Lib.core.entities.Address;
import com.boomchanotai.mine3Lib.repositories.PlayerRepository;
import com.boomchanotai.mine3Permission.config.Config;
import com.boomchanotai.core.logger.Logger;
import com.boomchanotai.mine3Permission.services.PermissionManager;
import com.boomchanotai.mine3Permission.utils.Utils;

public class PermissionCommand implements CommandExecutor {
    private PermissionManager permissionManager;

    public PermissionCommand(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        // permission reload
        if (args[0].equals("reload") && args.length == 1) {
            if (!sender.hasPermission("mine3.permission.reload")) {
                PlayerRepository.sendMessage((Player) sender,
                        ChatColor.RED + "You don't have permission to use this command.");
                return true;
            }

            Config.reloadConfig();
            permissionManager.reloadPermission();

            if (sender instanceof Player) {
                PlayerRepository.sendMessage((Player) sender, "Mine3Permission config has been reloaded.");
            } else {
                Logger.info("Mine3Permission config has been reloaded.");
            }

            return true;
        }

        // permission has <permission> - (Player)
        // permission has <permission> <address> - (Player, Console)
        if (args[0].equals("has")) {
            Player targetPlayer = null;

            if (!Utils.hasPermission(sender, "mine3.permission.has")) {
                return true;
            }

            if (args.length == 2) {
                // permission has <permission>

                if (!Utils.isPlayerUsingCommand(sender)) {
                    return true;
                }

                targetPlayer = (Player) sender;
            } else if (args.length == 3) {
                // permission has <permission> <address>

                Address address = new Address(args[2]);
                targetPlayer = PlayerRepository.getPlayer(address);
                if (targetPlayer == null) {
                    Utils.sendCommandReturnMessage(sender, ChatColor.RED + "Address not found.");
                    return true;
                }
            } else {
                return false;
            }

            String permission = args[1];
            if (targetPlayer.hasPermission(permission)) {
                Utils.sendCommandReturnMessage(sender, "Yes, player has permission.");
            } else {
                Utils.sendCommandReturnMessage(sender, "No, player doesn't have permission.");
            }

            return true;
        }

        // permission set-group <group> <address> - (Player, Console)
        if (args[0].equals("set-group") && args.length == 3) {
            if (!Utils.hasPermission(sender, "mine3.permission.set-group")) {
                return true;
            }

            String group = args[1];
            Address address = new Address(args[2]);
            permissionManager.attachPermissionGroup(address, group);

            Player player = PlayerRepository.getPlayer(address);
            PlayerRepository.sendMessage(player, "you have been changed to " + group + " group.");

            Utils.sendCommandReturnMessage(sender, address.getShortAddress() + " has been set to " + group + " group.");

            return true;
        }

        return false;
    }

}

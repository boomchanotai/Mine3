package com.boomchanotai.mine3Standard.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Lib.address.Address;
import com.boomchanotai.mine3Lib.repository.PlayerRepository;
import com.boomchanotai.mine3Standard.utils.Utils;

import net.md_5.bungee.api.ChatColor;

public class SpeedCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args.length > 2) {
            return false;
        }

        Player targetPlayer = null;
        int speed = 0;
        try {
            speed = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            Utils.sendCommandReturnMessage(sender, "Invalid speed.");
            return true;
        }

        if (speed < -10 || speed > 10) {
            Utils.sendCommandReturnMessage(sender, ChatColor.RED + "Speed must be between -10 and 10.");
            return true;
        }

        // speed <speed> - (Player)
        if (args.length == 1) {
            if (!Utils.isPlayerUsingCommand(sender)) {
                return true;
            }

            if (!Utils.hasPermission(sender, "mine3.speed")) {
                return true;
            }

            targetPlayer = (Player) sender;
        }

        // speed <speed> <address> - (Player, Console)
        if (args.length == 2) {
            if (!Utils.hasPermission(sender, "mine3.speed.others")) {
                return true;
            }

            Address address = new Address(args[1]);
            targetPlayer = PlayerRepository.getPlayer(address);
        }

        if (targetPlayer == null) {
            Utils.sendCommandReturnMessage(sender, "Address not found.");
            return true;
        }

        float speedValue = (float) speed / 10;
        if (targetPlayer.isFlying()) {
            targetPlayer.setFlySpeed(speedValue);
        } else {
            targetPlayer.setWalkSpeed(speedValue);
        }
        PlayerRepository.sendMessage(targetPlayer, "Your speed has been set to " + speed + ".");

        return true;
    }

}

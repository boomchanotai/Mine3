package com.boomchanotai.mine3Standard.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Lib.address.Address;
import com.boomchanotai.mine3Lib.repositories.PlayerRepository;
import com.boomchanotai.mine3Standard.Mine3Standard;
import com.boomchanotai.mine3Standard.utils.Utils;

public class KillCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args.length > 1) {
            return false;
        }

        // kill <address> - (Player, Console)
        if (!Utils.hasPermission(sender, "mine3.kill")) {
            return true;
        }

        if (args[0].toLowerCase().equals("all")) {
            for (Player p : Mine3Standard.getPlugin().getServer().getOnlinePlayers()) {
                p.setLastDamage(Integer.MAX_VALUE);
                p.setHealth(Double.MIN_VALUE);
                PlayerRepository.sendMessage(p, "You have been killed.");
            }

            return true;
        }

        Address address = new Address(args[0]);
        Player targetPlayer = PlayerRepository.getPlayer(address);
        if (targetPlayer == null) {
            Utils.sendCommandReturnMessage(sender, "Address not found.");
            return true;
        }

        targetPlayer.setLastDamage(Integer.MAX_VALUE);
        targetPlayer.setHealth(Double.MIN_VALUE);
        PlayerRepository.sendMessage(targetPlayer, "You have been killed.");

        return true;
    }

}

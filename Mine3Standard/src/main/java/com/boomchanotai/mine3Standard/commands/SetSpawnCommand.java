package com.boomchanotai.mine3Standard.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Lib.repositories.PlayerRepository;
import com.boomchanotai.mine3Standard.config.SpawnConfig;
import com.boomchanotai.mine3Standard.utils.Utils;

public class SetSpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Utils.hasPermission(sender, "mine3.setspawn")) {
            return true;
        }

        if (!Utils.isPlayerUsingCommand(sender)) {
            return true;
        }

        Player player = (Player) sender;
        SpawnConfig.setSpawnLocation(player.getLocation());
        player.getWorld().setSpawnLocation(player.getLocation());
        PlayerRepository.sendMessage(player, "Spawn location has been set.");

        return true;
    }

}

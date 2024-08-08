package com.boomchanotai.mine3Standard.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Lib.repositories.PlayerRepository;
import com.boomchanotai.mine3Standard.config.SpawnConfig;
import com.boomchanotai.mine3Standard.utils.Utils;

public class SpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Utils.hasPermission(sender, "mine3.spawn")) {
            return true;
        }

        if (!Utils.isPlayerUsingCommand(sender)) {
            return true;
        }

        Player player = (Player) sender;
        Location spawnLocation = SpawnConfig.getSpawnLocation();
        player.teleport(spawnLocation);
        PlayerRepository.sendMessage(player, "Teleported to spawn location.");

        return true;
    }

}

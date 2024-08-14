package com.boomchanotai.mine3Auth.commands;

import com.boomchanotai.mine3Auth.Mine3Auth;
import com.boomchanotai.core.logger.Logger;
import com.boomchanotai.mine3Lib.core.entities.Address;
import com.boomchanotai.mine3Lib.repositories.PlayerRepository;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AddressCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        if (args.length == 0 && !(sender instanceof Player)) {
            return false;
        }

        Player player = null;
        if (args.length == 0) {
            player = (Player) sender;
        }

        if (args.length == 1) {
            player = Mine3Auth.getPlugin().getServer().getPlayer(args[0]);
        }

        if (player == null) {
            return false;
        }

        Address address = PlayerRepository.getAddress(player.getUniqueId());
        if (address == null) {
            Logger.warning(
                    "Unexpected Event: Not found playerInfo!, UUID: " + player.getUniqueId()
                            + ", Command: /address");
            return false;
        }

        PlayerRepository.sendMessage(player, address.getValue());
        return true;
    }
}

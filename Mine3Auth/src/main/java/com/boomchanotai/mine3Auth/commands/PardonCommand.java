package com.boomchanotai.mine3Auth.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.web3j.crypto.Keys;

import com.boomchanotai.mine3Auth.logger.Logger;
import com.boomchanotai.mine3Auth.repository.PostgresRepository;
import com.boomchanotai.mine3Lib.repository.PlayerRepository;

import net.md_5.bungee.api.ChatColor;

public class PardonCommand implements CommandExecutor {
    private PostgresRepository pgRepo;

    public PardonCommand(PostgresRepository pgRepo) {
        this.pgRepo = pgRepo;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            return false;
        }

        // pardon <address> - (Player, Console)
        if (!sender.hasPermission("mine3.pardon")) {
            PlayerRepository.sendMessage((Player) sender,
                    ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        String address = Keys.toChecksumAddress(args[0]);
        if (pgRepo.getBannedPlayers().contains(address)) {
            pgRepo.setPlayerBanned(address, false);
        }

        if (sender instanceof Player) {
            PlayerRepository.sendMessage((Player) sender, address + " has been pardoned.");
        } else {
            Logger.info(address + " has been pardoned.");
        }

        return true;
    }

}
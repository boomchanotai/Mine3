package com.boomchanotai.mine3Standard.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Lib.logger.Logger;
import com.boomchanotai.mine3Lib.repository.Mine3Repository;
import com.boomchanotai.mine3Standard.repository.SpigotRepository;

public class GameModeCommand implements CommandExecutor {
    SpigotRepository spigotRepository;

    public GameModeCommand(SpigotRepository spigotRepository) {
        this.spigotRepository = spigotRepository;
    }

    private GameMode parsedGameMode(String gameMode, CommandSender sender) {
        switch (gameMode) {
            case "0":
                return GameMode.SURVIVAL;
            case "1":
                return GameMode.CREATIVE;
            case "2":
                return GameMode.ADVENTURE;
            case "3":
                return GameMode.SPECTATOR;
            default:
                try {
                    return GameMode.valueOf(gameMode.toUpperCase());
                } catch (Exception e) {
                    if (sender instanceof Player) {
                        spigotRepository.sendMessage(sender, "Invalid game mode.");
                    } else {
                        Logger.warning("Invalid game mode.");
                    }
                }
                return null;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        GameMode gameMode = parsedGameMode(args[0], sender);
        if (gameMode == null) {
            return true;
        }

        // gamemode <gamemode>
        if (args.length == 1 && sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("mine3.gamemode")) {
                spigotRepository.sendMessage(sender, "You don't have permission to use this command.");
                return true;
            }

            player.setGameMode(gameMode);

            return true;
        }

        // gamemode <gamemode> <address>
        if (args.length == 2) {
            if (sender instanceof Player && !sender.hasPermission("mine3.gamemode.others")) {
                spigotRepository.sendMessage(sender, "You don't have permission to use this command.");
                return true;
            }

            Player targetPlayer = Mine3Repository.getPlayer(args[1]);
            targetPlayer.setGameMode(gameMode);

            return true;
        }
        return true;
    }

}

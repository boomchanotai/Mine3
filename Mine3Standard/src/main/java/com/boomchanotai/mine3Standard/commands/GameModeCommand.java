package com.boomchanotai.mine3Standard.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Lib.repository.PlayerRepository;
import com.boomchanotai.mine3Standard.utils.Utils;

public class GameModeCommand implements CommandExecutor {
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
                    return null;
                }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        GameMode gameMode = parsedGameMode(args[0], sender);
        if (gameMode == null) {
            Utils.sendCommandReturnMessage(sender, "Invalid game mode.");
            return true;
        }

        // gamemode <gamemode>
        if (args.length == 1 && sender instanceof Player) {
            if (!Utils.hasPermission(sender, "mine3.gamemode")) {
                return true;
            }

            Player player = (Player) sender;
            player.setGameMode(gameMode);
            return true;
        }

        // gamemode <gamemode> <address>
        if (args.length == 2) {
            if (!Utils.hasPermission(sender, "mine3.gamemode.others")) {
                return true;
            }

            Player targetPlayer = PlayerRepository.getPlayer(args[1]);
            if (targetPlayer == null) {
                Utils.sendCommandReturnMessage(sender, "Address not found.");
                return true;
            }

            targetPlayer.setGameMode(gameMode);
            return true;
        }

        return false;
    }

}

package com.boomchanotai.mine3.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.boomchanotai.mine3.service.PlayerService;

public class LogoutCommand implements CommandExecutor {
    private PlayerService playerService;

    public LogoutCommand(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player p = (Player) sender;
        playerService.disconnectPlayer(p);
        playerService.connectPlayer(p);

        return true;
    }
}

package com.boomchanotai.mine3Standard.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Lib.repository.PlayerRepository;
import com.boomchanotai.mine3Standard.services.TpaService;
import com.boomchanotai.mine3Standard.utils.Utils;

public class TpaCancelCommand implements CommandExecutor {
    private TpaService tpaService;

    public TpaCancelCommand(TpaService tpaService) {
        this.tpaService = tpaService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 0) {
            return false;
        }

        // tpacancel - (Player)
        if (!Utils.isPlayerUsingCommand(sender)) {
            return true;
        }

        if (!Utils.hasPermission(sender, "mine3.tpacancel")) {
            return true;
        }

        Player player = (Player) sender;

        String playerAddress = PlayerRepository.getAddress(player.getUniqueId());
        if (playerAddress == null) {
            Utils.sendCommandReturnMessage(sender, "Can't parse your address. Please login again.");
            return true;
        }

        tpaService.cancelTpaRequest(playerAddress);

        return true;
    }

}

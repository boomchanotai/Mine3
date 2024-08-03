package com.boomchanotai.mine3Standard.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Lib.repository.PlayerRepository;
import com.boomchanotai.mine3Lib.utils.AddressUtils;
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

        Player toPlayer = (Player) sender;

        String toPlayerAddress = PlayerRepository.getAddress(toPlayer.getUniqueId());
        if (toPlayerAddress == null) {
            Utils.sendCommandReturnMessage(sender, "Can't parse your address. Please login again.");
            return true;
        }

        if (!tpaService.hasTpaRequest(toPlayerAddress)) {
            Utils.sendCommandReturnMessage(sender, "No tpa request found.");
            return true;
        }

        String fromPlayerAddress = tpaService.getTpaRequest(toPlayerAddress);
        if (fromPlayerAddress == null) {
            Utils.sendCommandReturnMessage(sender, "No tpa request found.");
            return true;
        }

        Player fromPlayer = PlayerRepository.getPlayer(fromPlayerAddress);
        if (fromPlayer == null) {
            Utils.sendCommandReturnMessage(sender, "Player not found.");
            return true;
        }

        tpaService.removeTpaRequest(toPlayerAddress);
        PlayerRepository.sendMessage(fromPlayer, "Tpa request to " + AddressUtils.addressShortener(toPlayerAddress)
                + " has been canceled.");
        PlayerRepository.sendMessage(toPlayer,
                "Tpa request from " + AddressUtils.addressShortener(fromPlayerAddress) + " has been canceled.");

        return true;
    }

}

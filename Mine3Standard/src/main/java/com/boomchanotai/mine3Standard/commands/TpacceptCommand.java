package com.boomchanotai.mine3Standard.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Lib.repository.PlayerRepository;
import com.boomchanotai.mine3Lib.utils.AddressUtils;
import com.boomchanotai.mine3Standard.services.TpaService;
import com.boomchanotai.mine3Standard.utils.Utils;

public class TpacceptCommand implements CommandExecutor {
    private TpaService tpaService;

    public TpacceptCommand(TpaService tpaService) {
        this.tpaService = tpaService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 0) {
            return false;
        }

        // tpaccept - (Player)
        if (!Utils.isPlayerUsingCommand(sender)) {
            return true;
        }

        if (!Utils.hasPermission(sender, "mine3.tpaccept")) {
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

        fromPlayer.teleport(toPlayer.getLocation());
        PlayerRepository.sendMessage(fromPlayer,
                "Teleporting to " + AddressUtils.addressShortener(toPlayerAddress) + "...");
        PlayerRepository.sendMessage(toPlayer,
                "Teleporting " + AddressUtils.addressShortener(fromPlayerAddress) + " to you...");

        tpaService.removeTpaRequest(toPlayerAddress);

        return true;
    }

}

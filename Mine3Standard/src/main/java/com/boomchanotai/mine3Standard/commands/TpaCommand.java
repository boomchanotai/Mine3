package com.boomchanotai.mine3Standard.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.web3j.crypto.Keys;

import com.boomchanotai.mine3Lib.repository.PlayerRepository;
import com.boomchanotai.mine3Lib.utils.AddressUtils;
import com.boomchanotai.mine3Standard.services.TpaService;
import com.boomchanotai.mine3Standard.utils.Utils;

public class TpaCommand implements CommandExecutor {
    private TpaService tpaService;

    public TpaCommand(TpaService tpaService) {
        this.tpaService = tpaService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args.length > 1) {
            return false;
        }

        String fromPlayerAddress = null;
        String toPlayerAddress = null;

        Player fromPlayer = null;
        Player toPlayer = null;

        // tpa <address> - (Player)
        if (args.length == 1) {
            if (!Utils.isPlayerUsingCommand(sender)) {
                return true;
            }

            if (!Utils.hasPermission(sender, "mine3.tpa")) {
                return true;
            }

            String toAddress = Keys.toChecksumAddress(args[0]);

            fromPlayer = (Player) sender;
            toPlayer = PlayerRepository.getPlayer(toAddress);

            fromPlayerAddress = PlayerRepository.getAddress(fromPlayer.getUniqueId());
            toPlayerAddress = toAddress;

        }

        if (fromPlayerAddress == null || toPlayerAddress == null) {
            Utils.sendCommandReturnMessage(sender, "Address not found.");
            return true;
        }

        if (fromPlayer == null || toPlayer == null) {
            Utils.sendCommandReturnMessage(sender, "Player not found.");
            return true;
        }

        if (fromPlayerAddress.equals(toPlayerAddress)) {
            Utils.sendCommandReturnMessage(sender, "Cannot teleport to yourself.");
            return true;
        }

        if (tpaService.hasTpaRequest(toPlayerAddress)
                && tpaService.getTpaRequest(toPlayerAddress).equals(fromPlayerAddress)) {
            Utils.sendCommandReturnMessage(sender, "Tpa request already sent.");
            return true;
        }

        tpaService.addTpaRequest(fromPlayerAddress, toPlayerAddress);

        PlayerRepository.sendMessage(fromPlayer,
                "Tpa request sent to " + AddressUtils.addressShortener(toPlayerAddress) + ".");
        PlayerRepository.sendMessage(toPlayer,
                AddressUtils.addressShortener(fromPlayerAddress)
                        + " wants to teleport to you. Use /tpaccept to accept. Use /tpacancel to deny.");

        return true;
    }

}

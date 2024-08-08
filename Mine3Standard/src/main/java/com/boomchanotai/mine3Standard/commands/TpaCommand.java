package com.boomchanotai.mine3Standard.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boomchanotai.mine3Lib.address.Address;
import com.boomchanotai.mine3Lib.repositories.PlayerRepository;
import com.boomchanotai.mine3Standard.services.TpaService;
import com.boomchanotai.mine3Standard.utils.Utils;

import net.md_5.bungee.api.ChatColor;

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

        // tpa <address> - (Player)
        if (!Utils.isPlayerUsingCommand(sender)) {
            return true;
        }

        if (!Utils.hasPermission(sender, "mine3.tpa")) {
            return true;
        }

        Address toPlayerAddress = new Address(args[0]);

        Player toPlayer = PlayerRepository.getPlayer(toPlayerAddress);
        if (toPlayer == null) {
            Utils.sendCommandReturnMessage(sender, "Address not found.");
            return true;
        }

        Player fromPlayer = (Player) sender;

        Address fromPlayerAddress = PlayerRepository.getAddress(fromPlayer.getUniqueId());
        if (fromPlayerAddress == null) {
            Utils.sendCommandReturnMessage(sender, "Can't parse your address. Please login again.");
            return true;
        }

        if (fromPlayerAddress.equals(toPlayerAddress)) {
            Utils.sendCommandReturnMessage(sender, ChatColor.RED + "Cannot teleport to yourself.");
            return true;
        }

        if (tpaService.hasTpaRequest(toPlayerAddress, "TPA")
                && tpaService.getTpaRequest(toPlayerAddress).getFirst().equals(fromPlayerAddress)) {
            Utils.sendCommandReturnMessage(sender, "Tpa request already sent.");
            return true;
        }

        tpaService.addTpaRequest(fromPlayerAddress, toPlayerAddress);

        PlayerRepository.sendMessage(fromPlayer,
                "Tpa request sent to " + toPlayerAddress.getShortAddress() + ".");
        PlayerRepository.sendMessage(toPlayer,
                fromPlayerAddress.getShortAddress()
                        + " wants to teleport to you. Use /tpaccept to accept. Use /tpacancel to deny.");

        return true;
    }

}

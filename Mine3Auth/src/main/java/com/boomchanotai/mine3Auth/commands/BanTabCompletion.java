package com.boomchanotai.mine3Auth.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import com.boomchanotai.mine3Lib.address.Address;

import com.boomchanotai.mine3Lib.repository.PlayerRepository;

public class BanTabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> subCommand = new ArrayList<>();

        if (args.length == 1) {
            ArrayList<Address> address = PlayerRepository.getAllAddress();
            for (Address a : address) {
                if (a.toString().toLowerCase().contains(args[0].toLowerCase())) {
                    subCommand.add(a.toString());
                }
            }
        }

        return subCommand;
    }

}

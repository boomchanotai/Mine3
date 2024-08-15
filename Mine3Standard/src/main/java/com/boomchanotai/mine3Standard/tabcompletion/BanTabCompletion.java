package com.boomchanotai.mine3Standard.tabcompletion;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import com.boomchanotai.mine3Lib.core.entities.Address;

import com.boomchanotai.mine3Lib.repositories.PlayerRepository;

public class BanTabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> subCommand = new ArrayList<>();

        if (args.length == 1) {
            Set<Address> address = PlayerRepository.getOnlinePlayers();
            for (Address a : address) {
                if (a.toString().toLowerCase().contains(args[0].toLowerCase())) {
                    subCommand.add(a.toString());
                }
            }
        }

        return subCommand;
    }

}

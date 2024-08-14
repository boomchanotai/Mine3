package com.boomchanotai.mine3Standard.tabcompletion;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.boomchanotai.mine3Lib.core.entities.Address;
import com.boomchanotai.mine3Lib.repositories.PlayerRepository;

public class GodTabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> subCommand = new ArrayList<>();

        if (args.length == 1) {
            ArrayList<Address> address = PlayerRepository.getOnlinePlayers();
            for (Address a : address) {
                if (a.getValue().toLowerCase().contains(args[0].toLowerCase())) {
                    subCommand.add(a.getValue());
                }
            }
        }

        return subCommand;
    }

}

package com.boomchanotai.mine3Standard.tabcompletion;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.boomchanotai.mine3Lib.core.entities.Address;
import com.boomchanotai.mine3Lib.repositories.PlayerRepository;

public class SpeedTabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> subCommand = new ArrayList<>();

        if (args.length == 1) {
            for (int i = -10; i <= 10; i++) {
                subCommand.add(String.valueOf(i));
            }
        }

        if (args.length == 2) {
            Set<Address> address = PlayerRepository.getOnlinePlayers();
            for (Address a : address) {
                if (a.getValue().toLowerCase().contains(args[1].toLowerCase())) {
                    subCommand.add(a.getValue());
                }
            }
        }

        return subCommand;
    }

}

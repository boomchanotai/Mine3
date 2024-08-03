package com.boomchanotai.mine3Standard.tabcompletion;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.boomchanotai.mine3Lib.repository.PlayerRepository;

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
            String[] address = PlayerRepository.getAllAddress();
            for (String a : address) {
                if (a.toLowerCase().contains(args[1].toLowerCase())) {
                    subCommand.add(a);
                }
            }
        }

        return subCommand;
    }

}

package com.boomchanotai.mine3Standard.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.boomchanotai.mine3Lib.repository.Mine3Repository;

public class GiveTabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> subCommand = new ArrayList<>();

        if (args.length == 1) {
            String[] address = Mine3Repository.getAllAddress();
            for (String a : address) {
                if (a.toLowerCase().contains(args[0].toLowerCase())) {
                    subCommand.add(a);
                }
            }
        }

        if (args.length == 2) {
            Material[] materials = Material.values();
            for (Material m : materials) {
                if (m.name().toLowerCase().contains(args[1].toLowerCase())) {
                    subCommand.add(m.name());
                }
            }
        }

        if (args.length == 3) {
            for (int i = 1; i <= 64; i++) {
                subCommand.add(String.valueOf(i));
            }
        }

        return subCommand;
    }

}

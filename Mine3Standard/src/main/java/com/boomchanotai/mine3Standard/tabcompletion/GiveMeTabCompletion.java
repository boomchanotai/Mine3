package com.boomchanotai.mine3Standard.tabcompletion;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class GiveMeTabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> subCommand = new ArrayList<>();

        if (args.length == 1) {
            Material[] materials = Material.values();
            for (Material m : materials) {
                if (m.name().toLowerCase().contains(args[0].toLowerCase())) {
                    subCommand.add(m.name());
                }
            }
        }

        if (args.length == 2) {
            for (int i = 1; i <= 64; i++) {
                subCommand.add(String.valueOf(i));
            }
        }

        return subCommand;
    }

}

package com.boomchanotai.mine3Standard.tabcompletion;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class StandardTabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> subCommand = new ArrayList<>();
        if (args.length == 1) {
            ArrayList<String> subs = new ArrayList<>();
            if (sender.hasPermission("mine3.standard.reload")) {
                subs.add("reload");
            }

            for (String sub : subs) {
                if (sub.toLowerCase().contains(args[0].toLowerCase())) {
                    subCommand.add(sub);
                }
            }
        }

        return subCommand;
    }

}

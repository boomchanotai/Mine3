package com.boomchanotai.mine3Auth.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class AuthTabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> subCommand = new ArrayList<>();
        if (args.length == 1) {

            subCommand.add("spawn");
            ArrayList<String> subs = new ArrayList<>();
            if (sender.hasPermission("mine3.auth.setspawn")) {
                subs.add("setspawn");
            }
            if (sender.hasPermission("mine3.auth.reload")) {
                subs.add("reload");
            }
            subs.add("spawn");

            for (String sub : subs) {
                if (sub.toLowerCase().contains(args[0].toLowerCase())) {
                    subCommand.add(sub);
                }
            }
        }

        return subCommand;
    }

}

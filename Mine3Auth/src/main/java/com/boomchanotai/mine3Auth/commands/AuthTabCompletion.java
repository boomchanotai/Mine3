package com.boomchanotai.mine3Auth.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class AuthTabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }

        List<String> subCommand = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("mine3Auth.setspawn")) {
                subCommand.add("setspawn");
                subCommand.add("reload");
            }

            subCommand.add("spawn");
        }

        return subCommand;
    }

}

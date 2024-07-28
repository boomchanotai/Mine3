package com.boomchanotai.mine3.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class AdminTabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }

        List<String> subCommand = new ArrayList<>();
        if (args.length == 1 && sender.hasPermission("mine3.setspawn")) {
            subCommand.add("setspawn");
        }

        return subCommand;
    }

}

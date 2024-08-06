package com.boomchanotai.mine3Permission.tabcompletion;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class PermissionTabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> subCommand = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("mine3.permission.reload")) {
                subCommand.add("reload");
            }
        }

        return subCommand;
    }

}

package com.boomchanotai.mine3Permission.tabcompletion;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.boomchanotai.mine3Lib.core.entities.Address;
import com.boomchanotai.mine3Lib.repositories.PlayerRepository;
import com.boomchanotai.mine3Permission.services.PermissionManager;

public class PermissionTabCompletion implements TabCompleter {
    private PermissionManager permissionManager;

    public PermissionTabCompletion(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> subCommand = new ArrayList<>();
        if (args.length == 1) {
            ArrayList<String> subs = new ArrayList<>();
            if (sender.hasPermission("mine3.permission.reload")) {
                subs.add("reload");
            }
            if (sender.hasPermission("mine3.permission.has")) {
                subs.add("has");
            }
            if (sender.hasPermission("mine3.permission.set-group")) {
                subs.add("set-group");
            }

            for (String sub : subs) {
                if (sub.toLowerCase().contains(args[0].toLowerCase())) {
                    subCommand.add(sub);
                }
            }
        }

        if (args.length == 2) {
            if (args[0].equals("set-group")) {
                Set<String> groups = permissionManager.getGroups();
                for (String group : groups) {
                    if (group.toLowerCase().contains(args[1].toLowerCase())) {
                        subCommand.add(group);
                    }
                }
            }
        }

        if (args.length == 3) {
            if (args[0].equals("has") || args[0].equals("set-group")) {
                Set<Address> address = PlayerRepository.getOnlinePlayers();
                for (Address a : address) {
                    if (a.getValue().toLowerCase().contains(args[2].toLowerCase())) {
                        subCommand.add(a.getValue());
                    }
                }
            }
        }

        return subCommand;
    }

}

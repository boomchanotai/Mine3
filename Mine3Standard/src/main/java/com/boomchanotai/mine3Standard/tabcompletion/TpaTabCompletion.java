package com.boomchanotai.mine3Standard.tabcompletion;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.boomchanotai.mine3Lib.repository.PlayerRepository;

public class TpaTabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> addressList = new ArrayList<>();

        if (args.length == 1) {
            String[] address = PlayerRepository.getAllAddress();
            for (String a : address) {
                if (a.toLowerCase().contains(args[0].toLowerCase())) {
                    addressList.add(a);
                }
            }
        }

        return addressList;
    }

}

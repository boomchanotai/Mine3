package com.boomchanotai.mine3Standard.tabcompletion;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.boomchanotai.mine3Standard.repositories.PostgresRepository;
import com.boomchanotai.mine3Lib.address.Address;

public class PardonTabCompletion implements TabCompleter {
    private PostgresRepository pgRepo;

    public PardonTabCompletion(PostgresRepository pgRepo) {
        this.pgRepo = pgRepo;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> subCommand = new ArrayList<>();

        if (args.length == 1) {
            ArrayList<Address> address = pgRepo.getBannedPlayers();
            for (Address a : address) {
                if (a.getValue().toLowerCase().contains(args[0].toLowerCase())) {
                    subCommand.add(a.getValue());
                }
            }
        }

        return subCommand;
    }

}

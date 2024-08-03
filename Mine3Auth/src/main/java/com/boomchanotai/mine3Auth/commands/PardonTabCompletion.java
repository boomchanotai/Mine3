package com.boomchanotai.mine3Auth.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.boomchanotai.mine3Auth.repository.PostgresRepository;

public class PardonTabCompletion implements TabCompleter {
    private PostgresRepository pgRepo;

    public PardonTabCompletion(PostgresRepository pgRepo) {
        this.pgRepo = pgRepo;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> subCommand = new ArrayList<>();

        if (args.length == 1) {
            ArrayList<String> address = pgRepo.getBannedPlayers();
            for (String a : address) {
                if (a.toLowerCase().contains(args[0].toLowerCase())) {
                    subCommand.add(a);
                }
            }
        }

        return subCommand;
    }

}

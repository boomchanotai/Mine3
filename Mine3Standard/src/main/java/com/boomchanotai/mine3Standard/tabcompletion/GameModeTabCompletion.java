package com.boomchanotai.mine3Standard.tabcompletion;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.boomchanotai.mine3Lib.core.entities.Address;
import com.boomchanotai.mine3Lib.repositories.PlayerRepository;

public class GameModeTabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> subCommand = new ArrayList<>();

        if (args.length == 1) {
            subCommand.add("0"); // Survival
            subCommand.add("1"); // Creative
            subCommand.add("2"); // Adventure
            subCommand.add("3"); // Spectator

            GameMode[] gameModeList = GameMode.values();
            for (GameMode gm : gameModeList) {
                if (gm.name().toLowerCase().contains(args[0].toLowerCase())) {
                    subCommand.add(gm.name());
                }
            }
        }

        if (args.length == 2) {
            Set<Address> address = PlayerRepository.getOnlinePlayers();
            for (Address a : address) {
                if (a.getValue().toLowerCase().contains(args[1].toLowerCase())) {
                    subCommand.add(a.getValue());
                }
            }
        }

        return subCommand;
    }

}

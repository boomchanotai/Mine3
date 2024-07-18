package com.boomchanotai.mine3.Commands;

import com.boomchanotai.mine3.Repository.PlayerRepository;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.boomchanotai.mine3.Config.Config.COLOR_CODE_PREFIX;
import static com.boomchanotai.mine3.Config.Config.TITLE;

public class Address implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player p = (Player) sender;
        p.sendMessage(ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, TITLE + "Hello World"));

        return true;
    }
}

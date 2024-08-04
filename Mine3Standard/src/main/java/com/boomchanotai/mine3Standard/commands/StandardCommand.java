package com.boomchanotai.mine3Standard.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.boomchanotai.mine3Standard.config.Config;
import com.boomchanotai.mine3Standard.utils.Utils;

public class StandardCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args.length > 1) {
            return false;
        }

        if (args[0].equals("reload")) {
            if (!Utils.hasPermission(sender, "mine3.standard.reload")) {
                return true;
            }

            Config.reloadConfig();

            Utils.sendCommandReturnMessage(sender, "Mine3Standard config has been reloaded.");
            return true;
        }

        return false;
    }

}

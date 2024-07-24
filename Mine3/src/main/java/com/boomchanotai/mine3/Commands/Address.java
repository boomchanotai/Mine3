package com.boomchanotai.mine3.Commands;

import com.boomchanotai.mine3.Entity.PlayerCacheData;
import com.boomchanotai.mine3.Logger.Logger;
import com.boomchanotai.mine3.Service.PlayerService;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.boomchanotai.mine3.Config.Config.COLOR_CODE_PREFIX;
import static com.boomchanotai.mine3.Config.Config.TITLE;

public class Address implements CommandExecutor {
    private PlayerService playerService;

    public Address(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player p = (Player) sender;
        PlayerCacheData playerCacheData = playerService.getPlayer(p.getUniqueId());
        if (playerCacheData == null) {
            Logger.warning("Unexpected Event: Not found playerInfo!, UUID: " + p.getUniqueId() + ", Command: /address");
            return false;
        }

        String address = playerCacheData.getAddress();
        p.sendMessage(ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, TITLE + address));

        return true;
    }
}

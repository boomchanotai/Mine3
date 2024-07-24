package com.boomchanotai.mine3.Commands;

import com.boomchanotai.mine3.Logger.Logger;
import com.boomchanotai.mine3.Service.PlayerService;
import com.fasterxml.jackson.databind.JsonNode;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static com.boomchanotai.mine3.Config.Config.COLOR_CODE_PREFIX;
import static com.boomchanotai.mine3.Config.Config.TITLE;

public class Address implements CommandExecutor {
    private PlayerService playerService;

    public Address(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player p = (Player) sender;
        JsonNode playerInfo = playerService.getPlayer(p.getUniqueId());
        if (playerInfo == null) {
            Logger.warning("Unexpected Event: Not found playerInfo!, UUID: " + p.getUniqueId() + ", Command: /address");
            return false;
        }

        String address = playerInfo.get("address").asText();
        p.sendMessage(ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, TITLE + address));

        System.out.println(p.getLevel()); // int
        System.out.println(p.getExp()); // float
        System.out.println(p.getHealth()); // double 0 - 20
        System.out.println(p.getFoodLevel()); // int 0 - 20
        System.out.println(p.getLocation().getX()); // double
        System.out.println(p.getLocation().getY()); // double
        System.out.println(p.getLocation().getZ()); // double
        System.out.println(p.getLocation().getPitch()); // float
        System.out.println(p.getLocation().getYaw()); // float
        System.out.println(p.getLocation().getPitch()); // double
        System.out.println(p.getLocation().getWorld()); // string
        System.out.println(Arrays.toString(p.getInventory().getContents()));


        return true;
    }
}

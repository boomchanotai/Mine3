package com.boomchanotai.mine3.Commands;

import com.boomchanotai.mine3.Entity.PlayerCacheData;
import com.boomchanotai.mine3.Logger.Logger;
import com.boomchanotai.mine3.Repository.RedisRepository;
import com.boomchanotai.mine3.Repository.SpigotRepository;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Address implements CommandExecutor {
    private RedisRepository redisRepo;
    private SpigotRepository spigotRepo;

    public Address(RedisRepository redisRepo, SpigotRepository spigotRepo) {
        this.redisRepo = redisRepo;
        this.spigotRepo = spigotRepo;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player p = (Player) sender;
        PlayerCacheData playerCacheData = redisRepo.getPlayerInfo(p.getUniqueId());
        if (playerCacheData == null) {
            Logger.warning("Unexpected Event: Not found playerInfo!, UUID: " + p.getUniqueId() + ", Command: /address");
            return false;
        }

        String address = playerCacheData.getAddress();
        spigotRepo.sendMessage(p, address);

        return true;
    }
}

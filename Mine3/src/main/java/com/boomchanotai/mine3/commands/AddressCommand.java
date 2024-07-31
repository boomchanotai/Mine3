package com.boomchanotai.mine3.commands;

import com.boomchanotai.mine3.Mine3;
import com.boomchanotai.mine3.entity.PlayerCacheData;
import com.boomchanotai.mine3.logger.Logger;
import com.boomchanotai.mine3.repository.RedisRepository;
import com.boomchanotai.mine3.repository.SpigotRepository;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AddressCommand implements CommandExecutor {
    private RedisRepository redisRepo;
    private SpigotRepository spigotRepo;

    public AddressCommand(RedisRepository redisRepo, SpigotRepository spigotRepo) {
        this.redisRepo = redisRepo;
        this.spigotRepo = spigotRepo;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        if (args.length == 0 && !(sender instanceof Player)) {
            return false;
        }

        Player player = null;
        if (args.length == 0) {
            player = (Player) sender;
        }

        if (args.length == 1) {
            player = Mine3.getInstance().getServer().getPlayer(args[0]);
        }

        if (player == null) {
            return false;
        }

        PlayerCacheData playerCacheData = redisRepo.getPlayerInfo(player.getUniqueId());
        if (playerCacheData == null) {
            Logger.warning(
                    "Unexpected Event: Not found playerInfo!, UUID: " + player.getUniqueId()
                            + ", Command: /address");
            return false;
        }

        String address = playerCacheData.getAddress();
        spigotRepo.sendMessage(sender, address);
        return true;
    }
}

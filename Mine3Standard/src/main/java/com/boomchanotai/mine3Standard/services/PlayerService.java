package com.boomchanotai.mine3Standard.services;

import java.util.Date;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffect;

import com.boomchanotai.mine3Standard.config.SpawnConfig;
import com.boomchanotai.mine3Standard.entities.PlayerData;

import dev.iiahmed.disguise.Disguise;
import dev.iiahmed.disguise.DisguiseProvider;
import dev.iiahmed.disguise.Skin;

public class PlayerService {
    private DisguiseProvider provider;

    public PlayerService(DisguiseProvider provider) {
        this.provider = provider;
    }

    // clearPlayerState is clear player state for waiting login
    public void clearPlayerState(Player player) {
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setExp(0.0F);
        player.setGameMode(GameMode.SURVIVAL);
        player.setFlySpeed(0.1F);
        player.setWalkSpeed(0.2F);
        player.setFlying(false);
        player.setOp(false);

        ProfileBanList profileBanList = Bukkit.getBanList(BanList.Type.PROFILE);
        profileBanList.pardon(player.getPlayerProfile());

        player.getInventory().clear();
        player.getEnderChest().clear();

        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
    }

    // restorePlayerState is restore player state from database
    public void restorePlayerState(Player player, PlayerData playerData) {
        // Check Player Ban
        if (playerData.isBanned()) {
            player.ban("You are banned from this server.", (Date) null, null);
            return;
        }

        // Change Player Name
        Disguise disguise = Disguise.builder()
                .setName(playerData.getAddress().getShortAddress())
                .setSkin(Skin.of(player))
                .build();
        provider.disguise(player, disguise);
        player.setPlayerListName(playerData.getAddress().getShortAddress());

        // Set Player State
        player.setLevel(playerData.getXpLevel());
        player.setExp(playerData.getXpExp());
        player.setHealth(playerData.getHealth());
        player.setFoodLevel(playerData.getFoodLevel());
        player.setGameMode(playerData.getGameMode());
        player.setFlySpeed(playerData.getFlySpeed());
        player.setWalkSpeed(playerData.getWalkSpeed());
        player.setAllowFlight(playerData.getAllowFlight());
        player.setFlying(playerData.isFlying());
        player.setOp(playerData.isOp());
        player.getInventory().setContents(playerData.getInventory());
        player.getEnderChest().setContents(playerData.getEnderchest());

        for (PotionEffect potionEffect : playerData.getPotionEffects()) {
            player.addPotionEffect(potionEffect);
        }

        // Teleport Player to Last Location
        Location lastLocation = new Location(
                playerData.getPlayerLocation().getWorld(),
                playerData.getPlayerLocation().getX(),
                playerData.getPlayerLocation().getY(),
                playerData.getPlayerLocation().getZ(),
                playerData.getPlayerLocation().getYaw(),
                playerData.getPlayerLocation().getPitch());
        player.teleport(lastLocation, TeleportCause.PLUGIN);
    }

    // setPlayerIdleState is set player state for waiting login complete
    public void setPlayerIdleState(Player player) {
        clearPlayerState(player);
        player.setPlayerListName("Anonymous");

        player.setInvulnerable(true);
        player.setInvisible(true);

        // Teleport Player to Spawn Location
        Location spawnLocation = SpawnConfig.getSpawnLocation();
        player.teleport(spawnLocation);

        // cancel disguise
        provider.undisguise(player);
    }

    // setPlayerActiveState is set player state for active player
    public void setPlayerActiveState(Player player) {
        player.setInvulnerable(false);
        player.setInvisible(false);
    }
}

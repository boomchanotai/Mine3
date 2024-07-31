package com.boomchanotai.mine3Auth.service;

import com.boomchanotai.mine3Auth.Mine3Auth;
import com.boomchanotai.mine3Auth.config.SpawnConfig;
import com.boomchanotai.mine3Auth.entity.PlayerData;
import com.boomchanotai.mine3Auth.repository.SpigotRepository;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import static com.boomchanotai.mine3Auth.config.Config.*;

import java.util.*;

public class PlayerService {
    private SpigotRepository spigotRepo;

    private HashMap<UUID, Boolean> playerList;

    public PlayerService(SpigotRepository spigotRepo) {
        this.spigotRepo = spigotRepo;

        playerList = new HashMap<>();
    }

    public Map<UUID, Boolean> getPlayerList() {
        return playerList;
    }

    public void addPlayer(UUID playerUUID) {
        playerList.put(playerUUID, true);
    }

    public void removePlayer(UUID playerUUID) {
        playerList.remove(playerUUID);
    }

    public void removeAll() {
        playerList.clear();
    }

    public void sendLoginURL(Player player, String token) {
        TextComponent titleComponent = new TextComponent(
                ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, TITLE));
        titleComponent.setColor(ChatColor.BLUE);

        String url = AUTH_WEBSITE_TOKEN_BASE_URL + token;
        TextComponent urlComponent = new TextComponent(
                ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, AUTH_CLICK_TO_LOGIN_MESSAGE));
        urlComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        urlComponent.setUnderlined(true);
        urlComponent.setColor(ChatColor.GRAY);

        spigotRepo.sendMessage(
                player,
                titleComponent,
                urlComponent);
    }

    public void sendWelcomeTitle(Player player) {
        spigotRepo.sendTitle(player,
                AUTH_JOIN_SERVER_TITLE_TITLE,
                AUTH_JOIN_SERVER_TITLE_SUBTITLE,
                AUTH_JOIN_SERVER_TITLE_FADE_OUT,
                AUTH_JOIN_SERVER_TITLE_STAY,
                AUTH_JOIN_SERVER_TITLE_FADE_IN);
    }

    public void sendAuthenticatedTitle(Player player) {
        spigotRepo.sendTitle(
                player,
                AUTH_LOGGED_IN_TITLE_TITLE,
                AUTH_LOGGED_IN_TITLE_SUBTITLE,
                AUTH_LOGGED_IN_TITLE_FADE_IN,
                AUTH_LOGGED_IN_TITLE_STAY,
                AUTH_LOGGED_IN_TITLE_FADE_OUT);
    }

    public void sendPreventActionMessage(Player player) {
        spigotRepo.sendMessage(player, AUTH_PREVENT_ACTION_MESSAGE);
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
        player.getInventory().clear();
        player.getEnderChest().clear();

        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
    }

    // restorePlayerState is restore player state from database
    public void restorePlayerState(Player player, PlayerData playerData) {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                // Set Player State
                player.setLevel(playerData.getXpLevel());
                player.setExp(playerData.getXpExp());
                player.setHealth(playerData.getHealth());
                player.setFoodLevel(playerData.getFoodLevel());
                player.setGameMode(playerData.getGameMode());
                player.setFlySpeed(playerData.getFlySpeed());
                player.setWalkSpeed(playerData.getWalkSpeed());
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
        };
        runnable.runTaskLater(Mine3Auth.getInstance(), 0);
    }

    // setPlayerIdleState is set player state for waiting login complete
    public void setPlayerIdleState(Player player) {
        clearPlayerState(player);
        player.setInvulnerable(true);

        World world = Mine3Auth.getInstance().getServer()
                .getWorld(SpawnConfig.getSpawnConfig().getString("spawn.world"));
        Location spawnLocation = new Location(
                world,
                SpawnConfig.getSpawnConfig().getDouble("spawn.x"),
                SpawnConfig.getSpawnConfig().getDouble("spawn.y"),
                SpawnConfig.getSpawnConfig().getDouble("spawn.z"),
                (float) SpawnConfig.getSpawnConfig().getDouble("spawn.yaw"),
                (float) SpawnConfig.getSpawnConfig().getDouble("spawn.pitch"));
        player.teleport(spawnLocation);
    }

    // setPlayerActiveState is set player state for active player
    public void setPlayerActiveState(Player player) {
        player.setInvulnerable(false);
    }
}

package com.boomchanotai.mine3Auth.service;

import com.boomchanotai.mine3Auth.entity.PlayerData;
import com.boomchanotai.mine3Lib.address.Address;
import com.boomchanotai.mine3Lib.repository.PlayerRepository;

import dev.iiahmed.disguise.Disguise;
import dev.iiahmed.disguise.DisguiseProvider;
import dev.iiahmed.disguise.Skin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.BanList;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffect;

import static com.boomchanotai.mine3Auth.config.Config.*;

import java.util.*;

public class PlayerService {
    private SpawnService spawnService;
    private DisguiseProvider provider;
    private HashMap<UUID, Boolean> playerList;

    public PlayerService(SpawnService spawnService, DisguiseProvider provider) {
        this.spawnService = spawnService;
        this.provider = provider;
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
        String url = AUTH_WEBSITE_TOKEN_BASE_URL + token;
        TextComponent urlComponent = new TextComponent(
                ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, AUTH_CLICK_TO_LOGIN_MESSAGE));
        urlComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        urlComponent.setUnderlined(true);
        urlComponent.setColor(ChatColor.GRAY);

        PlayerRepository.sendMessage(player, urlComponent);
    }

    public void sendWelcomeTitle(Player player) {
        player.sendTitle(
                ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, AUTH_JOIN_SERVER_TITLE_TITLE),
                ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, AUTH_JOIN_SERVER_TITLE_SUBTITLE),
                AUTH_JOIN_SERVER_TITLE_FADE_OUT,
                AUTH_JOIN_SERVER_TITLE_STAY,
                AUTH_JOIN_SERVER_TITLE_FADE_IN);
    }

    public void sendAuthenticatedTitle(Player player) {
        player.sendTitle(
                ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, AUTH_LOGGED_IN_TITLE_TITLE),
                ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, AUTH_LOGGED_IN_TITLE_SUBTITLE),
                AUTH_LOGGED_IN_TITLE_FADE_IN,
                AUTH_LOGGED_IN_TITLE_STAY,
                AUTH_LOGGED_IN_TITLE_FADE_OUT);
    }

    public void sendPreventActionMessage(Player player) {
        PlayerRepository.sendMessage(player, AUTH_PREVENT_ACTION_MESSAGE);
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
        player.teleport(spawnService.getSpawnLocation());
    }

    // setPlayerActiveState is set player state for active player
    public void setPlayerActiveState(Player player) {
        player.setInvulnerable(false);
        player.setInvisible(false);
    }

    public void sendJoinMessage(Address address) {
        String message = AUTH_JOIN_MESSAGE.replace("{address}", address.getShortAddress());
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, message));
    }

    public void sendQuitMessage(Address address) {
        String message = AUTH_QUIT_MESSAGE.replace("{address}", address.getShortAddress());
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, message));
    }
}

package com.boomchanotai.mine3.Service;

import com.boomchanotai.mine3.Listeners.PreventPlayerActionWhenNotLoggedIn;
import com.boomchanotai.mine3.Logger.Logger;
import com.boomchanotai.mine3.Mine3;
import com.boomchanotai.mine3.Config.SpawnConfig;
import com.boomchanotai.mine3.Entity.PlayerCacheData;
import com.boomchanotai.mine3.Entity.PlayerData;
import com.boomchanotai.mine3.Entity.PlayerLocation;
import com.boomchanotai.mine3.Repository.PostgresRepository;
import com.boomchanotai.mine3.Repository.RedisRepository;
import com.boomchanotai.mine3.Repository.SpigotRepository;
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
import org.json.JSONObject;
import org.web3j.crypto.Keys;

import java.sql.SQLException;
import java.util.*;

import static com.boomchanotai.mine3.Config.Config.*;

public class PlayerService {
    private PostgresRepository pgRepo;
    private RedisRepository redisRepo;
    private SpigotRepository spigotRepo;
    private static final int TOKEN_LENGTH = 32;

    public PlayerService(PostgresRepository pgRepo, RedisRepository redisRepo, SpigotRepository spigotRepo) {
        this.pgRepo = pgRepo;
        this.redisRepo = redisRepo;
        this.spigotRepo = spigotRepo;
    }

    private static String getRandomHexString(int numchars) {
        Random r = new Random();
        StringBuilder stringBuffer = new StringBuilder();
        while (stringBuffer.length() < numchars) {
            stringBuffer.append(Integer.toHexString(r.nextInt()));
        }

        return stringBuffer.substring(0, numchars);
    }

    public boolean isPlayerInGame(UUID playerUUID) {
        Player player = Mine3.getInstance().getServer().getPlayer(playerUUID);
        return player != null;
    }

    public void connectPlayer(Player player) {
        setPlayerIdleState(player);

        UUID playerUUID = player.getUniqueId();

        String token = getRandomHexString(TOKEN_LENGTH);
        redisRepo.setToken(token, playerUUID);

        // Send Message to player
        sendLoginURL(player, token);

        spigotRepo.sendTitle(player,
                AUTH_JOIN_SERVER_TITLE_TITLE,
                AUTH_JOIN_SERVER_TITLE_SUBTITLE,
                AUTH_JOIN_SERVER_TITLE_FADE_OUT,
                AUTH_JOIN_SERVER_TITLE_STAY,
                AUTH_JOIN_SERVER_TITLE_FADE_IN);
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

    public void playerLogin(String token, String address) throws Exception {
        String parsedAddress = Keys.toChecksumAddress(address);
        // 1. Get Player UUID
        UUID playerUUID = redisRepo.getPlayerUUIDFromToken(token);
        if (playerUUID == null) {
            Logger.warning("INVALID_TOKEN", "Can't get player from token.", parsedAddress);
            throw new Exception("INVALID_TOKEN");
        }

        // 2. Check Player in game
        boolean isPlayerInGame = isPlayerInGame(playerUUID);
        if (!isPlayerInGame) {
            Logger.warning("PLAYER_NOT_IN_GAME", "Not found player in game.", parsedAddress);
            throw new Exception("PLAYER_NOT_IN_GAME");
        }

        // 3. Check address already exist
        UUID checkPlayer = redisRepo.getPlayerFromAddress(parsedAddress);
        if (checkPlayer != null) {
            Logger.warning("ADDRESS_ALREADY_USED", "Address already used", parsedAddress);
            throw new Exception("ADDRESS_ALREADY_USED");
        }

        // 4. Store Player: (json) address
        JSONObject playerInfo = new JSONObject();
        playerInfo.put("address", parsedAddress);
        redisRepo.setPlayerInfo(playerUUID, playerInfo.toString());

        // 5. Store Address: PlayerUUID
        redisRepo.setAddress(playerUUID, parsedAddress);

        // 6. Delete Token
        redisRepo.deleteToken(token);

        // 7. Game Logic
        PreventPlayerActionWhenNotLoggedIn.playerConnected(playerUUID);
        Player player = Mine3.getInstance().getServer().getPlayer(playerUUID);
        if (player == null) {
            Logger.warning("Player is null", "Not found player in game.", parsedAddress);
            return;
        }

        // 8. Create User in Database

        if (!pgRepo.isAddressExist(parsedAddress)) {
            PlayerLocation playerLocation = new PlayerLocation(
                    player.getLocation().getX(),
                    player.getLocation().getY(),
                    player.getLocation().getZ(),
                    player.getLocation().getYaw(),
                    player.getLocation().getPitch(),
                    Objects.requireNonNull(player.getLocation().getWorld()));

            PlayerData createPlayerData = new PlayerData(parsedAddress, playerLocation);

            try {
                pgRepo.createNewPlayer(createPlayerData);
            } catch (SQLException exception) {
                Logger.warning(exception.getMessage(), "Failed to create new player", parsedAddress);
                throw exception;
            }
        }

        // 9. Update user Login
        try {
            pgRepo.setUserLoggedIn(parsedAddress);
        } catch (SQLException exception) {
            Logger.warning(exception.getMessage(), "Failed to update user login.", parsedAddress);
            throw exception;
        }

        // 10. get player info
        PlayerData playerData = pgRepo.getPlayerData(parsedAddress);
        if (playerData == null) {
            Logger.warning("PlayerData is null", "Failed to get player data in database.", parsedAddress);
            return;
        }
        setPlayerActiveState(player);
        restorePlayerState(player, playerData);

        // 11. Send Title
        spigotRepo.sendTitle(
                player,
                AUTH_LOGGED_IN_TITLE_TITLE,
                AUTH_LOGGED_IN_TITLE_SUBTITLE,
                AUTH_LOGGED_IN_TITLE_FADE_IN,
                AUTH_LOGGED_IN_TITLE_STAY,
                AUTH_LOGGED_IN_TITLE_FADE_OUT);
        spigotRepo.sendMessage(player, "Login as " + parsedAddress);
    }

    public void disconnectPlayer(Player player) {
        UUID playerUUID = player.getUniqueId();

        PreventPlayerActionWhenNotLoggedIn.playerDisconnected(playerUUID);

        PlayerCacheData playerCacheData = redisRepo.getPlayerInfo(playerUUID);
        // If Player not login
        if (playerCacheData == null) {
            return;
        }

        String address = playerCacheData.getAddress();
        String parsedAddress = Keys.toChecksumAddress(address);

        // Update user inventory
        try {
            PlayerLocation playerLocation = new PlayerLocation(
                    player.getLocation().getX(),
                    player.getLocation().getY(),
                    player.getLocation().getZ(),
                    player.getLocation().getYaw(),
                    player.getLocation().getPitch(),
                    Objects.requireNonNull(player.getLocation().getWorld()));

            PlayerData playerData = new PlayerData(
                    parsedAddress,
                    false,
                    player.getLevel(),
                    player.getExp(),
                    player.getHealth(),
                    player.getFoodLevel(),
                    player.getGameMode(),
                    player.getFlySpeed(),
                    player.getWalkSpeed(),
                    player.isFlying(),
                    player.isOp(),
                    player.getActivePotionEffects(),
                    player.getInventory().getContents(),
                    player.getEnderChest().getContents(),
                    playerLocation);

            pgRepo.updateUserInventory(playerData);
        } catch (SQLException exception) {
            Logger.warning(exception.getMessage(), "Failed to update user inventory.", parsedAddress);
        }
        clearPlayerState(player);

        // Delete player address in cache
        try {
            redisRepo.deleteAddress(parsedAddress);
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "Failed to delete address.", parsedAddress);
        }

        // Delete player info in cache
        try {
            redisRepo.deletePlayerInfo(playerUUID);
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "Failed to delete player info.", parsedAddress);
        }
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
        runnable.runTaskLater(Mine3.getInstance(), 0);
    }

    // setPlayerIdleState is set player state for waiting login complete
    public void setPlayerIdleState(Player player) {
        clearPlayerState(player);
        player.setInvulnerable(true);

        World world = Mine3.getInstance().getServer()
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

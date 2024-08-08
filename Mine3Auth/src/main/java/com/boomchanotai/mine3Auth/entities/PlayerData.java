package com.boomchanotai.mine3Auth.entities;

import java.util.Collection;

import org.bukkit.GameMode;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import com.boomchanotai.mine3Lib.address.Address;

public class PlayerData {
    private Address address;
    private String ensDomain;
    private boolean isLoggedIn;
    private int xpLevel;
    private float xpExp;
    private double health;
    private int foodLevel;
    private GameMode gameMode;
    private float flySpeed;
    private float walkSpeed;
    private boolean allowFlight;
    private boolean isFlying;
    private boolean isOp;
    private boolean isBanned;
    private Collection<PotionEffect> potionEffects;
    private ItemStack[] inventory;
    private ItemStack[] enderchest;
    private PlayerLocation playerLocation;

    public PlayerData(Address address, String ensDomain, boolean isLoggedIn, int xpLevel, float xpExp, double health,
            int foodLevel, GameMode gameMode, float flySpeed, float walkSpeed, boolean allowFlight, boolean isFlying,
            boolean isOp, boolean isBanned, Collection<PotionEffect> potionEffects, ItemStack[] inventory,
            ItemStack[] enderchest, PlayerLocation playerLocation) {
        this.address = address;
        this.ensDomain = ensDomain;
        this.isLoggedIn = isLoggedIn;
        this.xpLevel = xpLevel;
        this.xpExp = xpExp;
        this.health = health;
        this.foodLevel = foodLevel;
        this.gameMode = gameMode;
        this.flySpeed = flySpeed;
        this.walkSpeed = walkSpeed;
        this.allowFlight = allowFlight;
        this.isFlying = isFlying;
        this.isOp = isOp;
        this.isBanned = isBanned;
        this.potionEffects = potionEffects;
        this.inventory = inventory;
        this.enderchest = enderchest;
        this.playerLocation = playerLocation;
    }

    public PlayerData(Address address, String ensDomain, PlayerLocation playerLocation) {
        this.address = address;
        this.ensDomain = ensDomain;
        this.playerLocation = playerLocation;
    }

    public Address getAddress() {
        return address;
    }

    public String getEnsDomain() {
        return ensDomain;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public int getXpLevel() {
        return xpLevel;
    }

    public float getXpExp() {
        return xpExp;
    }

    public double getHealth() {
        return health;
    }

    public int getFoodLevel() {
        return foodLevel;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public float getFlySpeed() {
        return flySpeed;
    }

    public float getWalkSpeed() {
        return walkSpeed;
    }

    public boolean getAllowFlight() {
        return allowFlight;
    }

    public boolean isFlying() {
        return isFlying;
    }

    public boolean isOp() {
        return isOp;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public Collection<PotionEffect> getPotionEffects() {
        return potionEffects;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    public ItemStack[] getEnderchest() {
        return enderchest;
    }

    public PlayerLocation getPlayerLocation() {
        return playerLocation;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setEnsDomain(String ensDomain) {
        this.ensDomain = ensDomain;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public void setXpLevel(int xpLevel) {
        this.xpLevel = xpLevel;
    }

    public void setXpExp(float xpExp) {
        this.xpExp = xpExp;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setFoodLevel(int foodLevel) {
        this.foodLevel = foodLevel;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public void setFlySpeed(float flySpeed) {
        this.flySpeed = flySpeed;
    }

    public void setWalkSpeed(float walkSpeed) {
        this.walkSpeed = walkSpeed;
    }

    public void setAllowFlight(boolean allowFlight) {
        this.allowFlight = allowFlight;
    }

    public void setFlying(boolean flying) {
        isFlying = flying;
    }

    public void setOp(boolean op) {
        isOp = op;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public void setPotionEffects(Collection<PotionEffect> potionEffects) {
        this.potionEffects = potionEffects;
    }

    public void setInventory(ItemStack[] inventory) {
        this.inventory = inventory;
    }

    public void setEnderchest(ItemStack[] enderchest) {
        this.enderchest = enderchest;
    }

    public void setPlayerLocation(PlayerLocation playerLocation) {
        this.playerLocation = playerLocation;
    }
}

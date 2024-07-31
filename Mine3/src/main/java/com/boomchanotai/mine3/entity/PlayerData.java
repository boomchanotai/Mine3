package com.boomchanotai.mine3.entity;

import java.util.Collection;

import org.bukkit.GameMode;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class PlayerData {
    private String address;
    private boolean isLoggedIn;
    private int xpLevel;
    private float xpExp;
    private double health;
    private int foodLevel;
    private GameMode gameMode;
    private float flySpeed;
    private float walkSpeed;
    private boolean isFlying;
    private boolean isOp;
    private Collection<PotionEffect> potionEffects;
    private ItemStack[] inventory;
    private ItemStack[] enderchest;
    private PlayerLocation playerLocation;

    public PlayerData(String address, boolean isLoggedIn, int xpLevel, float xpExp, double health, int foodLevel,
            GameMode gameMode, float flySpeed, float walkSpeed, boolean isFlying, boolean isOp,
            Collection<PotionEffect> potionEffects, ItemStack[] inventory, ItemStack[] enderchest,
            PlayerLocation playerLocation) {
        this.address = address;
        this.isLoggedIn = isLoggedIn;
        this.xpLevel = xpLevel;
        this.xpExp = xpExp;
        this.health = health;
        this.foodLevel = foodLevel;
        this.gameMode = gameMode;
        this.flySpeed = flySpeed;
        this.walkSpeed = walkSpeed;
        this.isFlying = isFlying;
        this.isOp = isOp;
        this.potionEffects = potionEffects;
        this.inventory = inventory;
        this.enderchest = enderchest;
        this.playerLocation = playerLocation;
    }

    public PlayerData(String address, PlayerLocation playerLocation) {
        this.address = address;
        this.playerLocation = playerLocation;
    }

    public String getAddress() {
        return address;
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

    public boolean isFlying() {
        return isFlying;
    }

    public boolean isOp() {
        return isOp;
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

    public void setAddress(String address) {
        this.address = address;
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

    public void setFlying(boolean flying) {
        isFlying = flying;
    }

    public void setOp(boolean op) {
        isOp = op;
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

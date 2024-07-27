package com.boomchanotai.mine3.Entity;

import org.bukkit.inventory.ItemStack;

public class PlayerData {
    private String address;
    private boolean isLoggedIn;
    private int xpLevel;
    private float xpExp;
    private double health;
    private int foodLevel;
    private ItemStack[] inventory;
    private ItemStack[] enderchest;
    private PlayerLocation playerLocation;

    public PlayerData(String address, boolean isLoggedIn, int xpLevel, float xpExp, double health, int foodLevel,
            ItemStack[] inventory, ItemStack[] enderchest, PlayerLocation playerLocation) {
        this.address = address;
        this.isLoggedIn = isLoggedIn;
        this.xpLevel = xpLevel;
        this.xpExp = xpExp;
        this.health = health;
        this.foodLevel = foodLevel;
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

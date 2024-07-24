package com.boomchanotai.mine3.Entity;

public class PlayerData {
    private String address;
    private boolean isLoggedIn;
    private int xpLevel;
    private float xpExp;
    private int health;
    private int foodLevel;
    private PlayerLocation playerLocation;

    public PlayerData(String address, boolean isLoggedIn, int xpLevel, float xpExp, int health, int foodLevel, PlayerLocation playerLocation) {
        this.address = address;
        this.isLoggedIn = isLoggedIn;
        this.xpLevel = xpLevel;
        this.xpExp = xpExp;
        this.health = health;
        this.foodLevel = foodLevel;
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

    public int getHealth() {
        return health;
    }

    public int getFoodLevel() {
        return foodLevel;
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

    public void setHealth(int health) {
        this.health = health;
    }

    public void setFoodLevel(int foodLevel) {
        this.foodLevel = foodLevel;
    }

    public void setPlayerLocation(PlayerLocation playerLocation) {
        this.playerLocation = playerLocation;
    }
}



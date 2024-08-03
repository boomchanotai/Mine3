package com.boomchanotai.mine3Auth.service;

import org.bukkit.Location;

import com.boomchanotai.mine3Auth.config.SpawnConfig;

public class SpawnService {
    public void setSpawnLocation(Location location) {
        SpawnConfig.setSpawnLocation(location);
    }

    public Location getSpawnLocation() {
        return SpawnConfig.getSpawnLocation();
    }
}

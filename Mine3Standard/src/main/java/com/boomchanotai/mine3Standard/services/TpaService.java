package com.boomchanotai.mine3Standard.services;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import com.boomchanotai.mine3Standard.Mine3Standard;

import static com.boomchanotai.mine3Standard.constants.Constants.TICKS_PER_SECOND;
import static com.boomchanotai.mine3Standard.config.Config.TELEPORT_TIMEOUT;

public class TpaService {
    private Mine3Standard plugin;
    // (hashmap) toAddress, fromAddress
    private static HashMap<String, String> tpaRequests;
    private BukkitTask task;

    public TpaService(Mine3Standard plugin) {
        this.plugin = plugin;
        tpaRequests = new HashMap<String, String>();
    }

    public void addTpaRequest(String fromAddress, String toAddress) {
        tpaRequests.put(toAddress, fromAddress);

        // add timeout for tpa request
        task = Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                if (hasTpaRequest(toAddress) && getTpaRequest(toAddress).equals(fromAddress)) {
                    removeTpaRequest(toAddress);
                }

                task.cancel();
            }
        }, TICKS_PER_SECOND * TELEPORT_TIMEOUT);
    }

    public void removeTpaRequest(String toAddress) {
        tpaRequests.remove(toAddress);
    }

    public boolean hasTpaRequest(String toAddress) {
        return tpaRequests.containsKey(toAddress);
    }

    public String getTpaRequest(String toAddress) {
        return tpaRequests.get(toAddress);
    }

    public void clearTpaRequests() {
        tpaRequests.clear();
    }

    public HashMap<String, String> getTpaRequests() {
        return tpaRequests;
    }
}

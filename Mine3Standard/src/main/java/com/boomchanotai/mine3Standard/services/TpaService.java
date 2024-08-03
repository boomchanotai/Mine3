package com.boomchanotai.mine3Standard.services;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import com.boomchanotai.mine3Standard.Mine3Standard;

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
        // Timeout: 5 minutes
        // task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new
        // Runnable() {
        // @Override
        // public void run() {
        // if (hasTpaRequest(toAddress) && getTpaRequest(toAddress).equals(fromAddress))
        // {
        // removeTpaRequest(toAddress);
        // }

        // task.cancel();
        // }
        // }, 10);
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

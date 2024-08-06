package com.boomchanotai.mine3Permission.services;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.boomchanotai.mine3Permission.Mine3Permission;

public class PermissionManager {
    private static String defaultGroup;
    private static HashMap<String, ArrayList<String>> permissionMap;

    public PermissionManager() {
        defaultGroup = null;
        permissionMap = new HashMap<>();
    }

    public void initializePermission() {
        FileConfiguration config = Mine3Permission.getPlugin().getConfig();
        ConfigurationSection groups = config.getConfigurationSection("groups");

        groups.getKeys(false).forEach(key -> {
            ArrayList<String> permissions = new ArrayList<>();

            // Add permissions to the list
            config.getList("groups." + key + ".permissions").forEach(permission -> {
                permissions.add(permission.toString());
            });

            // Add permission from inheritance
            if (config.contains("groups." + key + ".inheritance")) {
                config.getList("groups." + key + ".inheritance").forEach(inheritance -> {
                    config.getList("groups." + inheritance + ".permissions").forEach(permission -> {
                        permissions.add(permission.toString());
                    });
                });
            }

            // Init default group
            if (config.getBoolean("groups." + key + ".default") && defaultGroup == null) {
                defaultGroup = key;
            }

            permissionMap.put(key, permissions);
        });
    }

    public void getGroupPermission(String group) {
        permissionMap.get(group);
    }

    public String getDefaultGroup() {
        return defaultGroup;
    }

    public static HashMap<String, ArrayList<String>> getPermissionMap() {
        return permissionMap;
    }
}

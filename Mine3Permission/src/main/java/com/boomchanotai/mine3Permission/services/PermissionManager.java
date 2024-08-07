package com.boomchanotai.mine3Permission.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import com.boomchanotai.mine3Lib.address.Address;
import com.boomchanotai.mine3Lib.repository.PlayerRepository;
import com.boomchanotai.mine3Permission.Mine3Permission;
import com.boomchanotai.mine3Permission.repositories.PostgresRepository;

import static com.boomchanotai.mine3Lib.config.Config.COLOR_CODE_PREFIX;

public class PermissionManager {
    private PostgresRepository pgRepo;
    private static String defaultGroup;
    private static HashMap<String, ArrayList<String>> permissionMap;
    private static HashMap<UUID, PermissionAttachment> permissionAttachmentMap;

    public PermissionManager(PostgresRepository pgRepo) {
        this.pgRepo = pgRepo;
        defaultGroup = null;
        permissionMap = new HashMap<>();
        permissionAttachmentMap = new HashMap<>();
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

    public Set<String> getGroups() {
        return permissionMap.keySet();
    }

    public HashMap<String, ArrayList<String>> getPermissionMap() {
        return permissionMap;
    }

    public void attachPermissionGroup(Address address, String group) {
        if (group == null) {
            group = getDefaultGroup();
        }

        pgRepo.createGroup(address, group);

        Player player = PlayerRepository.getPlayer(address);

        // Remove previous permission attachment
        unattachPermissionGroup(player);

        // Create new permission attachment
        PermissionAttachment attachment = player.addAttachment(Mine3Permission.getPlugin());
        permissionMap.get(group).forEach(permission -> {
            attachment.setPermission(permission, true);
        });

        // set prefix and suffix
        FileConfiguration config = Mine3Permission.getPlugin().getConfig();
        String prefix = config.getString("groups." + group + ".metadata.prefix");
        String suffix = config.getString("groups." + group + ".metadata.suffix");
        String name = ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, prefix) + address.getShortAddress()
                + ChatColor.translateAlternateColorCodes(COLOR_CODE_PREFIX, suffix);
        player.setDisplayName(name);

        permissionAttachmentMap.put(player.getUniqueId(), attachment);
    }

    public void unattachPermissionGroup(Player player) {
        if (!permissionAttachmentMap.containsKey(player.getUniqueId())) {
            return;
        }

        player.removeAttachment(permissionAttachmentMap.remove(player.getUniqueId()));
    }

    public void reloadPermission() {
        permissionMap.clear();
        permissionAttachmentMap.clear();
        defaultGroup = null;
        initializePermission();

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            Address address = PlayerRepository.getAddress(p.getUniqueId());
            String group = pgRepo.getGroup(address);
            attachPermissionGroup(address, group);
        }
    }
}

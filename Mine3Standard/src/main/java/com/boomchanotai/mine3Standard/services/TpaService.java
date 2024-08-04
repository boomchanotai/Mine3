package com.boomchanotai.mine3Standard.services;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.web3j.crypto.Keys;

import com.boomchanotai.mine3Lib.repository.PlayerRepository;
import com.boomchanotai.mine3Lib.utils.AddressUtils;
import com.boomchanotai.mine3Standard.Mine3Standard;
import com.boomchanotai.mine3Standard.utils.Utils;

import kotlin.Pair;

import static com.boomchanotai.mine3Standard.constants.Constants.TICKS_PER_SECOND;
import static com.boomchanotai.mine3Standard.config.Config.TELEPORT_TIMEOUT;

public class TpaService {
    enum TpaType {
        TPA,
        TPAHERE
    }

    private Mine3Standard plugin;
    // (hashmap) toAddress, fromAddress
    private static HashMap<String, Pair<String, TpaType>> tpaRequests;
    private BukkitTask task;

    public TpaService(Mine3Standard plugin) {
        this.plugin = plugin;
        tpaRequests = new HashMap<>();
    }

    public void addTpaRequest(String fromAddress, String toAddress) {
        tpaRequests.put(toAddress, new Pair<>(fromAddress, TpaType.TPA));

        // add timeout for tpa request
        task = Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                if (hasTpaRequest(toAddress, "TPA") && getTpaRequest(toAddress).getFirst().equals(fromAddress)) {
                    removeTpaRequest(toAddress);
                }

                task.cancel();
            }
        }, TICKS_PER_SECOND * TELEPORT_TIMEOUT);
    }

    public void removeTpaRequest(String address) {
        tpaRequests.remove(address);
    }

    public void addTpaHereRequest(String fromAddress, String toAddress) {
        tpaRequests.put(toAddress, new Pair<>(fromAddress, TpaType.TPAHERE));

        // add timeout for tpa request
        task = Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                if (hasTpaRequest(toAddress, "TPAHERE") && getTpaRequest(toAddress).getFirst().equals(fromAddress)) {
                    removeTpaRequest(toAddress);
                }

                task.cancel();
            }
        }, TICKS_PER_SECOND * TELEPORT_TIMEOUT);
    }

    public boolean hasTpaRequest(String address, String type) {
        TpaType tpaType = null;

        switch (type) {
            case "TPA":
                tpaType = TpaType.TPA;
                break;
            case "TPAHERE":
                tpaType = TpaType.TPAHERE;
                break;
            default:
                break;
        }

        if (tpaType == null) {
            return false;
        }

        return tpaRequests.containsKey(address) && tpaRequests.get(address).getSecond() == tpaType;
    }

    public Pair<String, TpaType> getTpaRequest(String address) {
        return tpaRequests.get(address);
    }

    public void clearTpaRequests() {
        tpaRequests.clear();
    }

    public void acceptTpaRequest(String address) {
        Pair<String, TpaType> request = getTpaRequest(address);
        if (request == null) {
            Utils.sendCommandReturnMessage(PlayerRepository.getPlayer(Keys.toChecksumAddress(address)),
                    "You don't have any tpa request.");
            return;
        }

        removeTpaRequest(address);

        if (request.getSecond() == TpaType.TPA) {
            String toPlayerAddress = Keys.toChecksumAddress(address);

            Player toPlayer = PlayerRepository.getPlayer(toPlayerAddress);
            if (toPlayer == null) {
                Utils.sendCommandReturnMessage(toPlayer, "Can't parse your address. Please login again.");
                return;
            }

            String fromPlayerAddress = request.getFirst();

            Player fromPlayer = PlayerRepository.getPlayer(fromPlayerAddress);
            if (fromPlayer == null) {
                Utils.sendCommandReturnMessage(fromPlayer,
                        "Not found " + AddressUtils.getShortAddress(fromPlayerAddress) + " in game.");
                return;
            }

            fromPlayer.teleport(toPlayer.getLocation());
            PlayerRepository.sendMessage(fromPlayer,
                    "Teleporting to " + AddressUtils.getShortAddress(toPlayerAddress) + "...");
            PlayerRepository.sendMessage(toPlayer,
                    "Teleporting " + AddressUtils.getShortAddress(fromPlayerAddress) + " to you...");

            return;
        } else if (request.getSecond() == TpaType.TPAHERE) {
            String fromPlayerAddress = Keys.toChecksumAddress(address);

            Player fromPlayer = PlayerRepository.getPlayer(fromPlayerAddress);
            if (fromPlayer == null) {
                Utils.sendCommandReturnMessage(fromPlayer, "Can't parse your address. Please login again.");
                return;
            }

            String toPlayerAddress = request.getFirst();

            Player toPlayer = PlayerRepository.getPlayer(toPlayerAddress);
            if (toPlayer == null) {
                Utils.sendCommandReturnMessage(toPlayer,
                        "Not found " + AddressUtils.getShortAddress(toPlayerAddress) + " in game.");
                return;
            }

            fromPlayer.teleport(toPlayer.getLocation());
            PlayerRepository.sendMessage(fromPlayer,
                    "Teleporting to " + AddressUtils.getShortAddress(fromPlayerAddress) + "...");
            PlayerRepository.sendMessage(toPlayer,
                    "Teleporting " + AddressUtils.getShortAddress(toPlayerAddress) + " to you...");

            return;
        }
    }

    public void cancelTpaRequest(String address) {
        Pair<String, TpaType> request = getTpaRequest(address);
        if (request == null) {
            Utils.sendCommandReturnMessage(PlayerRepository.getPlayer(Keys.toChecksumAddress(address)),
                    "You don't have any tpa request.");
            return;
        }

        removeTpaRequest(address);

        if (request.getSecond() == TpaType.TPA) {
            String toPlayerAddress = Keys.toChecksumAddress(address);

            Player toPlayer = PlayerRepository.getPlayer(toPlayerAddress);
            if (toPlayer == null) {
                Utils.sendCommandReturnMessage(toPlayer, "Can't parse your address. Please login again.");
                return;
            }

            String fromPlayerAddress = request.getFirst();

            Player fromPlayer = PlayerRepository.getPlayer(fromPlayerAddress);
            if (fromPlayer == null) {
                Utils.sendCommandReturnMessage(fromPlayer,
                        "Not found " + AddressUtils.getShortAddress(fromPlayerAddress) + " in game.");
                return;
            }

            PlayerRepository.sendMessage(fromPlayer,
                    "Tpa request to " + AddressUtils.getShortAddress(toPlayerAddress) + " has been canceled.");
            PlayerRepository.sendMessage(toPlayer,
                    "Tpa request from " + AddressUtils.getShortAddress(fromPlayerAddress) + " has been canceled.");

            return;
        } else if (request.getSecond() == TpaType.TPAHERE) {
            String fromPlayerAddress = Keys.toChecksumAddress(address);

            Player fromPlayer = PlayerRepository.getPlayer(fromPlayerAddress);
            if (fromPlayer == null) {
                Utils.sendCommandReturnMessage(fromPlayer, "Can't parse your address. Please login again.");
                return;
            }

            String toPlayerAddress = request.getFirst();

            Player toPlayer = PlayerRepository.getPlayer(toPlayerAddress);
            if (toPlayer == null) {
                Utils.sendCommandReturnMessage(toPlayer,
                        "Not found " + AddressUtils.getShortAddress(toPlayerAddress) + " in game.");
                return;
            }

            PlayerRepository.sendMessage(toPlayer,
                    "Tpahere request from " + AddressUtils.getShortAddress(fromPlayerAddress) + " has been canceled.");
            PlayerRepository.sendMessage(fromPlayer,
                    "Tpahere request to " + AddressUtils.getShortAddress(toPlayerAddress) + " has been canceled.");

            return;
        }
    }
}

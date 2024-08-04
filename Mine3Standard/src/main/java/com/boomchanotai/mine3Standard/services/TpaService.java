package com.boomchanotai.mine3Standard.services;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import com.boomchanotai.mine3Lib.address.Address;
import com.boomchanotai.mine3Lib.repository.PlayerRepository;
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
    private static HashMap<Address, Pair<Address, TpaType>> tpaRequests;
    private BukkitTask task;

    public TpaService(Mine3Standard plugin) {
        this.plugin = plugin;
        tpaRequests = new HashMap<>();
    }

    public void addTpaRequest(Address fromAddress, Address toAddress) {
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

    public void removeTpaRequest(Address address) {
        tpaRequests.remove(address);
    }

    public void addTpaHereRequest(Address fromAddress, Address toAddress) {
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

    public boolean hasTpaRequest(Address address, String type) {
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

    public Pair<Address, TpaType> getTpaRequest(Address address) {
        return tpaRequests.get(address);
    }

    public void clearTpaRequests() {
        tpaRequests.clear();
    }

    public void acceptTpaRequest(Address address) {
        Pair<Address, TpaType> request = getTpaRequest(address);
        if (request == null) {
            Utils.sendCommandReturnMessage(PlayerRepository.getPlayer(address),
                    "You don't have any tpa request.");
            return;
        }

        removeTpaRequest(address);

        if (request.getSecond() == TpaType.TPA) {
            Address toPlayerAddress = address;

            Player toPlayer = PlayerRepository.getPlayer(toPlayerAddress);
            if (toPlayer == null) {
                Utils.sendCommandReturnMessage(toPlayer, "Can't parse your address. Please login again.");
                return;
            }

            Address fromPlayerAddress = request.getFirst();

            Player fromPlayer = PlayerRepository.getPlayer(fromPlayerAddress);
            if (fromPlayer == null) {
                Utils.sendCommandReturnMessage(fromPlayer,
                        "Not found " + fromPlayerAddress.getShortAddress() + " in game.");
                return;
            }

            fromPlayer.teleport(toPlayer.getLocation());
            PlayerRepository.sendMessage(fromPlayer,
                    "Teleporting to " + toPlayerAddress.getShortAddress() + "...");
            PlayerRepository.sendMessage(toPlayer,
                    "Teleporting " + fromPlayerAddress.getShortAddress() + " to you...");

            return;
        } else if (request.getSecond() == TpaType.TPAHERE) {
            Address fromPlayerAddress = address;

            Player fromPlayer = PlayerRepository.getPlayer(fromPlayerAddress);
            if (fromPlayer == null) {
                Utils.sendCommandReturnMessage(fromPlayer, "Can't parse your address. Please login again.");
                return;
            }

            Address toPlayerAddress = request.getFirst();

            Player toPlayer = PlayerRepository.getPlayer(toPlayerAddress);
            if (toPlayer == null) {
                Utils.sendCommandReturnMessage(toPlayer,
                        "Not found " + toPlayerAddress.getShortAddress() + " in game.");
                return;
            }

            fromPlayer.teleport(toPlayer.getLocation());
            PlayerRepository.sendMessage(fromPlayer,
                    "Teleporting to " + fromPlayerAddress.getShortAddress() + "...");
            PlayerRepository.sendMessage(toPlayer,
                    "Teleporting " + toPlayerAddress.getShortAddress() + " to you...");

            return;
        }
    }

    public void cancelTpaRequest(Address address) {
        Pair<Address, TpaType> request = getTpaRequest(address);
        if (request == null) {
            Utils.sendCommandReturnMessage(PlayerRepository.getPlayer(address),
                    "You don't have any tpa request.");
            return;
        }

        removeTpaRequest(address);

        if (request.getSecond() == TpaType.TPA) {
            Address toPlayerAddress = address;

            Player toPlayer = PlayerRepository.getPlayer(toPlayerAddress);
            if (toPlayer == null) {
                Utils.sendCommandReturnMessage(toPlayer, "Can't parse your address. Please login again.");
                return;
            }

            Address fromPlayerAddress = request.getFirst();

            Player fromPlayer = PlayerRepository.getPlayer(fromPlayerAddress);
            if (fromPlayer == null) {
                Utils.sendCommandReturnMessage(fromPlayer,
                        "Not found " + fromPlayerAddress.getShortAddress() + " in game.");
                return;
            }

            PlayerRepository.sendMessage(fromPlayer,
                    "Tpa request to " + toPlayerAddress.getShortAddress() + " has been canceled.");
            PlayerRepository.sendMessage(toPlayer,
                    "Tpa request from " + fromPlayerAddress.getShortAddress() + " has been canceled.");

            return;
        } else if (request.getSecond() == TpaType.TPAHERE) {
            Address fromPlayerAddress = address;

            Player fromPlayer = PlayerRepository.getPlayer(fromPlayerAddress);
            if (fromPlayer == null) {
                Utils.sendCommandReturnMessage(fromPlayer, "Can't parse your address. Please login again.");
                return;
            }

            Address toPlayerAddress = request.getFirst();

            Player toPlayer = PlayerRepository.getPlayer(toPlayerAddress);
            if (toPlayer == null) {
                Utils.sendCommandReturnMessage(toPlayer,
                        "Not found " + toPlayerAddress.getShortAddress() + " in game.");
                return;
            }

            PlayerRepository.sendMessage(toPlayer,
                    "Tpahere request from " + fromPlayerAddress.getShortAddress() + " has been canceled.");
            PlayerRepository.sendMessage(fromPlayer,
                    "Tpahere request to " + toPlayerAddress.getShortAddress() + " has been canceled.");

            return;
        }
    }
}

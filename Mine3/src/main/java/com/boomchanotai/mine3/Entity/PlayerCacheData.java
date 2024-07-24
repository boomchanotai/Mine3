package com.boomchanotai.mine3.Entity;

import org.web3j.crypto.Keys;

public class PlayerCacheData {
    private String address;

    public PlayerCacheData(String address) {
        this.address = address;
    }

    public String getAddress() {
        return Keys.toChecksumAddress(address);
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

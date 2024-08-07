package com.boomchanotai.mine3Auth.entities;

import com.boomchanotai.mine3Lib.address.Address;

public class PlayerCacheData {
    private Address address;

    public PlayerCacheData(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}

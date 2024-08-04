package com.boomchanotai.mine3Lib.address;

import org.web3j.crypto.Keys;

public class Address {
    private String address;

    public Address(String address) {
        this.address = Keys.toChecksumAddress(address);
    }

    public String getShortAddress() {
        return address.substring(0, 5) + "..." + address.substring(38);
    }

    public String toString() {
        return address;
    }

    public boolean equals(Address other) {
        return address.equals(other.toString());
    }
}

package com.boomchanotai.mine3Lib.address;

public class Address {
    private org.web3j.abi.datatypes.Address address;

    public Address(String address) {
        this.address = new org.web3j.abi.datatypes.Address(address);
    }

    public String getShortAddress() {
        return address.toString().substring(0, 6) + "..."
                + address.toString().substring(address.toString().length() - 4);
    }

    public String toString() {
        return address.toString();
    }

    public String getValue() {
        return address.getValue();
    }

    public boolean equals(Address other) {
        if (this.address.equals(other.address)) {
            return true;
        }
        return false;
    }
}

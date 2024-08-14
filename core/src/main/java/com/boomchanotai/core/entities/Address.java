package com.boomchanotai.core.entities;

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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Address) {
            return address.equals(((Address) obj).address);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }
}

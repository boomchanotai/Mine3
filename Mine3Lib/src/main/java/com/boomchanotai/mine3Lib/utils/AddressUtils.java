package com.boomchanotai.mine3Lib.utils;

public class AddressUtils {
    public static String addressShortener(String address) {
        return address.substring(0, 5) + "..." + address.substring(38);
    }
}
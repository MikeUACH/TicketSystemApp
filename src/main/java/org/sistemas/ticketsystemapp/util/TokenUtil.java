package org.sistemas.ticketsystemapp.util;


import java.util.UUID;

public class TokenUtil {

    public static String generate() {
        return UUID.randomUUID().toString();
    }
}

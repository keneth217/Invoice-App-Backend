package com.app.invoice.configs;

import java.time.LocalDateTime;
import java.util.UUID;

public class ActivationTokenUtil {

    public static String generateActivationToken(String schoolID) {
        return UUID.nameUUIDFromBytes(schoolID.getBytes()).toString();
    }

    public static LocalDateTime getTokenExpiryDate() {
        return LocalDateTime.now().plusDays(1);
    }

}

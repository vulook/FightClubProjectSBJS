package edu.cbsystematics.com.fightclubprojectsbjs.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExpirationManager {
    private static final int MINUTES_TO_EXPIRE = 180;

    // Sets the expiry time for Verification Code.
    public static LocalDateTime expiryDateTimeVerificationCode(LocalDateTime regDateTime) {
        return regDateTime.plusMinutes(MINUTES_TO_EXPIRE);
    }

    // Returns a formatted representation of the expiry time.
    public static String getExpiryDateTimeVerificationCode(LocalDateTime expiryDateTimeVerificationCode) {
        // Creates a DateTimeFormatter object with the specified pattern.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        return formatter.format(expiryDateTimeVerificationCode);
    }

    // Checks if token has expired.
    public static boolean isExpiredDateTimeVerificationCode(LocalDateTime regDateTime) {
        return LocalDateTime.now().isAfter(regDateTime.plusMinutes(MINUTES_TO_EXPIRE));
    }

}
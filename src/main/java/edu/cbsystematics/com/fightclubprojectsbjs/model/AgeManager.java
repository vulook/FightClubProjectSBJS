package edu.cbsystematics.com.fightclubprojectsbjs.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class AgeManager {

    private static final int PERMITTED_REGISTRATION_AGE = 18;

    // This method checks if the user is of a permitted age to register
    public static boolean isPermittedAgeForRegistration(LocalDate birthDate) {
        int userAge = calculateUserAge(birthDate);
        System.out.println("User Age: " + userAge);
        return userAge < PERMITTED_REGISTRATION_AGE;
    }

    // This method calculates the age of the current user
    private static int calculateUserAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }

}
package com.corrot.room.utils;

public class WeightUnitsUtils {

    public static float kgToLbs(float kg) {
        // way to round to 2 decimal places
        return (float)(Math.round((kg * 2.20462262) * 100.0) / 100.0);
    }

    public static float lbsToKg(float lbs) {
        // way to round to 2 decimal places
        return (float)(Math.round((lbs * 0.45359237) * 100.0) / 100.0);
    }
}

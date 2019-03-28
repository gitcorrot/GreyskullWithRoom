package com.corrot.room.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PreferencesManager {

    private final static String PREFS_NAME = "com.corrot";
    private final static String PREFS_EXERCISES_KEY = "exercises";
    private final static String PREFS_FIRST_START_KEY = "first start";

    private static PreferencesManager instance;
    private static SharedPreferences mPreferences;

    private PreferencesManager(Context context) {
        mPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void init(Context context) {
        if (instance == null) {
            instance = new PreferencesManager(context);
        }
    }

    public static synchronized PreferencesManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("You have to initiate shared preferences first!");
        }
        return instance;
    }

    //---------------------------------------- UTILS -------------------------------------------//

    public static boolean isFirstStart() {
        return mPreferences.getBoolean(PREFS_FIRST_START_KEY, true);
    }

    public static void setFirstStart(boolean isFirst) {
        mPreferences.edit()
                .putBoolean(PREFS_FIRST_START_KEY, isFirst)
                .apply();
    }

    public static void saveExercises(String[] exercises) {
        if (exercises != null) {
            Set<String> set = new HashSet<>();
            Collections.addAll(set, exercises);

            mPreferences.edit()
                    .putStringSet(PREFS_EXERCISES_KEY, set)
                    .apply();
        }
    }

    private static void saveExercises(Set<String> exercises) {
        if (exercises != null) {
            mPreferences.edit()
                    .putStringSet(PREFS_EXERCISES_KEY, exercises)
                    .apply();
        }
    }

    public static String[] getExercises() {
        Set<String> exercises = mPreferences.getStringSet(PREFS_EXERCISES_KEY, null);
        if (exercises == null) {
            return null;
        }
        return new ArrayList<>(exercises).toArray(new String[exercises.size()]);
    }

    public static void addExercise(String exercise) {
        Set<String> exercises = mPreferences.getStringSet(PREFS_EXERCISES_KEY, null);
        if (exercises != null && exercise != null) {
            exercises.add(exercise);
            saveExercises(exercises);
        }
    }
}

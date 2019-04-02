package com.corrot.room.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArraySet;

import com.corrot.room.BodyWeightItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PreferencesManager {

    private final static String PREFS_NAME = "com.corrot";
    private final static String PREFS_EXERCISES_KEY = "exercises";
    private final static String PREFS_BODY_WEIGHTS_KEY = "body weights";
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

    private static void saveBodyWeights(Set<String> bodyWeights) {
        mPreferences.edit()
                .putStringSet(PREFS_BODY_WEIGHTS_KEY, bodyWeights)
                .apply();
    }

    public static Set<String> getBodyWeights() {
        Set<String> bodyWeights = mPreferences.getStringSet(PREFS_BODY_WEIGHTS_KEY, null);
        if (bodyWeights != null) {
            return sortBodyWeights(bodyWeights);
        } else {
            return new HashSet<>();
        }
    }

    private static Set<String> sortBodyWeights(Set<String> bodyWeights) {
        List<BodyWeightItem> weightsList = new ArrayList<>();
        for (String s : bodyWeights) {
            weightsList.add(new BodyWeightItem(s));
        }
        Collections.sort(weightsList);
        Set<String> sorted = new LinkedHashSet<>();

        for (BodyWeightItem i : weightsList) {
            sorted.add(i.itemToString());
        }
        return sorted;
    }

    public static void addBodyWeight(String bodyWeight, String date) {
        Set<String> bodyWeights = getBodyWeights();
        String bodyWeightRecord = bodyWeight + "_" + date;
        bodyWeights.add(bodyWeightRecord);
        saveBodyWeights(bodyWeights);
    }
}

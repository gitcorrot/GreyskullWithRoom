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
    public final static String PREFS_BODY_WEIGHTS_KEY = "body weights";
    private final static String PREFS_FIRST_START_KEY = "first start";

    private static PreferencesManager instance;
    private final SharedPreferences mPreferences;

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

    public boolean isFirstStart() {
        return mPreferences.getBoolean(PREFS_FIRST_START_KEY, true);
    }

    public void setFirstStart(boolean isFirst) {
        mPreferences.edit()
                .putBoolean(PREFS_FIRST_START_KEY, isFirst)
                .apply();
    }

    //-------------------------------------- EXERCISES -------------------------------------------//

    public void saveExercises(String[] exercises) {
        if (exercises != null) {
            Set<String> set = new HashSet<>();
            Collections.addAll(set, exercises);

            mPreferences.edit()
                    .putStringSet(PREFS_EXERCISES_KEY, set)
                    .apply();
        }
    }

    private void saveExercises(Set<String> exercises) {
        if (exercises != null) {
            mPreferences.edit()
                    .putStringSet(PREFS_EXERCISES_KEY, exercises)
                    .apply();
        }
    }

    public String[] getExercises() {
        Set<String> exercises = mPreferences.getStringSet(PREFS_EXERCISES_KEY, null);
        if (exercises == null) {
            return null;
        }
        return new ArrayList<>(exercises).toArray(new String[exercises.size()]);
    }

    public void addExercise(String exercise) {
        Set<String> exercises = mPreferences.getStringSet(PREFS_EXERCISES_KEY, null);
        if (exercises != null && exercise != null) {
            exercises.add(exercise);
            saveExercises(exercises);
        }
    }

    //------------------------------------- BODY WEIGHT -----------------------------------------//

    private void saveBodyWeights(Set<String> bodyWeights) {
        mPreferences.edit()
                .putStringSet(PREFS_BODY_WEIGHTS_KEY, bodyWeights)
                .apply();
    }

    private Set<String> getBodyWeightsSet() {
        Set<String> bodyWeights = mPreferences.getStringSet(PREFS_BODY_WEIGHTS_KEY, null);
        if (bodyWeights != null) {
            return sortBodyWeightsSet(bodyWeights);
        } else {
            return new HashSet<>();
        }
    }

    private Set<String> sortBodyWeightsSet(Set<String> bodyWeights) {
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

    public List<BodyWeightItem> getBodyWeightsList() {
        Set<String> bodyWeights = getBodyWeightsSet();
        if (bodyWeights != null) {
            return sortBodyWeightsList(bodyWeights);
        } else {
            return new ArrayList<>();
        }
    }

    private List<BodyWeightItem> sortBodyWeightsList(Set<String> bodyWeights) {
        List<BodyWeightItem> weightsList = new ArrayList<>();
        for (String s : bodyWeights) {
            weightsList.add(new BodyWeightItem(s));
        }
        Collections.sort(weightsList);
        return weightsList;
    }

    public void addBodyWeight(String bodyWeight, String date) {
        Set<String> bodyWeights = getBodyWeightsSet();
        String bodyWeightRecord = bodyWeight + "_" + date;
        bodyWeights.add(bodyWeightRecord);
        saveBodyWeights(bodyWeights);
    }
}

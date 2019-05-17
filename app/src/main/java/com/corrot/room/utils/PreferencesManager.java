package com.corrot.room.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.corrot.room.BodyWeightItem;
import com.corrot.room.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PreferencesManager {

    private final static String PREFS_NAME = "com.corrot key";
    public final static String PREFS_EXERCISES_KEY = "exercises key";
    public final static String PREFS_BODY_WEIGHTS_KEY = "body weights key";
    private final static String PREFS_FIRST_START_KEY = "first start key";
    private final static String PREFS_UNITS_KEY = "kg lbs key";

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

    public void registerListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public static synchronized PreferencesManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("You have to initiate shared preferences first!");
        }
        return instance;
    }

    //---------------------------------------- UTILS -------------------------------------------//

    public boolean isFirstStart(Context context) {
        boolean isFirst = mPreferences.getBoolean(PREFS_FIRST_START_KEY, true);
        if (isFirst) {
            initFirstStart(context);
            return true;
        } else
            return false;
    }

    private void initFirstStart(Context context) {
        mPreferences.edit()
                .putBoolean(PREFS_FIRST_START_KEY, false)
                .apply();
        Set<String> exercises = new HashSet<>();
        exercises.add(context.getString(R.string.squats));
        exercises.add(context.getString(R.string.deadlift));
        exercises.add(context.getString(R.string.bench_press));
        exercises.add(context.getString(R.string.barbell_row));
        exercises.add(context.getString(R.string.pull_ups));
        exercises.add(context.getString(R.string.overhead_press));
        saveExercises(exercises);

        setUnitSystem("kg");
    }

    public String getUnitSystem() {
        return mPreferences.getString(PREFS_UNITS_KEY, "error");
    }

    public void setUnitSystem(String units) {
        mPreferences.edit()
                .putString(PREFS_UNITS_KEY, units)
                .apply();
    }

    //--------------------------------EXERCISES' NAMES -------------------------------------------//

    /*public void saveExercises(String[] exercises) {
        if (exercises != null) {
            Set<String> set = new HashSet<>(Arrays.asList(exercises));
            //Collections.addAll(set, exercises);

            mPreferences.edit()
                    .putStringSet(PREFS_EXERCISES_KEY, set)
                    .apply();
        }
    }*/

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
        Set<String> exercisesCopy = new HashSet<>(exercises); // MOST IMPORTANT THING - MAKE COPY OF SET
        exercisesCopy.add(exercise);
        saveExercises(exercisesCopy);
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
            return new LinkedHashSet<>(sortBodyWeightsSet(bodyWeights)); // Important to make copy!
        } else {
            return new LinkedHashSet<>();
        }
    }

    public List<BodyWeightItem> getBodyWeightsList() {
        Set<String> bodyWeights = getBodyWeightsSet();
        if (!bodyWeights.isEmpty()) {
            List<BodyWeightItem> list = new ArrayList<>();
            for (String s : bodyWeights) {
                switch (getUnitSystem()) {
                    case "kg": {
                        list.add(new BodyWeightItem(s));
                        break;
                    }
                    case "lbs": {
                        BodyWeightItem bwi = new BodyWeightItem(s);
                        bwi.weight = WeightUnitsUtils.kgToLbs(bwi.weight);
                        list.add(bwi);
                        break;
                    }
                }
            }
            return list; // It is already sorted.
        } else {
            return new ArrayList<>();
        }
    }

    private Set<String> sortBodyWeightsSet(Set<String> bodyWeights) {
        List<BodyWeightItem> weightsList = new ArrayList<>();
        for (String s : bodyWeights) {
            weightsList.add(new BodyWeightItem(s));
        }
        // Sort by date.
        Collections.sort(weightsList);
        Set<String> sorted = new LinkedHashSet<>();

        for (BodyWeightItem i : weightsList) {
            sorted.add(i.itemToString());
        }
        return sorted;
    }

    public void addBodyWeight(String bodyWeight, String date) {
        Set<String> bodyWeights = getBodyWeightsSet();
        String bodyWeightRecord = bodyWeight + "_" + date;
        bodyWeights.add(bodyWeightRecord);
        saveBodyWeights(bodyWeights);
    }

    public void removeBodyWeight(BodyWeightItem itemToRemove) {
        List<BodyWeightItem> list = getBodyWeightsList();
        Set<String> set = new LinkedHashSet<>();
        for (BodyWeightItem item : list) {
            if (item.date.getTime() == itemToRemove.date.getTime()) {
                if (item.weight != itemToRemove.weight) {
                    set.add(item.itemToString());
                }
            } else {
                set.add(item.itemToString());
            }
        }
        saveBodyWeights(set);
    }
}

package com.corrot.room.utils;

import com.corrot.room.ExerciseItem;
import com.corrot.room.ExerciseSetItem;
import com.corrot.room.RoutineExerciseItem;
import com.corrot.room.db.entity.Exercise;
import com.corrot.room.db.entity.Routine;

import java.util.ArrayList;
import java.util.List;

public class EntityUtils {

    public static List<ExerciseItem> getExerciseItems(List<Exercise> exercises) {
        List<ExerciseItem> exerciseItems = new ArrayList<>();
        for (int i = 0; i < exercises.size(); i++) {
            exerciseItems.add(new ExerciseItem(exercises.get(i).id, i, exercises.get(i).name));
        }
        return exerciseItems;
    }

    public static List<ExerciseSetItem> getExerciseSetItems(List<Exercise> exercises, String unit) {
        List<ExerciseSetItem> exerciseSetItems = new ArrayList<>();
        for (int i = 0; i < exercises.size(); i++) {
            Exercise e = exercises.get(i);
            if (e.weights_kg.size() == e.reps.size()) {
                switch (unit) {
                    case "kg": {
                        for (int j = 0; j < e.weights_kg.size(); j++) {
                            int reps = e.reps.get(j);
                            float weight = e.weights_kg.get(j);        // i => adapter position
                            exerciseSetItems.add(new ExerciseSetItem(i, weight, reps));
                        }
                        break;
                    }
                    case "lbs": {
                        for (int j = 0; j < e.weights_lbs.size(); j++) {
                            int reps = e.reps.get(j);
                            float weight = e.weights_lbs.get(j);        // i => adapter position
                            exerciseSetItems.add(new ExerciseSetItem(i, weight, reps));
                        }
                        break;
                    }
                }
            }
        }
        return exerciseSetItems;
    }

    public static List<RoutineExerciseItem> getRoutineExercises(Routine routine) {
        List<RoutineExerciseItem> exerciseItems = new ArrayList<>();
        for (int i = 0; i < routine.exercises.size(); i++) {
            RoutineExerciseItem item = new RoutineExerciseItem();
            item.name = routine.exercises.get(i);
            item.sets = routine.sets.get(i);
            item.position = i;
            exerciseItems.add(item);
        }
        return exerciseItems;
    }

    public static List<ExerciseItem> getExerciseItemsFromRoutine(Routine routine) {
        if (routine == null) return null;
        List<ExerciseItem> exerciseItems = new ArrayList<>();
        for (int i = 0; i < routine.exercises.size(); i++) {
            exerciseItems.add(new ExerciseItem(i, routine.exercises.get(i)));
        }
        return exerciseItems;
    }
}

package com.corrot.room.utils;

import com.corrot.room.ExerciseItem;
import com.corrot.room.ExerciseSetItem;
import com.corrot.room.db.entity.Exercise;

import java.util.ArrayList;
import java.util.List;

public class EntityUtils {

    public static List<ExerciseItem> getExerciseItems(List<Exercise> exercises) {
        List<ExerciseItem> exerciseItems = new ArrayList<>();
        for(Exercise e : exercises) {
            exerciseItems.add(new ExerciseItem(e.id, e.name));
        }
        return exerciseItems;
    }

    public static List<ExerciseSetItem> getExerciseSetItems(List<Exercise> exercises) {
        List<ExerciseSetItem> exerciseSetItems = new ArrayList<>();
        for(Exercise e : exercises) {
            if(e.weights.size() == e.reps.size()) {
                for (int i = 0; i < e.weights.size(); i++) {
                    int reps = e.reps.get(i);
                    float weight = e.weights.get(i);
                    exerciseSetItems.add(new ExerciseSetItem(e.id, weight, reps));
                }
            }
        }
        return exerciseSetItems;
    }
}

package com.corrot.room.utils;

import android.util.Log;

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
        /*for(Exercise e : exercises) {
            exerciseItems.add(new ExerciseItem(e.id, e.name));
        }*/
        return exerciseItems;
    }

    public static List<ExerciseSetItem> getExerciseSetItems(List<Exercise> exercises) {
        List<ExerciseSetItem> exerciseSetItems = new ArrayList<>();

        for (int i = 0; i < exercises.size(); i++) {
            Exercise e = exercises.get(i);
            if (e.weights.size() == e.reps.size()) {

                for (int j = 0; j < e.weights.size(); j++) {

                    int reps = e.reps.get(j);
                    float weight = e.weights.get(j);        // i => adapter position
                    exerciseSetItems.add(new ExerciseSetItem(i, weight, reps));
                }
            }
        }

        /*for(Exercise e : exercises) {
            if(e.weights.size() == e.reps.size()) {
                for (int i = 0; i < e.weights.size(); i++) {
                    int reps = e.reps.get(i);
                    float weight = e.weights.get(i);
                    exerciseSetItems.add(new ExerciseSetItem(e.id, weight, reps));
                }
            }
        }*/

        return exerciseSetItems;
    }

    public static List<RoutineExerciseItem> getRoutineExercises(Routine routine) {
        List<RoutineExerciseItem> exerciseItems = new ArrayList<>();

        for (int i = 0; i < routine.exercises.size(); i++) {
            // Parse for example "Squats = 2 sets." into object
            RoutineExerciseItem item = new RoutineExerciseItem();
            try {
                String[] name = routine.exercises.get(i).split(" - ");
                item.name = name[0];
                try {
                    String[] sets = name[1].split(" ");
                    item.sets = Integer.parseInt(sets[0]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    Log.e("RoutinesAdapter", "Can't find ' ' in String!");
                    item.sets = 0;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.e("RoutinesAdapter", "Can't find ' - ' in String!");
                item.name = "Name";
                item.sets = 0;
            }
            item.position = i;
            exerciseItems.add(item);
        }
        return exerciseItems;
    }

    public static List<ExerciseItem> getRoutineWorkoutExerciseItems(Routine routine) {
        if (routine == null) return null;

        List<ExerciseItem> exerciseItems = new ArrayList<>();

        for (int i = 0; i < routine.exercises.size(); i++) {
            // Parse for example "Squats = 2 sets." into object
            // TODO: Find way to pass routine number of sets.
            String name;
            try {
                String[] str = routine.exercises.get(i).split(" - ");
                name = str[0];
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.e("EntityUtils", "Can't find ' - ' in String!");
                name = "Null";
            }
            exerciseItems.add(new ExerciseItem(i, name));
        }
        return exerciseItems;
    }
}

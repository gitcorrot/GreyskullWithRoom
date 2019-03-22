package com.corrot.room;

public class ExerciseSetItem {

    public int exerciseId;
    public int reps;
    public float weight;

    public ExerciseSetItem(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public ExerciseSetItem(int exerciseId, float weight, int reps) {
        this.exerciseId = exerciseId;
        this.weight = weight;
        this.reps = reps;
    }
}

package com.corrot.room;

public class ExerciseSetItem {

    public int exercisePosition;
    public int reps;
    public float weight;

    public ExerciseSetItem(int exerciseId) {
        this.exercisePosition = exerciseId;
    }

    public ExerciseSetItem(int exercisePosition, float weight, int reps) {
        this.exercisePosition = exercisePosition;
        this.weight = weight;
        this.reps = reps;
    }
}

package com.corrot.room;

public class ExerciseItem {

    public int exerciseId;
    public int position;
    public String name;

    public ExerciseItem(String name) {
        this.name = name;
    }

    public ExerciseItem(int position, String name) {
        this.position = position;
        this.name = name;
    }

    public ExerciseItem(int exerciseId, int position, String name) {
        this.exerciseId = exerciseId;
        this.position = position;
        this.name = name;
    }
}

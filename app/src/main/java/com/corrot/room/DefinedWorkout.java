package com.corrot.room;

public class DefinedWorkout {
    public String label;
    public String exercises;
    public boolean expanded;

    public DefinedWorkout(String label, String exercises) {
        this.label = label;
        this.exercises = exercises;
        expanded = false;
    }
}

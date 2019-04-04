package com.corrot.room;

import java.util.List;

public class DefinedWorkout {
    public String label;
    public List<String> exercises;
    public boolean expanded;

    public DefinedWorkout(String label, List<String> exercises) {
        this.label = label;
        this.exercises = exercises;
        expanded = false;
    }
}

package com.corrot.room;

public class DefinedWorkoutExerciseItem {
    public String name;
    public int sets;
    public int position;

    public DefinedWorkoutExerciseItem() {
        name = "";
        sets = 0;
        position = 0;
    }

    public DefinedWorkoutExerciseItem(String name, int sets, int position) {
        this.name = name;
        this.sets = sets;
        this.position = position;
    }
}

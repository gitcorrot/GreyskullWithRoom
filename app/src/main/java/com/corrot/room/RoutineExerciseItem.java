package com.corrot.room;

public class RoutineExerciseItem {
    public String name;
    public int sets;
    public int position;

    public RoutineExerciseItem() {
        name = "";
        sets = 0;
        position = 0;
    }

    public RoutineExerciseItem(String name, int sets, int position) {
        this.name = name;
        this.sets = sets;
        this.position = position;
    }
}

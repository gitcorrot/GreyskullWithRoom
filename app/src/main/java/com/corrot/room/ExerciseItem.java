package com.corrot.room;

import java.util.List;

public class ExerciseItem {

    public String name;
    public List<ExerciseSetItem> sets;

    public ExerciseItem(String name, List<ExerciseSetItem> sets) {
        this.name = name;
        this.sets = sets;
    }

    public ExerciseItem(List<ExerciseSetItem> sets) {
        this.sets = sets;
    }
}

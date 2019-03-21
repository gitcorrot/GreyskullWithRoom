package com.corrot.room;

public class ExerciseItem {

    public int id;
    public String name;
    //public List<ExerciseSetItem> sets;

    public ExerciseItem(int id, String name) {
        this.id = id;
        this.name = name;
        //this.sets = new ArrayList<>();
    }

    public ExerciseItem(String name) {
        this.name = name;
        //this.sets = new ArrayList<>();
    }

    /*public ExerciseItem(String name, List<ExerciseSetItem> sets) {
        this.name = name;
        this.sets = sets;
    }*/
}

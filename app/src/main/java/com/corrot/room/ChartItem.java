package com.corrot.room;

import java.util.Date;

public class ChartItem implements Comparable<ChartItem>{

    public float weight;
    public Date date;
    public String label;

    public ChartItem(float weight, Date date, String label) {
        this.weight = weight;
        this.date = date;
        this.label = label;
    }

    @Override
    public int compareTo(ChartItem o) {
        if (this.date == null || o.date == null) {
            return 0;
        }
        return o.date.compareTo(this.date);
    }
}

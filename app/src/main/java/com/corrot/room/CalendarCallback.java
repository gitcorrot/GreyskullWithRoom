package com.corrot.room;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

public interface CalendarCallback {
    void onSuccess(List<CalendarDay> days);
}

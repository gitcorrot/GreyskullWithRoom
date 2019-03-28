package com.corrot.room.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.corrot.room.R;
import com.corrot.room.db.entity.Exercise;
import com.corrot.room.utils.ChartUtils;
import com.corrot.room.viewmodel.ExerciseViewModel;
import com.corrot.room.viewmodel.WorkoutViewModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StatsFragment extends Fragment {

    LineChart lineChart;
    String name = "Overhead Press";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ExerciseViewModel mExerciseViewModel =
                ViewModelProviders.of(this).get(ExerciseViewModel.class);
        WorkoutViewModel mWorkoutViewModel =
                ViewModelProviders.of(this).get(WorkoutViewModel.class);

        lineChart = view.findViewById(R.id.fragment_statistics_line_chart);

        XAxis x = lineChart.getXAxis();
        x.setAvoidFirstLastClipping(true);
        x.setDrawGridLines(true);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setValueFormatter(new ChartUtils.DateAxisValueFormatter());
        x.setLabelCount(5,true);
        x.setTextSize(8f);

        YAxis yLeft = lineChart.getAxisLeft();
        yLeft.setDrawGridLines(true);
        yLeft.setDrawAxisLine(true);

        YAxis yRight = lineChart.getAxisRight();
        yRight.setDrawGridLines(false);
        yRight.setDrawLabels(false);

        Legend legend = lineChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setForm(Legend.LegendForm.LINE);

        List<Entry> entries = new ArrayList<>();

        try {
            List<Exercise> exercises = mExerciseViewModel.getAllExercises(name);
            for (Exercise e : exercises) {
                Date date = mWorkoutViewModel.getWorkoutById(e.workoutId).workoutDate;
                float max = Collections.max(e.weights);
                entries.add(new Entry(date.getTime(), max));
            }
            Collections.sort(entries, new EntryXComparator());
        } catch (ExecutionException e) {
            Log.e("StatsFragment", e.getMessage());
        } catch (InterruptedException e) {
            Log.e("StatsFragment", e.getMessage());
        }

        LineDataSet lineDataSet = new LineDataSet(entries, name);
        lineDataSet.setColor(0xFFD81B60);   // color accent
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setLineWidth(2.5f);
        lineDataSet.setCircleColor(0xFFD81B60);
        lineDataSet.setCircleRadius(5);
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setCircleHoleRadius(3.0f);
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);

        LineData lineData = new LineData(lineDataSet);
        lineData.setHighlightEnabled(false);
        lineData.setDrawValues(false);
        lineChart.setData(lineData);
        lineChart.setDescription(null);
        lineChart.setNoDataText("No data for " + name);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.setHighlightPerTapEnabled(false);
        lineChart.animateY(750, Easing.EaseOutCubic);
        lineChart.invalidate();
    }
}

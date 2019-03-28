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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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
        // Convert long to date on X-axis
        XAxis x = lineChart.getXAxis();
        x.setValueFormatter(new ChartUtils.DateAxisValueFormatter());
        List<Entry> entries = new ArrayList<>();

        try {
            // Get all exercises by name
            List<Exercise> exercises = mExerciseViewModel.getAllExercises(name);
            // Get date of exercise
            for (Exercise e : exercises) {
                Date date = mWorkoutViewModel.getWorkoutById(e.workoutId).workoutDate;
                Log.d("StatsFragment", "CHART DATE: " + date.toString());
                // Find biggest weight in workout
                float max = Collections.max(e.weights);
                // Add entry
                entries.add(new Entry(date.getTime(), max));
            }
        } catch (ExecutionException e) {
            Log.e("StatsFragment", e.getMessage());
        } catch (InterruptedException e) {
            Log.e("StatsFragment", e.getMessage());
        }

        LineDataSet lineDataSet = new LineDataSet(entries, name);
        lineDataSet.setColor(Color.CYAN);
        lineDataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }
}

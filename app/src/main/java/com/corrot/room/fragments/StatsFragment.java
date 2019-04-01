package com.corrot.room.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.corrot.room.R;
import com.corrot.room.db.entity.Exercise;
import com.corrot.room.utils.ChartUtils;
import com.corrot.room.utils.PreferencesManager;
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
    Spinner nameSpinner;
    String name;
    String[] exercisesNames;
    List<Entry> entries;

    ExerciseViewModel mExerciseViewModel;
    WorkoutViewModel mWorkoutViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mExerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        mWorkoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);
        exercisesNames = PreferencesManager.getExercises();

        /*if (savedInstanceState != null) {
            this.save
        }*/

        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lineChart = view.findViewById(R.id.fragment_statistics_line_chart);
        nameSpinner = view.findViewById(R.id.fragment_statistics_name_spinner);

        if (getContext() != null && exercisesNames != null) {
            ArrayAdapter namesAdapter = new ArrayAdapter<>(
                    getContext(),
                    R.layout.spinner_item,
                    R.id.spinner_text_view, exercisesNames
            );
            namesAdapter.setDropDownViewResource(R.layout.spinner_item);
            nameSpinner.setAdapter(namesAdapter);
        }

        nameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (exercisesNames != null) {
                    name = exercisesNames[position];
                    showChart();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (exercisesNames != null) {
                    name = exercisesNames[0];
                    showChart();
                }
            }
        });
    }

    private void showChart() {
        entries = new ArrayList<>();
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

        if (!entries.isEmpty()) {
            int colorAccent = 0;
            if (getContext() != null) {
                colorAccent = ContextCompat.getColor(getContext(), R.color.colorAccent);
            }
            XAxis x = lineChart.getXAxis();
            x.setAvoidFirstLastClipping(true);
            x.setDrawGridLines(true);
            x.setPosition(XAxis.XAxisPosition.BOTTOM);
            x.setValueFormatter(new ChartUtils.DateAxisValueFormatter());
            x.setLabelCount(5, true);
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

            LineDataSet lineDataSet = new LineDataSet(entries, name);
            lineDataSet.setColor(colorAccent);   // color accent
            lineDataSet.setValueTextColor(Color.BLACK);
            lineDataSet.setLineWidth(2.5f);
            lineDataSet.setCircleColor(colorAccent);
            lineDataSet.setCircleRadius(5);
            lineDataSet.setDrawCircleHole(true);
            lineDataSet.setCircleHoleRadius(3.0f);
            lineDataSet.setMode(LineDataSet.Mode.LINEAR);

            LineData lineData = new LineData(lineDataSet);
            lineData.setHighlightEnabled(false);
            lineData.setDrawValues(false);

            lineChart.setData(lineData);
            lineChart.setTouchEnabled(true);
            lineChart.setDragEnabled(true);
            lineChart.setScaleEnabled(false);
            lineChart.setHighlightPerTapEnabled(false);
            lineChart.animateY(750, Easing.EaseOutCubic);
            lineChart.setDescription(null);
            lineChart.invalidate();
        } else {
            lineChart.setNoDataText("No data for " + name);
            lineChart.setData(null);
            lineChart.invalidate();
        }
    }

    // TODO: Learn how to use saveInstanceState
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("asdasd", "onSaveInstanceState");
        outState.putInt("saved spinner item", nameSpinner.getSelectedItemPosition());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        Log.d("asdasd", "onViewStateRestored");

        if (savedInstanceState != null) {
            int savedPosition = savedInstanceState.getInt("saved spinner item", -1);
            nameSpinner.setSelection(savedPosition);
        }
    }
}

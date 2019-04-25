package com.corrot.room.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.corrot.room.R;
import com.corrot.room.WorkoutCallback;
import com.corrot.room.db.entity.Exercise;
import com.corrot.room.db.entity.Workout;
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

public class StatsFragment extends Fragment
        implements SharedPreferences.OnSharedPreferenceChangeListener, WorkoutCallback{

    private LineChart mLineChart;
    private Spinner mNameSpinner;
    private String mName;
    private String[] mExercisesNames;
    private List<Exercise> mExercises;

    private WorkoutViewModel mWorkoutViewModel;
    private PreferencesManager pm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mExercises = new ArrayList<>();
        pm = PreferencesManager.getInstance();
        mExercisesNames = pm.getExercises();

        ExerciseViewModel mExerciseViewModel =
                ViewModelProviders.of(this).get(ExerciseViewModel.class);
        mWorkoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);

        mExerciseViewModel.getAllExercises().observe(this, exercises -> {
            mExercises = exercises;
            updateEntries();
        });
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLineChart = view.findViewById(R.id.fragment_statistics_line_chart);
        mNameSpinner = view.findViewById(R.id.fragment_statistics_name_spinner);

        updateSpinnerAdapter();

        mNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mExercisesNames != null) {
                    mName = mExercisesNames[position];
                    updateEntries();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (mExercisesNames != null) {
                    mName = mExercisesNames[0];
                    updateEntries();
                }
            }
        });
    }
    private void updateEntries() {
        // TODO: write function in viewModel to get all exercises by name
        List<Entry> entries = new ArrayList<>();
        for (Exercise e : mExercises) {
            if (e.name.equals(mName)) {
                mWorkoutViewModel.getWorkoutById(e.workoutId, workout -> {
                    Date date = workout.workoutDate;
                    float max = Collections.max(e.weights);
                    entries.add(new Entry(date.getTime(), max));
                });
            }
        }
        updateChart(entries);
    }

    private void updateChart(List<Entry> entries) {

        if (!entries.isEmpty()) {
            int colorAccent = 0;
            if (getContext() != null) {
                colorAccent = ContextCompat.getColor(getContext(), R.color.colorAccent);
            }
            XAxis x = mLineChart.getXAxis();
            x.setAvoidFirstLastClipping(true);
            x.setDrawGridLines(true);
            x.setPosition(XAxis.XAxisPosition.BOTTOM);
            x.setValueFormatter(new ChartUtils.DateAxisValueFormatter());
            x.setLabelCount(5, true);
            x.setTextSize(8f);

            YAxis yLeft = mLineChart.getAxisLeft();
            yLeft.setDrawGridLines(true);
            yLeft.setDrawAxisLine(true);

            YAxis yRight = mLineChart.getAxisRight();
            yRight.setDrawGridLines(false);
            yRight.setDrawLabels(false);

            Legend legend = mLineChart.getLegend();
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            legend.setDrawInside(false);
            legend.setForm(Legend.LegendForm.LINE);

            LineDataSet lineDataSet = new LineDataSet(entries, mName);
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

            mLineChart.setData(lineData);
            mLineChart.setTouchEnabled(true);
            mLineChart.setDragEnabled(true);
            mLineChart.setScaleEnabled(false);
            mLineChart.setHighlightPerTapEnabled(false);
            mLineChart.animateY(750, Easing.EaseOutCubic);
            mLineChart.setDescription(null);
            mLineChart.invalidate();
        } else {
            mLineChart.setNoDataText("No data for " + mName);
            mLineChart.setData(null);
            mLineChart.invalidate();
        }
    }

    private void updateSpinnerAdapter() {
        if (getContext() != null && mExercisesNames != null) {
            ArrayAdapter namesAdapter = new ArrayAdapter<>(
                    getContext(),
                    R.layout.spinner_item,
                    R.id.spinner_text_view,
                    mExercisesNames
            );
            namesAdapter.setDropDownViewResource(R.layout.spinner_item);
            mNameSpinner.setAdapter(namesAdapter);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("saved spinner item", mNameSpinner.getSelectedItemPosition());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            int savedPosition = savedInstanceState.getInt("saved spinner item", 0);
            mNameSpinner.setSelection(savedPosition);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PreferencesManager.PREFS_EXERCISES_KEY)) {
            mExercisesNames = pm.getExercises();
            updateSpinnerAdapter();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        pm.registerListener(this);
        updateEntries();
    }

    @Override
    public void onPause() {
        super.onPause();
        pm.unregisterListener(this);
    }

    @Override
    public void onSuccess(Workout workout) {

    }
}

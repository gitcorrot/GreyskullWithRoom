package com.corrot.room.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.corrot.room.BodyWeightItem;
import com.corrot.room.dialogs.NewBodyWeightDialog;
import com.corrot.room.R;
import com.corrot.room.adapters.BodyWeightsAdapter;
import com.corrot.room.utils.ChartUtils;
import com.corrot.room.utils.PreferencesManager;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BodyFragment extends Fragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private LineChart mLineChart;
    private PreferencesManager pm;
    private BodyWeightsAdapter bodyWeightsAdapter; // it should be final

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        pm = PreferencesManager.getInstance();

        return inflater.inflate(R.layout.fragment_body, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mLineChart = view.findViewById(R.id.fragment_body_chart);
        RecyclerView bodyWeightRecyclerView = view.findViewById(R.id.fragment_body_recycler_view);
        MaterialButton addWeightButton = view.findViewById(R.id.fragment_body_add_button);

        bodyWeightsAdapter = new BodyWeightsAdapter(this.getContext());
        bodyWeightRecyclerView.setAdapter(bodyWeightsAdapter);
        bodyWeightRecyclerView.setItemAnimator(null);
        bodyWeightRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        bodyWeightsAdapter.setBodyWeights(pm.getBodyWeightsList());

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder vh, int i) {
                BodyWeightItem item = bodyWeightsAdapter.getBodyWeightItemAt(vh.getAdapterPosition());
                pm.removeBodyWeight(item);
            }
        }).attachToRecyclerView(bodyWeightRecyclerView);

        addWeightButton.setOnClickListener(v ->
                showNewBodyWeightDialog(v.getContext(), getFragmentManager()));
    }

    private void showNewBodyWeightDialog(Context context, FragmentManager fragmentManager) {
        if (getFragmentManager() != null) {
            NewBodyWeightDialog dialog = new NewBodyWeightDialog();
            dialog.show(fragmentManager, "new body weight dialog");
        } else {
            Toast.makeText(context, "Can't find FragmentManager!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PreferencesManager.PREFS_BODY_WEIGHTS_KEY)) {
            bodyWeightsAdapter.setBodyWeights(pm.getBodyWeightsList());
            showChart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        pm.registerListener(this);
        showChart();
    }

    @Override
    public void onPause() {
        super.onPause();
        pm.unregisterListener(this);
    }

    private void showChart() {
        List<Entry> entries = new ArrayList<>();

        List<BodyWeightItem> bodyWeights = pm.getBodyWeightsList();
        for (BodyWeightItem i : bodyWeights) {
            entries.add(new Entry(i.date.getTime(), i.weight));
        }
        Collections.sort(entries, new EntryXComparator());

        if (!entries.isEmpty()) {
            int colorSecondary = 0;
            if (getContext() != null) {
                colorSecondary = ContextCompat.getColor(getContext(), R.color.colorSecondary);
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
            yRight.setDrawGridLines(true);
            yRight.setDrawLabels(false);

            Legend legend = mLineChart.getLegend();
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            legend.setDrawInside(false);
            legend.setForm(Legend.LegendForm.LINE);

            LineDataSet lineDataSet = new LineDataSet(entries, "Body weight history");
            lineDataSet.setColor(colorSecondary);
            lineDataSet.setValueTextColor(Color.BLACK);
            lineDataSet.setLineWidth(2.5f);
            lineDataSet.setCircleColor(colorSecondary);
            lineDataSet.setCircleRadius(5);
            lineDataSet.setDrawCircleHole(true);
            lineDataSet.setCircleHoleRadius(3.0f);
            lineDataSet.setMode(LineDataSet.Mode.LINEAR);

            LineData lineData = new LineData(lineDataSet);
            lineData.setDrawValues(false);

            mLineChart.setData(lineData);
            mLineChart.setDrawBorders(true);
            mLineChart.setBorderWidth(1.25f);
            mLineChart.setBorderColor(colorSecondary);
            mLineChart.setTouchEnabled(false);
            mLineChart.animateY(750, Easing.EaseOutCubic);
            mLineChart.setDescription(null);
            mLineChart.invalidate();
        } else {
            mLineChart.setNoDataText("Add body weight to see chart");
            mLineChart.setData(null);
            mLineChart.invalidate();
        }
    }
}

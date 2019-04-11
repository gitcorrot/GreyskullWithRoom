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
import com.corrot.room.NewBodyWeightDialog;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

public class BodyFragment extends Fragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private LineChart bodyWeightLineChart;
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
        bodyWeightLineChart = view.findViewById(R.id.fragment_body_chart);
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

        addWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewBodyWeightDialog(v.getContext(), getFragmentManager());
            }
        });
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
            int colorAccent = 0;
            if (getContext() != null) {
                colorAccent = ContextCompat.getColor(getContext(), R.color.colorAccent);
            }
            XAxis x = bodyWeightLineChart.getXAxis();
            x.setAvoidFirstLastClipping(true);
            x.setDrawGridLines(true);
            x.setPosition(XAxis.XAxisPosition.BOTTOM);
            x.setValueFormatter(new ChartUtils.DateAxisValueFormatter());
            x.setLabelCount(5, true);
            x.setTextSize(8f);

            YAxis yLeft = bodyWeightLineChart.getAxisLeft();
            yLeft.setDrawGridLines(true);
            yLeft.setDrawAxisLine(true);

            YAxis yRight = bodyWeightLineChart.getAxisRight();
            yRight.setDrawGridLines(false);
            yRight.setDrawLabels(false);

            Legend legend = bodyWeightLineChart.getLegend();
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            legend.setDrawInside(false);
            legend.setForm(Legend.LegendForm.LINE);

            LineDataSet lineDataSet = new LineDataSet(entries, "Body weight history");
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

            bodyWeightLineChart.setData(lineData);
            bodyWeightLineChart.setTouchEnabled(true);
            bodyWeightLineChart.setDragEnabled(true);
            bodyWeightLineChart.setScaleEnabled(false);
            bodyWeightLineChart.setHighlightPerTapEnabled(false);
            bodyWeightLineChart.animateY(750, Easing.EaseOutCubic);
            bodyWeightLineChart.setDescription(null);
            bodyWeightLineChart.invalidate();
        } else {
            bodyWeightLineChart.setNoDataText("Add body weight to see chart");
            bodyWeightLineChart.setData(null);
            bodyWeightLineChart.invalidate();
        }
    }
}

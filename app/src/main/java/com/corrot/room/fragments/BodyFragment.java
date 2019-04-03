package com.corrot.room.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.corrot.room.NewBodyWeightDialog;
import com.corrot.room.R;
import com.corrot.room.adapters.BodyWeightsAdapter;
import com.corrot.room.utils.PreferencesManager;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BodyFragment extends Fragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private LineChart bodyWeightLineChart;
    private RecyclerView bodyWeightRecyclerView;
    private MaterialButton addWeightButton;
    private PreferencesManager pm;

    private BodyWeightsAdapter bodyWeightsAdapter; // it should be final

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_body, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        bodyWeightLineChart = view.findViewById(R.id.fragment_body_chart);
        bodyWeightRecyclerView = view.findViewById(R.id.fragment_body_recycler_view);
        addWeightButton = view.findViewById(R.id.fragment_body_add_button);

        pm = PreferencesManager.getInstance();

        bodyWeightsAdapter = new BodyWeightsAdapter(this.getContext());
        bodyWeightRecyclerView.setAdapter(bodyWeightsAdapter);
        bodyWeightRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        bodyWeightsAdapter.setBodyWeights(pm.getBodyWeightsList());

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
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        pm.registerListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        pm.unregisterListener(this);
    }
}

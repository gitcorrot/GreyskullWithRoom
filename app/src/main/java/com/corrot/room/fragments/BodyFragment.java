package com.corrot.room.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.corrot.room.NewBodyWeightDialog;
import com.corrot.room.R;
import com.corrot.room.adapters.BodyWeightsAdapter;
import com.corrot.room.adapters.WorkoutsListAdapter;
import com.corrot.room.db.entity.Workout;
import com.corrot.room.utils.PreferencesManager;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BodyFragment extends Fragment {

    private LineChart bodyWeightLineChart;
    private RecyclerView bodyWeightRecyclerView;
    private MaterialButton addWeightButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_body, container, false);

        final RecyclerView recyclerView = view.findViewById(R.id.fragment_body_recycler_view);
        final BodyWeightsAdapter bodyWeightsAdapter =
                new BodyWeightsAdapter(this.getContext());

        recyclerView.setAdapter(bodyWeightsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        // TODO: on shared preferences changed
        bodyWeightsAdapter.setBodyWeights(PreferencesManager.getBodyWeightsList());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        bodyWeightLineChart = view.findViewById(R.id.fragment_body_chart);
        bodyWeightRecyclerView = view.findViewById(R.id.fragment_body_recycler_view);
        addWeightButton = view.findViewById(R.id.fragment_body_add_button);

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
}

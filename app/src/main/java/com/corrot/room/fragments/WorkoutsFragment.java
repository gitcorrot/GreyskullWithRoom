package com.corrot.room.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.corrot.room.NewDefinedWorkoutDialog;
import com.corrot.room.R;
import com.corrot.room.adapters.DefinedWorkoutsAdapter;
import com.corrot.room.db.entity.DefinedWorkout;
import com.corrot.room.viewmodel.DefinedWorkoutViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

public class WorkoutsFragment extends Fragment {

    private MaterialButton addWorkotuButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_workouts, container, false);
        addWorkotuButton = view.findViewById(R.id.fragment_workouts_add_new_button);

        addWorkotuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NewDefinedWorkoutDialog().show(getFragmentManager(), "Add");
            }
        });



        DefinedWorkoutViewModel definedWorkoutViewModel =
                ViewModelProviders.of(this).get(DefinedWorkoutViewModel.class);

        final RecyclerView recyclerView = view.findViewById(R.id.fragment_workouts_recycler_view);
        final DefinedWorkoutsAdapter workoutListAdapter =
                new DefinedWorkoutsAdapter(this.getContext(), this.getActivity());
        recyclerView.setAdapter(workoutListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        definedWorkoutViewModel.getAllWorkouts().observe(this, new Observer<List<DefinedWorkout>>() {
            @Override
            public void onChanged(List<DefinedWorkout> definedWorkouts) {
                workoutListAdapter.setDefinedWorkouts(definedWorkouts);
            }
        });

        return view;
    }
}

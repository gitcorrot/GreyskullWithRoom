package com.corrot.room.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.corrot.room.R;
import com.corrot.room.adapters.DefinedWorkoutsAdapter;
import com.corrot.room.db.entity.DefinedWorkout;
import com.corrot.room.viewmodel.DefinedWorkoutViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WorkoutsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_workouts, container, false);
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
                // TODO: DIFF UTILS
            }
        });

        List<String> ex= new ArrayList<>();
        ex.add("exercise 1");
        ex.add("exercise 2");
        ex.add("exercise 3");
        DefinedWorkout d1 = new DefinedWorkout("FIRST WORKOUT", ex);
        DefinedWorkout d2 = new DefinedWorkout("SECOND WORKOUT", ex);
        DefinedWorkout d3 = new DefinedWorkout("THIRD WORKOUT", ex);
        definedWorkoutViewModel.insertSingleWorkout(d1);
        definedWorkoutViewModel.insertSingleWorkout(d2);
        definedWorkoutViewModel.insertSingleWorkout(d3);

        return view;
    }
}

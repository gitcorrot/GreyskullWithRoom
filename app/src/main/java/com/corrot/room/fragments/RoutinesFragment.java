package com.corrot.room.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.corrot.room.R;
import com.corrot.room.adapters.RoutinesAdapter;
import com.corrot.room.dialogs.NewRoutineDialog;
import com.corrot.room.viewmodel.RoutineViewModel;
import com.google.android.material.button.MaterialButton;

public class RoutinesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_routines, container, false);
        MaterialButton addWorkoutButton = view.findViewById(R.id.fragment_routines_add_new_button);

        addWorkoutButton.setOnClickListener(v ->
                new NewRoutineDialog().show(requireFragmentManager(), "Add"));

        RoutineViewModel routineViewModel =
                ViewModelProviders.of(this).get(RoutineViewModel.class);

        final RecyclerView recyclerView = view.findViewById(R.id.fragment_routines_recycler_view);
        final RoutinesAdapter workoutListAdapter =
                new RoutinesAdapter(this.getContext(), this.getActivity());
        recyclerView.setAdapter(workoutListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        routineViewModel.getAllRoutines().observe(this, workoutListAdapter::setRoutines);
        return view;
    }
}

package com.corrot.room.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.corrot.room.NewRoutineDialog;
import com.corrot.room.R;
import com.corrot.room.adapters.RoutinesAdapter;
import com.corrot.room.db.entity.Routine;
import com.corrot.room.viewmodel.RoutineViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RoutinesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_routines, container, false);
        MaterialButton addWorkoutButton = view.findViewById(R.id.fragment_routines_add_new_button);

        addWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // getFragmentManager() ?
                new NewRoutineDialog().show(requireFragmentManager(), "Add");
            }
        });

        RoutineViewModel routineViewModel =
                ViewModelProviders.of(this).get(RoutineViewModel.class);

        final RecyclerView recyclerView = view.findViewById(R.id.fragment_routines_recycler_view);
        final RoutinesAdapter workoutListAdapter =
                new RoutinesAdapter(this.getContext(), this.getActivity());
        recyclerView.setAdapter(workoutListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        routineViewModel.getAllRoutines().observe(this, new Observer<List<Routine>>() {
            @Override
            public void onChanged(List<Routine> routines) {
                workoutListAdapter.setRoutines(routines);
            }
        });
        return view;
    }
}

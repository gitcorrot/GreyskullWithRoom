package com.corrot.room.fragments;

import android.app.AlertDialog;
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
import com.corrot.room.RoutineExerciseItem;
import com.corrot.room.adapters.RoutinesAdapter;
import com.corrot.room.db.entity.Routine;
import com.corrot.room.dialogs.NewRoutineDialog;
import com.corrot.room.utils.EntityUtils;
import com.corrot.room.viewmodel.NewRoutineViewModel;
import com.corrot.room.viewmodel.RoutineViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class RoutinesFragment extends Fragment implements RoutinesAdapter.RoutinesAdapterInterface {

    private RoutineViewModel mRoutineViewModel;
    private RoutinesAdapter routinesListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_routines, container, false);

        MaterialButton addRoutineButton = view.findViewById(R.id.fragment_routines_add_new_button);
        mRoutineViewModel = ViewModelProviders.of(this).get(RoutineViewModel.class);
        final RecyclerView recyclerView = view.findViewById(R.id.fragment_routines_recycler_view);
        routinesListAdapter = new RoutinesAdapter(getContext(), this);

        recyclerView.setAdapter(routinesListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        addRoutineButton.setOnClickListener(v ->
                new NewRoutineDialog().show(requireFragmentManager(), "Add"));

        mRoutineViewModel.getAllRoutines().observe(this, routinesListAdapter::setRoutines);
        return view;
    }

    // Routines adapter methods
    @Override
    public void onDeleteClick(Routine routine) {
        new AlertDialog.Builder(getContext(), R.style.ThemeOverlay_MaterialComponents_Dialog)
                .setTitle("Are you sure you want delete this routine?")
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Yes", (dialog, which) ->
                        mRoutineViewModel.deleteRoutine(routine))
                .create().show();
    }

    @Override
    public void onEditClick(Routine routine) {
        List<RoutineExerciseItem> exerciseItems = EntityUtils.getRoutineExercises(routine);

        NewRoutineViewModel mNewRoutineViewModel =
                ViewModelProviders.of(this).get(NewRoutineViewModel.class);
        mNewRoutineViewModel.init();
        mNewRoutineViewModel.setExercises(exerciseItems);

        NewRoutineDialog dialog = new NewRoutineDialog();
        Bundle args = new Bundle();
        args.putInt("id", routine.id);
        args.putString("label", routine.label);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "Edit");
    }
}

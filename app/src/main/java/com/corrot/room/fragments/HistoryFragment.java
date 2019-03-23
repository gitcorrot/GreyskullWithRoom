package com.corrot.room.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.corrot.room.db.entity.Workout;
import com.corrot.room.viewmodel.ExerciseViewModel;
import com.corrot.room.R;
import com.corrot.room.viewmodel.WorkoutViewModel;
import com.corrot.room.adapters.WorkoutsListAdapter;

import java.util.Calendar;
import java.util.List;

public class HistoryFragment extends Fragment {

    private Button buttonInsert;
    private Button buttonDelete;

    private WorkoutViewModel mWorkoutViewModel;
    private ExerciseViewModel mExerciseViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_history, container, false);

        // TODO: change names to match convention
        buttonInsert = view.findViewById(R.id.button);
        buttonDelete = view.findViewById(R.id.buttonDelete);

        mWorkoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);
        mExerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);

        final RecyclerView recyclerView = view.findViewById(R.id.workoutsRecyclerView);
        final WorkoutsListAdapter workoutListAdapter = new WorkoutsListAdapter(view.getContext());//not sure if it;s ok
        recyclerView.setAdapter(workoutListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));//not sure if it;s ok

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Workout testWorkout = new Workout(Calendar.getInstance().getTime());
                mWorkoutViewModel.insertSingleWorkout(testWorkout);
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWorkoutViewModel.deleteAll();
                Toast.makeText(view.getContext(), "All workouts deleted!", Toast.LENGTH_SHORT).show();
            }
        });

        mWorkoutViewModel.getAllWorkouts().observe(this, new Observer<List<Workout>>() {
            @Override
            public void onChanged(List<Workout> workouts) {
                workoutListAdapter.setWorkouts(workouts);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Workout w = workoutListAdapter.getWorkoutAt(viewHolder.getAdapterPosition());
                mWorkoutViewModel.deleteWorkout(w);
                Toast.makeText(view.getContext(), "Workout deleted!", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        return view;
    }
}

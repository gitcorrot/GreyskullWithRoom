package com.corrot.room.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.corrot.room.R;
import com.corrot.room.adapters.WorkoutsListAdapter;
import com.corrot.room.db.entity.Workout;
import com.corrot.room.viewmodel.WorkoutViewModel;
import com.marcohc.robotocalendar.RobotoCalendarView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryFragment extends Fragment {

    private WorkoutViewModel mWorkoutViewModel;
    private RobotoCalendarView calendarView;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_history, container, false);
        mWorkoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);

        final RecyclerView recyclerView = view.findViewById(R.id.fragment_history_recycler_view);
        calendarView = view.findViewById(R.id.fragment_history_calendar_view);

        final WorkoutsListAdapter workoutListAdapter =
                new WorkoutsListAdapter(this.getContext(), this.getActivity());

        recyclerView.setAdapter(workoutListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mWorkoutViewModel.getAllWorkouts().observe(this, workouts -> {
            setCalendarEvents(workouts);
            workoutListAdapter.setWorkouts(workouts);
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
                Toast.makeText(view.getContext(),
                        "Workout deleted!", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
        return view;
    }

    private void setCalendarEvents(List<Workout> workouts) {
        calendarView.clearSelectedDay();
        for(Workout w : workouts) {
            calendarView.markCircleImage1(w.workoutDate);
        }
    }
}

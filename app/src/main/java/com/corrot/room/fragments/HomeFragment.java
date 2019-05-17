package com.corrot.room.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.corrot.room.R;
import com.corrot.room.activities.NewWorkoutActivity;
import com.corrot.room.adapters.WorkoutsListAdapter;
import com.corrot.room.db.entity.Routine;
import com.corrot.room.db.entity.Workout;
import com.corrot.room.viewmodel.HomeFragmentViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment implements
        WorkoutsListAdapter.WorkoutsListAdapterInterface {

    private HomeFragmentViewModel mHomeViewModel;
    private List<Routine> routinesList;
    private Date loadTo;
    private Date loadFrom;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        routinesList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        loadTo = calendar.getTime();
        calendar.add(Calendar.MONTH, -1);
        loadFrom = calendar.getTime();

        // TODO: add button "Show more..." and onClick set loadFrom to month before.

        mHomeViewModel = ViewModelProviders.of(this).get(HomeFragmentViewModel.class);

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FloatingActionButton floatingButton = view.findViewById(R.id.floating_button);
        TextView lastWorkoutTextView = view.findViewById(R.id.fragment_home_last_workout_text_view);
        TextView workoutsCountTextView = view.findViewById(R.id.fragment_home_workouts_count_text_view);
        TextView recentWorkoutsTextView = view.findViewById(R.id.fragment_home_recent_workouts_text_view);
        Button moreButton = view.findViewById(R.id.fragment_home_load_more_button);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.fragment_home_swipe_layout);
        RecyclerView recyclerView = view.findViewById(R.id.fragment_home_recycler_view);

        WorkoutsListAdapter mWorkoutListAdapter = new WorkoutsListAdapter(getContext(), getActivity(), this);
        recyclerView.setAdapter(mWorkoutListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(null);

        mHomeViewModel.getAllRoutines().observe(this, routines -> routinesList = routines);
        mHomeViewModel.getLastWorkout().observe(this, lastWorkoutTextView::setText);
        mHomeViewModel.getTotalCount().observe(this, count -> {
            if (count != null) {
                workoutsCountTextView.setVisibility(View.VISIBLE);
                workoutsCountTextView.setText(count);
            } else workoutsCountTextView.setVisibility(View.INVISIBLE);
        });
        mHomeViewModel.getWorkoutsFromTo().observe(this, workouts -> {
            if (workouts.isEmpty()) {
                recentWorkoutsTextView.setVisibility(View.INVISIBLE);
            } else {
                recentWorkoutsTextView.setVisibility(View.VISIBLE);
                mWorkoutListAdapter.setWorkouts(workouts);
            }
        });

        mHomeViewModel.setDateFrom(loadFrom);
        mHomeViewModel.setDateTo(loadTo);

        moreButton.setOnClickListener(v -> {
            calendar.setTime(loadFrom);
            calendar.add(Calendar.MONTH, -1);
            loadFrom = calendar.getTime();
            mHomeViewModel.setDateFrom(loadFrom);
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadTo = Calendar.getInstance().getTime();
            mHomeViewModel.setDateTo(loadTo);
            swipeRefreshLayout.setRefreshing(false);
        });

        // Start NewWorkoutActivity on floatingButton click
        floatingButton.setOnClickListener(v -> {
            if (!getActivity().isFinishing()) showExercisesDialog(getContext()).show();
        });

        return view;
    }

    private AlertDialog showExercisesDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose workout");

        // TODO: (maybe) DON'T KEEP ALL ROUTINES IN LIST. JUST MAKE
        // TODO: A SYNCHRONOUS CALL WHEN YOU NEED IT

        List<String> routines = new ArrayList<>();
        routines.add("Normal workout");
        if (routinesList != null) {
            for (Routine r : routinesList) {
                routines.add(r.label);
            }
        }

        String[] routinesArray = new String[routines.size()];
        routines.toArray(routinesArray);

        builder.setItems(routinesArray, (dialog, which) -> {
            if (routinesArray.length >= which) {
                Intent newWorkoutIntent = new Intent(getContext(), NewWorkoutActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("flags", NewWorkoutActivity.FLAG_ADD_WORKOUT);
                // 0 is "Normal workout"
                if (which > 0) {
                    bundle.putSerializable("routine", routinesList.get(which - 1));
                }
                newWorkoutIntent.putExtras(bundle);
                startActivity(newWorkoutIntent);
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    // Workouts list adapter's interface method
    @Override
    public void onEditClick(Workout workout) {
        Intent updateWorkoutIntent = new Intent(getActivity(), NewWorkoutActivity.class);
        updateWorkoutIntent.putExtra("flags", NewWorkoutActivity.FLAG_UPDATE_WORKOUT);
        updateWorkoutIntent.putExtra("workoutId", workout.id);
        startActivity(updateWorkoutIntent);
    }

    @Override
    public void onDeleteClick(Workout workout) {
        mHomeViewModel.deleteWorkout(workout);
        Toast.makeText(getContext(), "Workout deleted!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShareClick(Workout workout) {
        // TODO: Share workout.
    }
}

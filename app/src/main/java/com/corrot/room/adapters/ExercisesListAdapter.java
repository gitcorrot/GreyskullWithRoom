package com.corrot.room.adapters;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.corrot.room.ExerciseItem;
import com.corrot.room.ExerciseSetItem;
import com.corrot.room.R;
import com.corrot.room.viewmodel.NewWorkoutViewModel;

import java.util.ArrayList;
import java.util.List;

public class ExercisesListAdapter extends RecyclerView.Adapter<ExercisesListAdapter.exerciseViewHolder> {

    class exerciseViewHolder extends RecyclerView.ViewHolder {
        private final TextView exerciseTextView;
        private final RecyclerView setsRecyclerView;
        private final Button addSetButton;

        private exerciseViewHolder(View view) {
            super(view);
            exerciseTextView = view.findViewById(R.id.exercise_name_text_view);
            setsRecyclerView = view.findViewById(R.id.sets_recycler_view);
            addSetButton = view.findViewById(R.id.add_set_button);
        }
    }

    private final LayoutInflater mInflater;
    private List<ExerciseItem> mExercises;
    private NewWorkoutViewModel newWorkoutViewModel;
    private LifecycleOwner mLifecycleOwner;


    public ExercisesListAdapter(Context context, LifecycleOwner lifecycleOwner) {
        mInflater = LayoutInflater.from(context);
        mLifecycleOwner = lifecycleOwner;
        mExercises = new ArrayList<>(); // necessary?
        newWorkoutViewModel = ViewModelProviders.of((AppCompatActivity)context). // ???
                get(NewWorkoutViewModel.class);
        newWorkoutViewModel.init();
    }

    @NonNull
    @Override
    public exerciseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.recyclerview_exercise_item, viewGroup, false);
        return new exerciseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final exerciseViewHolder exerciseViewHolder, int i) {

        // Attach sets to recycler view
        final SetsListAdapter exerciseSetsAdapter =
                new SetsListAdapter(exerciseViewHolder.setsRecyclerView.getContext()); // wrong context??

        exerciseViewHolder.setsRecyclerView.setAdapter(exerciseSetsAdapter);
        exerciseViewHolder.setsRecyclerView.setLayoutManager(
                new LinearLayoutManager(exerciseViewHolder.setsRecyclerView.getContext()));


        newWorkoutViewModel.getAllSetItems().observe(mLifecycleOwner, new Observer<List<ExerciseSetItem>>() {
            @Override
            public void onChanged(@Nullable List<ExerciseSetItem> exerciseSetItems) {
                int position = exerciseViewHolder.getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    int exerciseId = mExercises.get(position).id;
                    List<ExerciseSetItem> items = newWorkoutViewModel.getSetsByExerciseId(exerciseId);
                    exerciseSetsAdapter.setSets(items);
                }
            }
        });

        if(mExercises != null) {
            ExerciseItem exercise = mExercises.get(i);
            exerciseViewHolder.exerciseTextView.setText(exercise.name + " ID: "+ exercise.id);
        }

        exerciseViewHolder.addSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = exerciseViewHolder.getAdapterPosition();
                newWorkoutViewModel.addSet(new ExerciseSetItem(id, 0, 0));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mExercises != null)
            return mExercises.size();
        else return 0;
    }

    public void setExercises(List<ExerciseItem> exercises) {
        mExercises = exercises;
        this.notifyDataSetChanged();
    }
}

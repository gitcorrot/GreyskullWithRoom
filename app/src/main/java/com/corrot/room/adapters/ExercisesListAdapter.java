package com.corrot.room.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.corrot.room.db.entity.Exercise;

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

    public ExercisesListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
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
                new SetsListAdapter(exerciseViewHolder.addSetButton.getContext()); // wrong context??

        exerciseViewHolder.setsRecyclerView.setAdapter(exerciseSetsAdapter);
        exerciseViewHolder.setsRecyclerView.setLayoutManager(
                new LinearLayoutManager(exerciseViewHolder.addSetButton.getContext()));
        //adapters.add(exerciseSetsAdapter);

        if(mExercises != null) {
            ExerciseItem exercise = mExercises.get(i);
            exerciseViewHolder.exerciseTextView.setText(exercise.name);
        }

        exerciseViewHolder.addSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExercises.get(exerciseViewHolder.getAdapterPosition()).sets.
                        add(new ExerciseSetItem(0,0));
                exerciseSetsAdapter.setSets(mExercises.
                        get(exerciseViewHolder.getAdapterPosition()).sets);
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

    public void addExercise(ExerciseItem exercise) {
        mExercises.add(exercise);
        this.notifyDataSetChanged();
    }

    public List<Exercise> getExercises() {

        List<Exercise> exercises = new ArrayList<>();

        for(int i = 0; i < mExercises.size(); i++) {
            ExerciseItem e = mExercises.get(i);

            List<Float> weights = new ArrayList<>();
            List<Integer> reps = new ArrayList<>();


            /*List<ExerciseSetItem> sets = exerciseSetsAdapter.getSets();

            for(ExerciseSetItem i : sets) {
                weights.add(i.weight);
                reps.add(i.reps);
            }*/

            Exercise exercise = new Exercise(i, e.name, weights, reps);
            exercises.add(exercise);
        }
        return  exercises;
    }
}

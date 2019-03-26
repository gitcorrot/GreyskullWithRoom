package com.corrot.room.adapters;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
        private final ImageButton removeExerciseButton;

        private exerciseViewHolder(View view) {
            super(view);

            exerciseTextView = view.findViewById(R.id.exercise_name_text_view);
            setsRecyclerView = view.findViewById(R.id.sets_recycler_view);
            addSetButton = view.findViewById(R.id.add_set_button);
            removeExerciseButton = view.findViewById(R.id.exercise_remove_image_button);
        }
    }

    private NewWorkoutViewModel newWorkoutViewModel;

    private final LayoutInflater mInflater;
    private List<ExerciseItem> mExercises;

    public ExercisesListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        //mExercises = new ArrayList<>(); // necessary?

        newWorkoutViewModel = ViewModelProviders.of((AppCompatActivity)context). // ???
                get(NewWorkoutViewModel.class);
        newWorkoutViewModel.init();
    }

    @NonNull
    @Override
    public exerciseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_exercise_item, viewGroup, false);
        return new exerciseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final exerciseViewHolder viewHolder, int i) {

        // Attach sets to recycler view
        final SetsListAdapter exerciseSetsAdapter =
                new SetsListAdapter(viewHolder.setsRecyclerView.getContext()); // wrong context??

        viewHolder.setsRecyclerView.setAdapter(exerciseSetsAdapter);
        viewHolder.setsRecyclerView.setLayoutManager(
                new LinearLayoutManager(viewHolder.setsRecyclerView.getContext()));

        final int exercisePosition = viewHolder.getAdapterPosition();

        if(exercisePosition != RecyclerView.NO_POSITION) {
            //int exerciseItemPosition = mExercises.get(exercisePosition).position;
            List<ExerciseSetItem> items = newWorkoutViewModel.getSetsByExercisePosition(exercisePosition);
            exerciseSetsAdapter.setSets(items);
        }

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder vh, int i) {
                int setPosition = vh.getAdapterPosition();
                ExerciseSetItem item = newWorkoutViewModel
                        .getSetsByExercisePosition(exercisePosition).get(setPosition);
                newWorkoutViewModel.removeSet(item);
                List<ExerciseSetItem> items = newWorkoutViewModel.getSetsByExercisePosition(exercisePosition);
                viewHolder.setsRecyclerView.setAdapter(exerciseSetsAdapter);
                exerciseSetsAdapter.setSets(items);
                Toast.makeText(viewHolder.itemView.getContext(), "Set deleted!", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(viewHolder.setsRecyclerView);

        if(mExercises != null) {
            ExerciseItem exercise = mExercises.get(viewHolder.getAdapterPosition());
            viewHolder.exerciseTextView.setText(exercise.name + " Position: "+ exercise.position);
        }

        viewHolder.addSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = viewHolder.getAdapterPosition();
                newWorkoutViewModel.addSet(new ExerciseSetItem(id));
                List<ExerciseSetItem> items = newWorkoutViewModel.getSetsByExercisePosition(id);
                viewHolder.setsRecyclerView.setAdapter(exerciseSetsAdapter);
                exerciseSetsAdapter.setSets(items);
            }
        });

        viewHolder.removeExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExerciseItem exercise = newWorkoutViewModel.getExerciseByPosition(exercisePosition);
                newWorkoutViewModel.removeExercise(exercise);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mExercises != null ? mExercises.size() : 0;
    }

    public void setExercises(List<ExerciseItem> exercises) {
        mExercises = exercises;
        this.notifyDataSetChanged();
    }
}

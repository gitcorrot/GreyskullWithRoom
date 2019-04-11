package com.corrot.room.adapters;

import android.content.Context;
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

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private NewWorkoutViewModel mNewWorkoutViewModel;
    private final LayoutInflater mInflater;
    private List<ExerciseItem> mExercises;

    public ExercisesListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mNewWorkoutViewModel = ViewModelProviders.of((AppCompatActivity) context). // ???
                get(NewWorkoutViewModel.class);
        mNewWorkoutViewModel.init();
    }

    @NonNull
    @Override
    public exerciseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView =
                mInflater.inflate(R.layout.recyclerview_exercise_item, viewGroup,false);
       final exerciseViewHolder vh = new exerciseViewHolder(itemView);

        // TODO: Refactor it.

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final exerciseViewHolder viewHolder, int i) {

        // Attach sets to recycler view
        final SetsListAdapter exerciseSetsAdapter =
                new SetsListAdapter(viewHolder.setsRecyclerView.getContext());

        viewHolder.setsRecyclerView.setAdapter(exerciseSetsAdapter);
        viewHolder.setsRecyclerView.setLayoutManager(
                new LinearLayoutManager(viewHolder.setsRecyclerView.getContext()));

        final int exercisePosition = viewHolder.getAdapterPosition();

        if (exercisePosition != RecyclerView.NO_POSITION) {
            List<ExerciseSetItem> items =
                    mNewWorkoutViewModel.getSetsByExercisePosition(exercisePosition);
            exerciseSetsAdapter.setSets(items);
        }

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder vh, int i) {
                int setPosition = vh.getAdapterPosition();

                ExerciseSetItem item = mNewWorkoutViewModel
                        .getSetsByExercisePosition(exercisePosition).get(setPosition);
                mNewWorkoutViewModel.removeSet(item);

                List<ExerciseSetItem> items = mNewWorkoutViewModel
                        .getSetsByExercisePosition(exercisePosition);

                viewHolder.setsRecyclerView.setAdapter(exerciseSetsAdapter);
                exerciseSetsAdapter.setSets(items);
                Toast.makeText(viewHolder.itemView.getContext(),
                        "Set deleted!", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(viewHolder.setsRecyclerView);


        viewHolder.addSetButton.setOnClickListener(v -> {
            int id = viewHolder.getAdapterPosition();
            mNewWorkoutViewModel.addSet(new ExerciseSetItem(id));
            List<ExerciseSetItem> items = mNewWorkoutViewModel.getSetsByExercisePosition(id);
            viewHolder.setsRecyclerView.setAdapter(exerciseSetsAdapter);
            exerciseSetsAdapter.setSets(items);
        });

        viewHolder.removeExerciseButton.setOnClickListener(v -> {
            ExerciseItem exercise = mNewWorkoutViewModel.getExerciseByPosition(exercisePosition);
            mNewWorkoutViewModel.removeExercise(exercise);
        });

        if (mExercises != null) {
            ExerciseItem exercise = mExercises.get(viewHolder.getAdapterPosition());
            viewHolder.exerciseTextView.setText(exercise.name);
        }

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


/*
package com.corrot.room.adapters;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

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

    private NewWorkoutViewModel mNewWorkoutViewModel;
    private final LayoutInflater mInflater;
    private List<ExerciseItem> mExercises;

    public ExercisesListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mNewWorkoutViewModel = ViewModelProviders.of((AppCompatActivity) context). // ???
                get(NewWorkoutViewModel.class);
        mNewWorkoutViewModel.init();
    }

    @NonNull
    @Override
    public exerciseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView =
                mInflater.inflate(R.layout.recyclerview_exercise_item, viewGroup, false);
        final exerciseViewHolder vh = new exerciseViewHolder(itemView);
        final int id = vh.getAdapterPosition();
        // Attach sets to recycler view
        final SetsListAdapter exerciseSetsAdapter = new SetsListAdapter(vh.setsRecyclerView.getContext());



        if (vh.getAdapterPosition() != RecyclerView.NO_POSITION) {
            List<ExerciseSetItem> items =
                    mNewWorkoutViewModel.getSetsByExercisePosition(vh.getAdapterPosition());
            exerciseSetsAdapter.setSets(items);
        }
        vh.setsRecyclerView.setAdapter(exerciseSetsAdapter);
        vh.setsRecyclerView.setLayoutManager(
                new LinearLayoutManager(vh.setsRecyclerView.getContext()));

        vh.addSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewWorkoutViewModel.addSet(new ExerciseSetItem(id));
                List<ExerciseSetItem> items = mNewWorkoutViewModel.getSetsByExercisePosition(id);
                vh.setsRecyclerView.setAdapter(exerciseSetsAdapter);
                exerciseSetsAdapter.setSets(items);
            }
        });

        vh.removeExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExerciseItem exercise = mNewWorkoutViewModel.getExerciseByPosition(vh.getAdapterPosition());
                mNewWorkoutViewModel.removeExercise(exercise);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder vh, int i) {
                int setPosition = vh.getAdapterPosition();

                ExerciseSetItem item = mNewWorkoutViewModel
                        .getSetsByExercisePosition(vh.getAdapterPosition()).get(setPosition);
                mNewWorkoutViewModel.removeSet(item);

                List<ExerciseSetItem> items = mNewWorkoutViewModel
                        .getSetsByExercisePosition(vh.getAdapterPosition());

                //TODO: Refactor this to not use setAdapter!
                //vh.setsRecyclerView.setAdapter(exerciseSetsAdapter);
                exerciseSetsAdapter.setSets(items);
                Toast.makeText(vh.itemView.getContext(),
                        "Set deleted!", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(vh.setsRecyclerView);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final exerciseViewHolder viewHolder, int i) {
        if (mExercises != null) {
            ExerciseItem exercise = mExercises.get(viewHolder.getAdapterPosition());
            viewHolder.exerciseTextView.setText(exercise.name);
        }
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

 */
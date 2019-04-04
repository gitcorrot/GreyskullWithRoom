package com.corrot.room.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.corrot.room.DefinedWorkoutExerciseItem;
import com.corrot.room.R;
import com.corrot.room.utils.PreferencesManager;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DefinedWorkoutExercisesAdapter
        extends RecyclerView.Adapter<DefinedWorkoutExercisesAdapter.DefinedWorkoutExerciseViewHolder> {

    class DefinedWorkoutExerciseViewHolder extends RecyclerView.ViewHolder {
        private final Spinner exercisesSpinner;
        private final MaterialButton incrementButton;
        private final MaterialButton decrementButton;
        private final TextView setsNumberTextView;

        private DefinedWorkoutExerciseViewHolder(View itemView) {
            super(itemView);
            exercisesSpinner = itemView.findViewById(R.id.dialog_add_defined_exercise_spinner);
            incrementButton = itemView.findViewById(R.id.dialog_add_defined_increment);
            decrementButton = itemView.findViewById(R.id.dialog_add_defined_decrement);
            setsNumberTextView = itemView.findViewById(R.id.dialog_add_defined_sets_number);

            pm = PreferencesManager.getInstance();
        }
    }

    private final LayoutInflater mInflater;
    private List<DefinedWorkoutExerciseItem> mExercises;
    private PreferencesManager pm;

    public DefinedWorkoutExercisesAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public DefinedWorkoutExerciseViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_defined_workout_exercise_item, viewGroup, false);
        return new DefinedWorkoutExerciseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DefinedWorkoutExerciseViewHolder viewHolder, int position) {
        final String[] exercisesNames = pm.getExercises();
        if (viewHolder.itemView.getContext() != null && exercisesNames != null) {
            ArrayAdapter namesAdapter = new ArrayAdapter<>(
                    viewHolder.itemView.getContext(),
                    R.layout.spinner_item,
                    R.id.spinner_text_view,
                    exercisesNames
            );
            namesAdapter.setDropDownViewResource(R.layout.spinner_item);
            viewHolder.exercisesSpinner.setAdapter(namesAdapter);
        }
        viewHolder.exercisesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mExercises.get(position).name = exercisesNames[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mExercises.get(viewHolder.getAdapterPosition()).name = exercisesNames[0];
            }
        });

        viewHolder.incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(viewHolder.setsNumberTextView.getText().toString());
                viewHolder.setsNumberTextView.setText(String.valueOf(number + 1));
            }
        });

        viewHolder.decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(viewHolder.setsNumberTextView.getText().toString());
                viewHolder.setsNumberTextView.setText(String.valueOf(number - 1));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mExercises != null ? mExercises.size() : 0;
    }

    public void setExercises(List<DefinedWorkoutExerciseItem> exercises) {
        mExercises = exercises;
        notifyDataSetChanged();
    }
}



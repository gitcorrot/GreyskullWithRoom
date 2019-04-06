package com.corrot.room.adapters;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.corrot.room.DefinedWorkoutExerciseItem;
import com.corrot.room.R;
import com.corrot.room.utils.PreferencesManager;
import com.corrot.room.viewmodel.NewDefinedWorkoutViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class DefinedWorkoutExercisesAdapter
        extends RecyclerView.Adapter<DefinedWorkoutExercisesAdapter.DefinedWorkoutExerciseViewHolder> {

    class DefinedWorkoutExerciseViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton dropDownButton;
        private final TextView exerciseNameTextView;
        private final MaterialButton incrementButton;
        private final MaterialButton decrementButton;
        private final TextView setsNumberTextView;

        private DefinedWorkoutExerciseViewHolder(View itemView) {
            super(itemView);
            dropDownButton = itemView.findViewById(R.id.dialog_add_defined_exercise_menu);
            exerciseNameTextView = itemView.findViewById(R.id.dialog_add_defined_exercise_name);
            incrementButton = itemView.findViewById(R.id.dialog_add_defined_increment);
            decrementButton = itemView.findViewById(R.id.dialog_add_defined_decrement);
            setsNumberTextView = itemView.findViewById(R.id.dialog_add_defined_sets_number);
        }
    }

    private final LayoutInflater mInflater;
    private List<DefinedWorkoutExerciseItem> mExercises;
    private final String[] exercisesNames;
    private NewDefinedWorkoutViewModel mNewDefinedWorkoutViewModel;

    public DefinedWorkoutExercisesAdapter(FragmentActivity activity) {
        mInflater = LayoutInflater.from(activity);
        exercisesNames = PreferencesManager.getInstance().getExercises();
        mNewDefinedWorkoutViewModel = new NewDefinedWorkoutViewModel();
              //  = ViewModelProviders.of(activity).get(NewDefinedWorkoutViewModel.class);
        mNewDefinedWorkoutViewModel.init();
    }

    @NonNull
    @Override
    public DefinedWorkoutExerciseViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_defined_workout_exercise_item, viewGroup, false);
        final DefinedWorkoutExerciseViewHolder vh = new DefinedWorkoutExerciseViewHolder(itemView);

        final PopupMenu popup = new PopupMenu(vh.exerciseNameTextView.getContext(), vh.dropDownButton);
        for (int i = 0; i < exercisesNames.length; i++) {
            popup.getMenu().add(Menu.NONE, i, Menu.NONE, exercisesNames[i]);
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int pos = item.getItemId();
                DefinedWorkoutExerciseItem e = mExercises.get(vh.getAdapterPosition());
                e.name = exercisesNames[pos];
                mNewDefinedWorkoutViewModel.updateExercise(e);
                return true;
            }
        });

        vh.dropDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.show();
            }
        });

        vh.incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefinedWorkoutExerciseItem e = mExercises.get(vh.getAdapterPosition());
                e.sets++;
                mNewDefinedWorkoutViewModel.updateExercise(e);
            }
        });

        vh.decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefinedWorkoutExerciseItem e = mExercises.get(vh.getAdapterPosition());
                if (e.sets > 0) {
                    e.sets--;
                    mNewDefinedWorkoutViewModel.updateExercise(e);
                }
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final DefinedWorkoutExerciseViewHolder viewHolder, int position) {
        int sets = mExercises.get(viewHolder.getAdapterPosition()).sets;
        viewHolder.setsNumberTextView.setText(String.valueOf(sets));
        String name = mExercises.get(viewHolder.getAdapterPosition()).name;
        viewHolder.exerciseNameTextView.setText(name);
    }

    @Override
    public int getItemCount() {
        return mExercises != null ? mExercises.size() : 0;
    }

    public DefinedWorkoutExerciseItem getExerciseAt(int pos) {
        return mExercises.get(pos);
    }

    public void setExercises(List<DefinedWorkoutExerciseItem> newExercises) {
        if (mExercises == null) {
            mExercises = new ArrayList<>();
        }
        if (newExercises != null) {
            this.mExercises.clear();
            this.mExercises.addAll(newExercises);
            notifyDataSetChanged();
        }
    }
}



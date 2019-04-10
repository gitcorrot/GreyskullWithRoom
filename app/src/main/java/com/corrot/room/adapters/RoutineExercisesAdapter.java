package com.corrot.room.adapters;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.corrot.room.RoutineExerciseItem;
import com.corrot.room.R;
import com.corrot.room.utils.PreferencesManager;
import com.corrot.room.viewmodel.NewRoutineViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class RoutineExercisesAdapter
        extends RecyclerView.Adapter<RoutineExercisesAdapter.RoutineExerciseViewHolder> {

    class RoutineExerciseViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton dropDownButton;
        private final TextView exerciseNameTextView;
        private final MaterialButton incrementButton;
        private final MaterialButton decrementButton;
        private final TextView setsNumberTextView;

        private RoutineExerciseViewHolder(View itemView) {
            super(itemView);
            dropDownButton = itemView.findViewById(R.id.dialog_add_routine_exercise_menu);
            exerciseNameTextView = itemView.findViewById(R.id.dialog_add_routine_exercise_name);
            incrementButton = itemView.findViewById(R.id.dialog_add_routine_increment);
            decrementButton = itemView.findViewById(R.id.dialog_add_routine_decrement);
            setsNumberTextView = itemView.findViewById(R.id.dialog_add_routine_sets_number);

            incrementButton.setText("+");
            decrementButton.setText("-");
        }
    }

    private final LayoutInflater mInflater;
    private List<RoutineExerciseItem> mExercises;
    private NewRoutineViewModel mNewRoutineViewModel;
    private final String[] exercisesNames;

    public RoutineExercisesAdapter(FragmentActivity activity) {
        mInflater = LayoutInflater.from(activity);
        exercisesNames = PreferencesManager.getInstance().getExercises();
        mNewRoutineViewModel =
                ViewModelProviders.of(activity).get(NewRoutineViewModel.class);
        mNewRoutineViewModel.init();
    }

    @NonNull
    @Override
    public RoutineExerciseViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_routine_exercise_item, viewGroup, false);
        final RoutineExerciseViewHolder vh = new RoutineExerciseViewHolder(itemView);

        final PopupMenu popup = new PopupMenu(vh.exerciseNameTextView.getContext(), vh.dropDownButton);
        for (int i = 0; i < exercisesNames.length; i++) {
            popup.getMenu().add(Menu.NONE, i, Menu.NONE, exercisesNames[i]);
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int pos = item.getItemId();
                RoutineExerciseItem e = mExercises.get(vh.getAdapterPosition());
                if(e == null) {
                    e = new RoutineExerciseItem("", 0,0);
                }
                e.name = exercisesNames[pos];
                mNewRoutineViewModel.updateExercise(e);
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
                RoutineExerciseItem e = mExercises.get(vh.getAdapterPosition());
                e.sets++;
                mNewRoutineViewModel.updateExercise(e);
            }
        });

        vh.decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoutineExerciseItem e = mExercises.get(vh.getAdapterPosition());
                if (e.sets > 0) {
                    e.sets--;
                    mNewRoutineViewModel.updateExercise(e);
                }
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RoutineExerciseViewHolder viewHolder, int position) {
        int sets = mExercises.get(viewHolder.getAdapterPosition()).sets;
        String name = mExercises.get(viewHolder.getAdapterPosition()).name;
        viewHolder.setsNumberTextView.setText(String.valueOf(sets));
        viewHolder.exerciseNameTextView.setText(name);
    }

    @Override
    public int getItemCount() {
        return mExercises != null ? mExercises.size() : 0;
    }

    public RoutineExerciseItem getExerciseAt(int pos) {
        return mExercises.get(pos);
    }

    public void setExercises(List<RoutineExerciseItem> newExercises) {
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



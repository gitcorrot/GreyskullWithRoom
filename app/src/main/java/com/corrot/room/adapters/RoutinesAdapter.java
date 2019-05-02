package com.corrot.room.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.corrot.room.R;
import com.corrot.room.db.entity.Routine;
import com.corrot.room.utils.RoutinesDiffUtilCallback;
import com.corrot.room.viewmodel.RoutineViewModel;

import java.util.ArrayList;
import java.util.List;

public class RoutinesAdapter extends RecyclerView.Adapter<RoutinesAdapter.RoutineViewHolder> {

    class RoutineViewHolder extends RecyclerView.ViewHolder {
        private final TextView labelTextView;
        private final TextView exercisesTextView;
        private final LinearLayout subItem;
        private final ImageButton deleteButton;
        private final ImageButton editButton;

        private RoutineViewHolder(View itemView) {
            super(itemView);
            labelTextView = itemView.findViewById(R.id.routine_label);
            exercisesTextView = itemView.findViewById(R.id.routine_exercises);
            subItem = itemView.findViewById(R.id.routine_sub_item);
            deleteButton = itemView.findViewById(R.id.routine_delete_button);
            editButton = itemView.findViewById(R.id.routine_edit_button);
        }
    }

    private final LayoutInflater mInflater;
    private List<Routine> mRoutines;
    private RoutinesAdapterInterface listener;

    public interface RoutinesAdapterInterface {
        void onDeleteClick(Routine routine);
        void onEditClick(Routine routine);
    }

    public RoutinesAdapter(Context context, RoutinesAdapterInterface listener) {
        mInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoutineViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.recyclerview_routine_item, viewGroup, false);
        final RoutineViewHolder viewHolder = new RoutineViewHolder(itemView);

        viewHolder.itemView.setOnClickListener(v -> {
            final Routine workout = mRoutines.get(viewHolder.getAdapterPosition());
            workout.expanded = !workout.expanded;
            notifyItemChanged(viewHolder.getAdapterPosition());
        });

        viewHolder.deleteButton.setOnClickListener(v -> {
            final Routine routine = mRoutines.get(viewHolder.getAdapterPosition());
            listener.onDeleteClick(routine);
        });

        viewHolder.editButton.setOnClickListener(v -> {
            final Routine routine = mRoutines.get(viewHolder.getAdapterPosition());
            listener.onEditClick(routine);
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RoutineViewHolder viewHolder, int i) {
        if (mRoutines != null) {
            final Routine routine = mRoutines.get(i);
            viewHolder.labelTextView.setText(routine.label);

            StringBuilder exercises = new StringBuilder();
            if (routine.exercises.size() == routine.sets.size()) {
                for (int j = 0; j < routine.exercises.size(); j++) {
                    if (exercises.length() > 1) exercises.append("\n");
                    exercises.append(routine.exercises.get(j))
                            .append(", ")
                            .append(routine.sets.get(j))
                            .append(" sets");
                }
            } else {
                // TODO: exercises size != sets size
            }
            viewHolder.exercisesTextView.setText(exercises.toString());
            viewHolder.subItem.setVisibility(routine.expanded ? View.VISIBLE : View.GONE);
        }

    }

    public void setRoutines(List<Routine> newRoutines) {
        if (this.mRoutines == null) {
            this.mRoutines = new ArrayList<>();
        }
        if (newRoutines != null) {
            RoutinesDiffUtilCallback callback =
                    new RoutinesDiffUtilCallback(this.mRoutines, newRoutines);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
            this.mRoutines.clear();
            this.mRoutines.addAll(newRoutines);
            diffResult.dispatchUpdatesTo(this);
        }
    }

    @Override
    public int getItemCount() {
        if (mRoutines != null)
            return mRoutines.size();
        else return 0;
    }
}

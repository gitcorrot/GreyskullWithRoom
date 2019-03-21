package com.corrot.room.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.corrot.room.ExerciseSetItem;
import com.corrot.room.R;

import java.util.List;

public class SetsListAdapter extends RecyclerView.Adapter<SetsListAdapter.ExerciseSetViewHolder>  {

    public static int REPS_EDIT_TEXT  = 0;
    public static int WEIGHT_EDIT_TEXT  = 1;

    class ExerciseSetViewHolder extends RecyclerView.ViewHolder {
        private final TextView setTextView;
        private final EditText repsEditText;
        private final EditText weightEditText;

        private MyCustomEditTextListener repsEditTextListener;
        private MyCustomEditTextListener weightEditTextListener;

        private ExerciseSetViewHolder(View itemView) {
            super(itemView);

            setTextView = itemView.findViewById(R.id.new_set_text_view);
            repsEditText = itemView.findViewById(R.id.new_set_reps_edit_text);
            weightEditText = itemView.findViewById(R.id.new_set_weight_edit_text);

            repsEditTextListener = new MyCustomEditTextListener(SetsListAdapter.REPS_EDIT_TEXT);
            weightEditTextListener = new MyCustomEditTextListener(SetsListAdapter.WEIGHT_EDIT_TEXT);

            repsEditText.addTextChangedListener(repsEditTextListener);
            weightEditText.addTextChangedListener(weightEditTextListener);
        }
    }

    private final LayoutInflater mInflater;
    private List<ExerciseSetItem> mSets;

    public SetsListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ExerciseSetViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_set_item, viewGroup, false);
        return new ExerciseSetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExerciseSetViewHolder viewHolder, int i) {

        viewHolder.repsEditTextListener.updatePosition(viewHolder.getAdapterPosition());
        viewHolder.weightEditTextListener.updatePosition(viewHolder.getAdapterPosition());

        if(mSets!= null) {
            ExerciseSetItem item = mSets.get(i);
            viewHolder.setTextView.setText(String.valueOf(i));
            viewHolder.weightEditText.setText(String.valueOf(item.weight));
            viewHolder.repsEditText.setText(String.valueOf(item.reps));
        }
    }

    @Override
    public int getItemCount() {
        return mSets != null ? mSets.size() : 0;
    }

    public void setSets(List<ExerciseSetItem> sets) {
        mSets = sets;
        notifyDataSetChanged();
    }

    private class MyCustomEditTextListener implements TextWatcher {

        private int position;
        private int type;

        public MyCustomEditTextListener(int type) {
            this.type = type;
        }

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            // TODO: update in repository. (diffutils?)

            if(!TextUtils.isEmpty(charSequence)) {
                if(type == SetsListAdapter.REPS_EDIT_TEXT) {
                    mSets.get(position).reps = Integer.parseInt(charSequence.toString());
                }
                else if (type == SetsListAdapter.WEIGHT_EDIT_TEXT) {
                    mSets.get(position).weight = Float.parseFloat(charSequence.toString());
                }
            }
            else {
                if(type == SetsListAdapter.REPS_EDIT_TEXT) {
                    mSets.get(position).reps = 0;
                }
                else if (type == SetsListAdapter.WEIGHT_EDIT_TEXT) {
                    mSets.get(position).weight = 0;
                }
            }
        }

        // no usage
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) { }

        @Override
        public void afterTextChanged(Editable editable) { }
    }
}



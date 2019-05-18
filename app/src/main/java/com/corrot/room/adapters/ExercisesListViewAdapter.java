package com.corrot.room.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.corrot.room.R;
import com.corrot.room.utils.PreferencesManager;

import java.util.List;

public class ExercisesListViewAdapter extends BaseAdapter implements ListAdapter {

    private List<String> list;
    private Context context;

    public ExercisesListViewAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.listview_exercise_item, null);
        }

        TextView exerciseNameTextView = view.findViewById(R.id.listview_exercise_item_name);
        ImageButton exerciseDeleteButton = view.findViewById(R.id.listview_exercise_item_delete_button);

        exerciseNameTextView.setText(list.get(position));
        exerciseDeleteButton.setOnClickListener(v -> {
            list.remove(position);
            notifyDataSetChanged();
        });

        return view;
    }

    public void setList(List<String> newList) {
        list = newList;
        notifyDataSetChanged();
    }

    public List<String> getList() {
        return list;
    }

    public void addExercise(String exercise) {
        // TODO: exceptions
        list.add(exercise);
        notifyDataSetChanged();
    }
}

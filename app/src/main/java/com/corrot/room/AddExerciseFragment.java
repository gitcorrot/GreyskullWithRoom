package com.corrot.room;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class AddExerciseFragment extends Fragment {

    Button buttonDelete;
    Button buttonAddSet;
    LinearLayout linearLayout;
    AddExerciseSetItem addExerciseSetItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_new_exercise, container, false);

        buttonDelete = view.findViewById(R.id.new_exercise_delete_button);
        buttonAddSet = view.findViewById(R.id.new_exercise_add_set_button);
        linearLayout = view.findViewById(R.id.new_exercise_linear_layout);

        addExerciseSetItem = new AddExerciseSetItem();
        //addExerciseSetItem.

        //view.getTag();

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.removeAllViews();
            }
        });

        buttonAddSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}

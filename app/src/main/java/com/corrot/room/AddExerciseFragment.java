package com.corrot.room;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class AddExerciseFragment extends Fragment {

    ImageButton buttonDelete;
    Button buttonAddSet;
    List<ExerciseSetItem> exerciseSetItemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view= inflater.inflate(R.layout.fragment_new_exercise, container, false);

        buttonDelete = view.findViewById(R.id.new_exercise_delete_button);
        buttonAddSet = view.findViewById(R.id.new_exercise_add_set_button);

        exerciseSetItemList = new ArrayList<>();

        final RecyclerView recyclerView = view.findViewById(R.id.new_exercise_recycler_view);
        final ExerciseSetsAdapter exerciseSetsAdapter = new ExerciseSetsAdapter(view.getContext());//not sure if it;s ok
        recyclerView.setAdapter(exerciseSetsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));//not sure if it;s ok
        exerciseSetItemList.add(new ExerciseSetItem(1,0,0));
        exerciseSetsAdapter.setSets(exerciseSetItemList);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(view != null) {
                    container.removeViewInLayout(view);
                    exerciseSetItemList = null;
                }

            }
        });

        buttonAddSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExerciseSetItem exerciseSetItem = new ExerciseSetItem(0,0,0);
                exerciseSetItemList.add(exerciseSetItem);
                exerciseSetsAdapter.setSets(exerciseSetItemList);
            }
        });

        return view;
    }
}

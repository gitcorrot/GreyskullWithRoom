package com.corrot.room.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.corrot.room.ExerciseItem;
import com.corrot.room.ExerciseSetItem;
import com.corrot.room.adapters.ExercisesListAdapter;
import com.corrot.room.R;
import com.corrot.room.db.entity.Exercise;
import com.corrot.room.db.entity.Workout;
import com.corrot.room.utils.EntityUtils;
import com.corrot.room.utils.MyTimeUtils;
import com.corrot.room.viewmodel.ExerciseViewModel;
import com.corrot.room.viewmodel.NewWorkoutViewModel;
import com.corrot.room.viewmodel.WorkoutViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NewWorkoutActivity extends AppCompatActivity {

    public final static int FLAG_ERROR = -1;
    public final static int FLAG_ADD_WORKOUT = 0;
    public final static int FLAG_UPDATE_WORKOUT = 1;

    TextView dateTextView;
    RecyclerView exercisesRecyclerView;
    Button addExerciseButton;
    ImageButton saveWorkoutButton;

    private NewWorkoutViewModel mNewWorkoutViewModel;
    private WorkoutViewModel mWorkoutViewModel;
    private ExerciseViewModel mExerciseViewModel;

    private AppCompatActivity mActivity;
    private Date date;
    private int currentFlag;
    private Workout mWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        mActivity = this;

        dateTextView = findViewById(R.id.new_workout_date_text_view);
        exercisesRecyclerView = findViewById(R.id.exercises_recycler_view);
        addExerciseButton = findViewById(R.id.new_exercise_button);
        saveWorkoutButton = findViewById(R.id.new_workout_save_button);

        mNewWorkoutViewModel = ViewModelProviders.of(mActivity).get(NewWorkoutViewModel.class);
        mWorkoutViewModel = ViewModelProviders.of(mActivity).get(WorkoutViewModel.class);
        mExerciseViewModel = ViewModelProviders.of(mActivity).get(ExerciseViewModel.class);

        mNewWorkoutViewModel.init();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentFlag = extras.getInt("flags", FLAG_ERROR);
            Log.d("asdasd", "Activity opened with flag: " + currentFlag + "!");
            switch (currentFlag) {

                case FLAG_ERROR: {
                    Log.d("asdasd", "Error flag!");
                    break;
                }

                case FLAG_ADD_WORKOUT: {
                    date = Calendar.getInstance().getTime();
                    dateTextView.setText(MyTimeUtils.parseDate(date, "dd/MM/yyyy"));
                    break;
                }

                case FLAG_UPDATE_WORKOUT: {
                    String workoutId = extras.getString("workoutId", "no workout");
                    try {
                        mWorkout = mWorkoutViewModel.getWorkoutById(workoutId);
                        date = mWorkout.workoutDate;
                        dateTextView.setText(MyTimeUtils.parseDate(date, "dd/MM/yyyy"));
                    } catch (InterruptedException e) {
                        Log.d("asdasd", e.getMessage());
                    } catch (ExecutionException e) {
                        Log.d("asdasd", e.getMessage());
                    }

                    if (!workoutId.equals("no workout")) {
                        try {
                            List<Exercise> exercises =
                                    mExerciseViewModel.getExercisesByWorkoutId(workoutId);
                            if (exercises != null) {
                                mNewWorkoutViewModel.setExercises(EntityUtils.getExerciseItems(exercises));
                                mNewWorkoutViewModel.setSets(EntityUtils.getExerciseSetItems(exercises));
                                Log.d("asdasd", "MNEWWORKOUTVIEWMODEL UPDATING SETS AND EXERCISES");
                            }
                        } catch (InterruptedException e) {
                            Log.d("asdasd", e.getMessage());
                        } catch (ExecutionException e) {
                            Log.d("asdasd", e.getMessage());
                        }
                    }
                    break;
                }
            }
        }

        final ExercisesListAdapter exercisesListAdapter = new ExercisesListAdapter(this);
        exercisesRecyclerView.setAdapter(exercisesListAdapter);
        exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNewWorkoutViewModel.getAllExerciseItems().observe(this, new Observer<List<ExerciseItem>>() {
            @Override
            public void onChanged(@Nullable List<ExerciseItem> exerciseItems) {
                exercisesListAdapter.setExercises(exerciseItems);
            }
        }); // not sure if it's necessary

        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExercisesDialog(v.getContext()).show();
            }
        });


        saveWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ExerciseItem> exercises = mNewWorkoutViewModel.getAllExerciseItems().getValue();
                if(exercises != null) {

                    switch (currentFlag) {

                        case FLAG_ADD_WORKOUT:
                            mWorkout = new Workout(date);
                            mWorkoutViewModel.insertSingleWorkout(mWorkout);
                            break;

                        case FLAG_UPDATE_WORKOUT:
                            // TODO: update only if workout has changed.
                            // TODO: what if exercise is removed?
                            mWorkoutViewModel.updateWorkout(mWorkout);
                            break;
                    }

                    for(ExerciseItem e : exercises) {

                        // Getting sets of exercise
                        List<ExerciseSetItem> sets = mNewWorkoutViewModel.getSetsByExercisePosition(e.position);
                        List<Integer> reps = new ArrayList<>();
                        List<Float> weights = new ArrayList<>();
                        if(sets != null) {
                            for(ExerciseSetItem esi : sets) {
                                reps.add(esi.reps);
                                weights.add(esi.weight);
                            }
                        }

                        Log.d("asdasd", "Gettign exercise of ID: " + e.position);
                        Exercise newExercise = new Exercise(e.exerciseId, mWorkout.id,e.name, weights, reps);

                        switch (currentFlag) {
                            case FLAG_ADD_WORKOUT:
                                mExerciseViewModel.insertSingleExercise(newExercise);
                                Log.d("asdasd", "Exercise added successfully!");
                                break;
                            case FLAG_UPDATE_WORKOUT:
                                mExerciseViewModel.updateSingleExercise(newExercise);
                                Log.d("asdasd", "Exercise updated successfully!");
                                break;
                        }
                    }
                }
                Toast.makeText(mActivity, "Exercise added successfully!",
                        Toast.LENGTH_SHORT).show();
                mActivity.finishAndRemoveTask(); //.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNewWorkoutViewModel.destroyInstance();
    }

    private AlertDialog showExercisesDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Which exercise you want to add?");

        // TODO: get it from shared preferences
        final String[] exercises = {"Squat", "Dead lift", "Bench press"};

        builder.setItems(exercises, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mNewWorkoutViewModel.addExercise(new ExerciseItem(exercises[which]));
            }
        });
        return builder.create();
    }
}

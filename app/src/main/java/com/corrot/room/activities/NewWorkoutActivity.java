package com.corrot.room.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.corrot.room.ExerciseItem;
import com.corrot.room.ExerciseSetItem;
import com.corrot.room.R;
import com.corrot.room.adapters.ExercisesListAdapter;
import com.corrot.room.db.entity.Exercise;
import com.corrot.room.db.entity.Routine;
import com.corrot.room.db.entity.Workout;
import com.corrot.room.utils.EntityUtils;
import com.corrot.room.utils.MyTimeUtils;
import com.corrot.room.utils.PreferencesManager;
import com.corrot.room.viewmodel.ExerciseViewModel;
import com.corrot.room.viewmodel.NewWorkoutViewModel;
import com.corrot.room.viewmodel.WorkoutViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class NewWorkoutActivity extends AppCompatActivity {

    public final static int FLAG_ERROR = -1;
    public final static int FLAG_ADD_WORKOUT = 0;
    public final static int FLAG_UPDATE_WORKOUT = 1;

    TextView labelEditText;
    TextView dateTextView;
    RecyclerView exercisesRecyclerView;
    Button addExerciseButton;
    ImageButton saveWorkoutButton;

    private NewWorkoutViewModel mNewWorkoutViewModel;
    private WorkoutViewModel mWorkoutViewModel;
    private ExerciseViewModel mExerciseViewModel;

    private Workout mWorkout;
    private Date date;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener dateListener;
    private TimePickerDialog.OnTimeSetListener timeListener;
    private PreferencesManager pm;
    private int currentFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        pm = PreferencesManager.getInstance();

        dateTextView = findViewById(R.id.new_workout_date_text_view);
        labelEditText = findViewById(R.id.new_workout_label_edit_text);
        exercisesRecyclerView = findViewById(R.id.exercises_recycler_view);
        addExerciseButton = findViewById(R.id.new_exercise_button);
        saveWorkoutButton = findViewById(R.id.new_workout_save_button);

        mNewWorkoutViewModel = ViewModelProviders.of(this).get(NewWorkoutViewModel.class);
        mWorkoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);
        mExerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);

        mNewWorkoutViewModel.init();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentFlag = extras.getInt("flags", FLAG_ERROR);
            switch (currentFlag) {
                case FLAG_ERROR: {
                    Log.e("NewWorkoutActivity", "Error flag!");
                    break;
                }
                case FLAG_ADD_WORKOUT: {
                    date = Calendar.getInstance().getTime();
                    dateTextView.setText(MyTimeUtils.parseDate(date, MyTimeUtils.MAIN_FORMAT));

                    try {
                        Routine routine = (Routine) extras.getSerializable("routine");
                        if (routine != null) {
                            labelEditText.setText(routine.label);
                            List<ExerciseItem> exercises =
                                    EntityUtils.getExerciseItemsFromRoutine(routine);
                            if (exercises != null) {
                                mNewWorkoutViewModel.setExercises(exercises);

                                for (int pos = 0; pos < exercises.size(); pos++) {
                                    for (int i = 0; i < routine.sets.get(pos); i++) {
                                        mNewWorkoutViewModel.addSet(new ExerciseSetItem(pos));
                                    }
                                }
                            }
                        }
                    } catch (ClassCastException e) {
                        Log.d("NewWorkoutActivity", e.getLocalizedMessage());
                    }
                    break;
                }
                case FLAG_UPDATE_WORKOUT: {
                    String workoutId = extras.getString("workoutId", "no workout");
                    String workoutLabel = extras.getString("label", "Normal workout");

                    mWorkoutViewModel.getWorkoutById(workoutId, workout -> {
                        mWorkout = workout;
                        date = mWorkout.workoutDate;
                        labelEditText.setText(mWorkout.label);
                        dateTextView.setText(MyTimeUtils.parseDate(date, MyTimeUtils.MAIN_FORMAT));

                        if (!workoutLabel.equals("Normal workout")) {
                            labelEditText.setText(workoutLabel);
                        }

                        if (!workoutId.equals("no workout")) {
                            mExerciseViewModel.getExercisesByWorkoutId(workoutId, exercises -> {
                                mNewWorkoutViewModel.setExercises(EntityUtils.getExerciseItems(exercises));
                                mNewWorkoutViewModel.setSets(EntityUtils.getExerciseSetItems(exercises));
                            });
                        }
                    });
                    break;
                }
            }
        }

        final ExercisesListAdapter exercisesListAdapter = new ExercisesListAdapter(this);
        exercisesRecyclerView.setAdapter(exercisesListAdapter);
        exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNewWorkoutViewModel.getAllExerciseItems().observe(this, exercisesListAdapter::setExercises);

        dateTextView.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT > 23) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        this,
                        dateListener,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();
            }
        });

        dateListener = (datePicker, year, month, dayOfMonth) -> {
            calendar = new GregorianCalendar(
                    datePicker.getYear(),
                    datePicker.getMonth(),
                    datePicker.getDayOfMonth()
            );
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this,
                    timeListener,
                    Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                    Calendar.getInstance().get(Calendar.MINUTE),
                    true);
            // Set dialog in center of screen.
            Window window = timePickerDialog.getWindow();
            if (window != null) {
                window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);
            }
            timePickerDialog.show();
        };

        timeListener = (view, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            date = calendar.getTime();
            mNewWorkoutViewModel.isChanged = true;
            dateTextView.setText(MyTimeUtils.parseDate(date, MyTimeUtils.MAIN_FORMAT));
        };

        addExerciseButton.setOnClickListener(v ->
                showExercisesDialog(v.getContext()).show());

        saveWorkoutButton.setOnClickListener(v -> {
            // do it to lose focus from editText to save its value.
            v.setFocusableInTouchMode(true);
            v.requestFocus();
            saveWorkout();
            this.finishAndRemoveTask();
        });
    }

    @Override
    public void onBackPressed() {
        // do it to lose focus from editText to save its value.
        saveWorkoutButton.setFocusableInTouchMode(true);
        saveWorkoutButton.requestFocus();

        if (mNewWorkoutViewModel.isChanged) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Do you want to save this workout?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                saveWorkout();
                this.finishAndRemoveTask();
            });
            builder.setNegativeButton("No", (dialog, which) ->
                    this.finishAndRemoveTask());
            builder.setNeutralButton("Cancel", null);
            builder.create().show();
        } else {
            this.finishAndRemoveTask();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNewWorkoutViewModel.destroyInstance();
    }

    private AlertDialog showExercisesDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Which exercise you want to add?");

        final String[] exercises = pm.getExercises();

        builder.setItems(exercises, (dialog, which) -> {
            if (exercises != null) {
                ExerciseItem exerciseItem = new ExerciseItem(exercises[which]);
                mNewWorkoutViewModel.addExercise(exerciseItem);
            }
        });
        return builder.create();
    }

    private void saveWorkout() {
        List<ExerciseItem> exercises = mNewWorkoutViewModel.getAllExerciseItems().getValue();
        if (exercises != null) {
            String label = labelEditText.getText().toString();
            switch (currentFlag) {
                case FLAG_ADD_WORKOUT:
                    mWorkout = new Workout(date, label);
                    mWorkoutViewModel.insertSingleWorkout(mWorkout);
                    break;
                case FLAG_UPDATE_WORKOUT:
                    mWorkout.workoutDate = date;
                    mWorkout.label = label;
                    mWorkoutViewModel.updateWorkout(mWorkout);
                    mExerciseViewModel.deleteAllExercisesByWorkoutId(mWorkout.id);
                    break;
            }

            List<Exercise> updatedExercises = new ArrayList<>();

            for (ExerciseItem e : exercises) {

                // Getting sets of exercise
                List<ExerciseSetItem> sets =
                        mNewWorkoutViewModel.getSetsByExercisePosition(e.position);
                List<Integer> reps = new ArrayList<>();
                List<Float> weights = new ArrayList<>();

                if (sets != null) {
                    for (ExerciseSetItem esi : sets) {
                        reps.add(esi.reps);
                        weights.add(esi.weight);
                    }
                }

                Exercise newExercise = new Exercise(mWorkout.id,
                        mWorkout.workoutDate, mWorkout.label, e.name, weights, reps);

                switch (currentFlag) {
                    case FLAG_ADD_WORKOUT:
                        mExerciseViewModel.insertSingleExercise(newExercise);
                        break;
                    case FLAG_UPDATE_WORKOUT:
                        updatedExercises.add(newExercise);
                        break;
                }
            }

            if (currentFlag == FLAG_UPDATE_WORKOUT) {
                mExerciseViewModel.deleteAllExercisesByWorkoutId(mWorkout.id);
                mExerciseViewModel.insertMultipleExercises(updatedExercises);
            }
        }

        String action = currentFlag == FLAG_ADD_WORKOUT ? "added" : "updated";
        Toast.makeText(this, "Exercise " + action + " successfully!",
                Toast.LENGTH_SHORT).show();
    }
}

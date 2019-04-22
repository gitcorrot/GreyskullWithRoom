package com.corrot.room.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.corrot.room.dialogs.NewExerciseNameDialog;
import com.corrot.room.R;
import com.corrot.room.db.WorkoutsDatabase;
import com.corrot.room.db.entity.Routine;
import com.corrot.room.fragments.BodyFragment;
import com.corrot.room.fragments.HistoryFragment;
import com.corrot.room.fragments.HomeFragment;
import com.corrot.room.fragments.RoutinesFragment;
import com.corrot.room.fragments.StatsFragment;
import com.corrot.room.utils.PreferencesManager;
import com.corrot.room.viewmodel.RoutineViewModel;
import com.corrot.room.viewmodel.WorkoutViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String CURRENT_FRAGMENT_KEY = "current fragment";

    private final Fragment homeFragment = new HomeFragment();
    private final Fragment routinesFragment = new RoutinesFragment();
    private final Fragment historyFragment = new HistoryFragment();
    private final Fragment statsFragment = new StatsFragment();
    private final Fragment bodyFragment = new BodyFragment();
    private Fragment currentFragment = homeFragment;

    private WorkoutViewModel mWorkoutViewModel;

    private List<Routine> routinesList;
    private Toolbar toolbar;
    FloatingActionButton floatingButton;
    private AppCompatActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivity = this;
        routinesList = new ArrayList<>();

        mWorkoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);
        RoutineViewModel mRoutineViewModel = ViewModelProviders.of(this).get(RoutineViewModel.class);

        mRoutineViewModel.getAllRoutines().observe(this, routines ->
                routinesList = routines);

        PreferencesManager.init(getApplicationContext());
        PreferencesManager pm = PreferencesManager.getInstance();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("SIMPLE TRAINING LOG");
        setSupportActionBar(toolbar);

        if (pm.isFirstStart()) {
            firstStartInit();
        }

        floatingButton = findViewById(R.id.floating_button);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navItemListener);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_activity_fragment_container, homeFragment)   // don't hide
                .add(R.id.main_activity_fragment_container, routinesFragment)
                .add(R.id.main_activity_fragment_container, historyFragment)
                .add(R.id.main_activity_fragment_container, statsFragment)
                .add(R.id.main_activity_fragment_container, bodyFragment)
                .hide(routinesFragment)
                .hide(historyFragment)
                .hide(statsFragment)
                .hide(bodyFragment)
                .commit();

        // Start NewWorkoutActivity on floatingButton click
        floatingButton.setOnClickListener(v -> {
            // Choose between routines and normal training
            showExercisesDialog(mActivity).show(); //??
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navItemListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_bar_home:
                            getSupportFragmentManager().beginTransaction()
                                    .hide(currentFragment)
                                    .show(homeFragment)
                                    .commit();
                            currentFragment = homeFragment;
                            floatingButton.show();
                            return true;
                        case R.id.navigation_bar_routines:
                            getSupportFragmentManager().beginTransaction()
                                    .hide(currentFragment)
                                    .show(routinesFragment)
                                    .commit();
                            currentFragment = routinesFragment;
                            floatingButton.show();
                            return true;
                        case R.id.navigation_bar_history:
                            getSupportFragmentManager().beginTransaction()
                                    .hide(currentFragment)
                                    .show(historyFragment)
                                    .commit();
                            currentFragment = historyFragment;
                            floatingButton.show();
                            return true;
                        case R.id.navigation_bar_stats:
                            getSupportFragmentManager().beginTransaction()
                                    .hide(currentFragment)
                                    .show(statsFragment)
                                    .commit();
                            currentFragment = statsFragment;
                            floatingButton.show();
                            return true;
                        case R.id.navigation_bar_body:
                            getSupportFragmentManager().beginTransaction()
                                    .hide(currentFragment)
                                    .show(bodyFragment)
                                    .commit();
                            currentFragment = bodyFragment;
                            floatingButton.hide();
                            return true;
                    }
                    return false;
                }
            };

    private void firstStartInit() {
        // TODO: if it is first applications start add example routines
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_settings_add_new: {
                if (getFragmentManager() != null) {
                    NewExerciseNameDialog dialog = new NewExerciseNameDialog();
                    dialog.show(getSupportFragmentManager(), "new exercise dialog");
                } else
                    Toast.makeText(this, "Can't find FragmentManager!",
                            Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.toolbar_settings_delete_all: {
                mWorkoutViewModel.deleteAll();
                Toast.makeText(this,
                        "All workouts deleted!", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.toolbar_settings_backup: {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        2);
                WorkoutsDatabase.backup(this, "mojbackup");
                //Toast.makeText(this, "Backup done!", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.toolbar_settings_restore: {

                Toast.makeText(this, "Workouts restored!", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        int fragment = currentFragment.getId();
        outState.putInt(CURRENT_FRAGMENT_KEY, fragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            int fragment = savedInstanceState.getInt(CURRENT_FRAGMENT_KEY);
            switch (fragment) {
                case R.id.navigation_bar_home:
                    getSupportFragmentManager().beginTransaction()
                            .show(homeFragment)
                            .hide(routinesFragment)
                            .hide(historyFragment)
                            .hide(statsFragment)
                            .hide(bodyFragment)
                            .commit();
                    toolbar.setTitle("Home");
                case R.id.navigation_bar_routines:
                    getSupportFragmentManager().beginTransaction()
                            .hide(homeFragment)
                            .show(routinesFragment)
                            .hide(historyFragment)
                            .hide(statsFragment)
                            .hide(bodyFragment)
                            .commit();
                    toolbar.setTitle("Routines");
                case R.id.navigation_bar_history:
                    getSupportFragmentManager().beginTransaction()
                            .hide(homeFragment)
                            .hide(routinesFragment)
                            .show(historyFragment)
                            .hide(statsFragment)
                            .hide(bodyFragment)
                            .commit();
                    toolbar.setTitle("History");
                case R.id.navigation_bar_stats:
                    getSupportFragmentManager().beginTransaction()
                            .hide(homeFragment)
                            .hide(routinesFragment)
                            .hide(historyFragment)
                            .show(statsFragment)
                            .hide(bodyFragment)
                            .commit();
                    toolbar.setTitle("Stats");
                case R.id.navigation_bar_body:
                    getSupportFragmentManager().beginTransaction()
                            .hide(homeFragment)
                            .hide(routinesFragment)
                            .hide(historyFragment)
                            .hide(statsFragment)
                            .show(bodyFragment)
                            .commit();
                    floatingButton.hide();
                    toolbar.setTitle("Body");
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    private AlertDialog showExercisesDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose workout");

        List<String> routines = new ArrayList<>();
        routines.add("Normal workout");
        if (routinesList != null) {
            for (Routine r : routinesList) {
                routines.add(r.label);
            }
        }

        String[] routinesArray = new String[routines.size()];
        routines.toArray(routinesArray);

        builder.setItems(routinesArray, (dialog, which) -> {
            if (routinesArray.length >= which) {
                Intent newWorkoutIntent = new Intent(this, NewWorkoutActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("flags", NewWorkoutActivity.FLAG_ADD_WORKOUT);
                bundle.putSerializable("routine", routinesList.get(which - 1));
                newWorkoutIntent.putExtras(bundle);
                startActivity(newWorkoutIntent);
            }
        });
        return builder.create();
    }

    @Override
    protected void onDestroy() {
        WorkoutsDatabase.destroyInstance();
        super.onDestroy();
    }
}

package com.corrot.room.activities;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.corrot.room.fragments.BodyFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.corrot.room.NewExerciseNameDialog;
import com.corrot.room.fragments.HistoryFragment;
import com.corrot.room.fragments.HomeFragment;
import com.corrot.room.R;
import com.corrot.room.fragments.StatsFragment;
import com.corrot.room.db.WorkoutsDatabase;
import com.corrot.room.utils.PreferencesManager;
import com.corrot.room.viewmodel.WorkoutViewModel;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton floatingButton;
    private final Fragment homeFragment = new HomeFragment();
    private final Fragment historyFragment = new HistoryFragment();
    private final Fragment statsFragment = new StatsFragment();
    private final Fragment bodyFragment = new BodyFragment();
    private Fragment currentFragment = homeFragment;
    WorkoutViewModel mWorkoutViewModel;
    private final static String CURRENT_FRAGMENT_KEY = "current fragment";
    PreferencesManager pm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWorkoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);
        pm = PreferencesManager.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PreferencesManager.init(this);

        boolean firstStart = pm.isFirstStart();
        if (firstStart) {
            firstStartInit();
        }

        floatingButton = findViewById(R.id.floating_button);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navItemListener);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_activity_fragment_container, homeFragment)   // don't hide
                .add(R.id.main_activity_fragment_container, historyFragment)
                .add(R.id.main_activity_fragment_container, statsFragment)
                .add(R.id.main_activity_fragment_container, bodyFragment)
                .hide(historyFragment)
                .hide(statsFragment)
                .hide(bodyFragment)
                .commit();

        // Start NewWorkoutActivity on floatingButton click
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newWorkoutIntent =
                        new Intent(MainActivity.this, NewWorkoutActivity.class);
                newWorkoutIntent.putExtra("flags", NewWorkoutActivity.FLAG_ADD_WORKOUT);
                MainActivity.this.startActivity(newWorkoutIntent);
            }
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
                            return true;
                        case R.id.navigation_bar_history:
                            getSupportFragmentManager().beginTransaction()
                                    .hide(currentFragment)
                                    .show(historyFragment)
                                    .commit();
                            currentFragment = historyFragment;
                            return true;
                        case R.id.navigation_bar_stats:
                            getSupportFragmentManager().beginTransaction()
                                    .hide(currentFragment)
                                    .show(statsFragment)
                                    .commit();
                            currentFragment = statsFragment;
                            return true;
                        case R.id.navigation_bar_body:
                            getSupportFragmentManager().beginTransaction()
                                    .hide(currentFragment)
                                    .show(bodyFragment)
                                    .commit();
                            currentFragment = bodyFragment;
                            return true;
                    }
                    return false;
                }
            };

    private void firstStartInit() {
        pm.setFirstStart(false);

        final String[] exercises = {
                "Squats", "Deadlift", "Bench Press", "Barbell Row",
                "Pull-ups", "Overhead Press"
        };
        pm.saveExercises(exercises);
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        int fragment = -1;
        if (currentFragment.equals(homeFragment)) {
            fragment = R.id.navigation_bar_home;
        } else if (currentFragment.equals(historyFragment)) {
            fragment = R.id.navigation_bar_history;
        } else if (currentFragment.equals(statsFragment)) {
            fragment = R.id.navigation_bar_stats;
        } else if (currentFragment.equals(bodyFragment)) {
            fragment = R.id.navigation_bar_body;
        }

        if (fragment != -1)
            outState.putInt(CURRENT_FRAGMENT_KEY, fragment);
        else  {
            Log.d("MainActivity", "onSaveInstanceState: NO FRAGMENT TO SAVE.");
        }
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
                            .hide(historyFragment)
                            .hide(statsFragment)
                            .hide(bodyFragment)
                            .commit();
                case R.id.navigation_bar_history:
                    getSupportFragmentManager().beginTransaction()
                            .hide(homeFragment)
                            .show(historyFragment)
                            .hide(statsFragment)
                            .hide(bodyFragment)
                            .commit();
                case R.id.navigation_bar_stats:
                    getSupportFragmentManager().beginTransaction()
                            .hide(homeFragment)
                            .hide(historyFragment)
                            .show(statsFragment)
                            .hide(bodyFragment)
                            .commit();
                case R.id.navigation_bar_body:
                    getSupportFragmentManager().beginTransaction()
                            .hide(homeFragment)
                            .hide(historyFragment)
                            .hide(statsFragment)
                            .show(bodyFragment)
                            .commit();
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        WorkoutsDatabase.destroyInstance();
        super.onDestroy();
    }
}

package com.corrot.room.activities;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.corrot.room.fragments.BodyFragment;
import com.corrot.room.fragments.WorkoutsFragment;
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

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton floatingButton;
    private final Fragment homeFragment = new HomeFragment();
    private final Fragment workoutsFragment = new WorkoutsFragment();
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
        PreferencesManager.init(getApplicationContext());
        pm = PreferencesManager.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (pm.isFirstStart()) {
            firstStartInit();
        }

        floatingButton = findViewById(R.id.floating_button);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navItemListener);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_activity_fragment_container, homeFragment)   // don't hide
                .add(R.id.main_activity_fragment_container, workoutsFragment)
                .add(R.id.main_activity_fragment_container, historyFragment)
                .add(R.id.main_activity_fragment_container, statsFragment)
                .add(R.id.main_activity_fragment_container, bodyFragment)
                .hide(workoutsFragment)
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
                            floatingButton.show();
                            return true;
                        case R.id.navigation_bar_workouts:
                            getSupportFragmentManager().beginTransaction()
                                    .hide(currentFragment)
                                    .show(workoutsFragment)
                                    .commit();
                            currentFragment = workoutsFragment;
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
        // init
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
                            .hide(workoutsFragment)
                            .hide(historyFragment)
                            .hide(statsFragment)
                            .hide(bodyFragment)
                            .commit();
                case R.id.navigation_bar_workouts:
                    getSupportFragmentManager().beginTransaction()
                            .hide(homeFragment)
                            .show(workoutsFragment)
                            .hide(historyFragment)
                            .hide(statsFragment)
                            .hide(bodyFragment)
                            .commit();
                case R.id.navigation_bar_history:
                    getSupportFragmentManager().beginTransaction()
                            .hide(homeFragment)
                            .hide(workoutsFragment)
                            .show(historyFragment)
                            .hide(statsFragment)
                            .hide(bodyFragment)
                            .commit();
                case R.id.navigation_bar_stats:
                    getSupportFragmentManager().beginTransaction()
                            .hide(homeFragment)
                            .hide(workoutsFragment)
                            .hide(historyFragment)
                            .show(statsFragment)
                            .hide(bodyFragment)
                            .commit();
                case R.id.navigation_bar_body:
                    getSupportFragmentManager().beginTransaction()
                            .hide(homeFragment)
                            .hide(workoutsFragment)
                            .hide(historyFragment)
                            .hide(statsFragment)
                            .show(bodyFragment)
                            .commit();
                    floatingButton.hide();
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

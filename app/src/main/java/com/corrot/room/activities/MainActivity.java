package com.corrot.room.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class MainActivity extends AppCompatActivity {

    FloatingActionButton floatingButton;
    private final Fragment homeFragment = new HomeFragment();
    private final Fragment historyFragment = new HistoryFragment();
    private final Fragment statsFragment = new StatsFragment();
    private Fragment currentFragment = homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PreferencesManager.init(this);

        boolean firstStart = PreferencesManager.isFirstStart();
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
                .hide(historyFragment)
                .hide(statsFragment)
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
                    }
                    return false;
                }
            };

    private void firstStartInit() {
        PreferencesManager.setFirstStart(false);

        final String[] exercises = {
                "Squats", "Deadlift", "Bench Press", "Barbell Row",
                "Pull-ups", "Overhead Press"
        };
        PreferencesManager.saveExercises(exercises);
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        WorkoutsDatabase.destroyInstance();
        super.onDestroy();
    }
}

package com.corrot.room.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.corrot.room.R;
import com.corrot.room.db.WorkoutsDatabase;
import com.corrot.room.db.entity.Routine;
import com.corrot.room.dialogs.NewExerciseNameDialog;
import com.corrot.room.fragments.BodyFragment;
import com.corrot.room.fragments.HistoryFragment;
import com.corrot.room.fragments.HomeFragment;
import com.corrot.room.fragments.RoutinesFragment;
import com.corrot.room.fragments.StatsFragment;
import com.corrot.room.repository.RoutinesRepository;
import com.corrot.room.repository.WorkoutsRepository;
import com.corrot.room.utils.PreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

    PreferencesManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferencesManager.init(getApplicationContext());
        pm = PreferencesManager.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        if (pm.isFirstStart(getApplicationContext())) {
            firstStartInit();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navItemListener);

        FragmentManager fm = getSupportFragmentManager();

        if (fm.findFragmentByTag("home") == null)
            fm.beginTransaction().add(R.id.main_activity_fragment_container, homeFragment, "home").commit();
        if (fm.findFragmentByTag("routines") == null)
            fm.beginTransaction().add(R.id.main_activity_fragment_container, routinesFragment, "routines").commit();
        if (fm.findFragmentByTag("history") == null)
            fm.beginTransaction().add(R.id.main_activity_fragment_container, historyFragment, "history").commit();
        if (fm.findFragmentByTag("stats") == null)
            fm.beginTransaction().add(R.id.main_activity_fragment_container, statsFragment, "stats").commit();
        if (fm.findFragmentByTag("body") == null)
            fm.beginTransaction().add(R.id.main_activity_fragment_container, bodyFragment, "body").commit();

        fm.beginTransaction()
                .hide(routinesFragment)
                .hide(historyFragment)
                .hide(statsFragment)
                .hide(bodyFragment)
                .show(currentFragment)
                .commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navItemListener = item -> {
        FragmentManager fm = getSupportFragmentManager();
        switch (item.getItemId()) {
            case R.id.navigation_bar_home:
                if (!homeFragment.isVisible()) {
                    fm.beginTransaction()
                            .hide(currentFragment)
                            .show(homeFragment)
                            .commit();
                    currentFragment = homeFragment;
                }
                return true;
            case R.id.navigation_bar_routines:
                if (!routinesFragment.isVisible()) {
                    fm.beginTransaction()
                            .hide(currentFragment)
                            .show(routinesFragment)
                            .commit();
                    currentFragment = routinesFragment;
                }
                return true;
            case R.id.navigation_bar_history:
                if (!historyFragment.isVisible()) {
                    fm.beginTransaction()
                            .hide(currentFragment)
                            .show(historyFragment)
                            .commit();
                    currentFragment = historyFragment;
                }
                return true;
            case R.id.navigation_bar_stats:
                if (!statsFragment.isVisible()) {
                    fm.beginTransaction()
                            .hide(currentFragment)
                            .show(statsFragment)
                            .commit();
                    currentFragment = statsFragment;
                }
                return true;
            case R.id.navigation_bar_body:
                if (!bodyFragment.isVisible()) {
                    fm.beginTransaction()
                            .hide(currentFragment)
                            .show(bodyFragment)
                            .commit();
                    currentFragment = bodyFragment;
                }
                return true;
        }
        return false;
    };

    private void firstStartInit() {
        List<String> exercises = new ArrayList<>();
        exercises.add(getString(R.string.squats));
        exercises.add(getString(R.string.bench_press));
        exercises.add(getString(R.string.barbell_row));
        List<Integer> sets = new ArrayList<>();
        sets.add(5);
        sets.add(5);
        sets.add(5);
        Routine r1 = new Routine(getString(R.string.stronglifts_5x5_a), exercises, sets);
        exercises.clear();
        exercises.add(getString(R.string.squats));
        exercises.add(getString(R.string.overhead_press));
        exercises.add(getString(R.string.deadlift));
        sets.clear();
        sets.add(5);
        sets.add(5);
        sets.add(1);
        Routine r2 = new Routine(getString(R.string.stronglifts_5x5_b), exercises, sets);
        RoutinesRepository repo = new RoutinesRepository(getApplication());
        repo.insertSingleRoutine(r1);
        repo.insertSingleRoutine(r2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        // Below code is to show icons
        MenuBuilder menuBuilder = (MenuBuilder) menu;
        menuBuilder.setOptionalIconsVisible(true);

        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_settings_exercises: {
                if (getFragmentManager() != null) {
                    NewExerciseNameDialog dialog = new NewExerciseNameDialog();
                    dialog.show(getSupportFragmentManager(), "new exercise dialog");
                } else
                    Toast.makeText(this, "Can't find FragmentManager!",
                            Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.toolbar_settings_change_units: {
                switch (pm.getUnitSystem()) {
                    case "kg": {
                        pm.setUnitSystem("lbs");
                        break;
                    }
                    case "lbs": {
                        pm.setUnitSystem("kg");
                        break;
                    }
                    default: {
                        Log.e("MainActivity", "Wrong unit system!");
                        break;
                    }
                }
                // Restart app
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                if (i != null) {
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
                break;
            }
            case R.id.toolbar_settings_delete_all: {
                showYesNoDialog(this);
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

    private void showYesNoDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

        builder.setMessage("Are you sure you want to delete all workouts?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", (dialog, which) -> {
                    new WorkoutsRepository(getApplication()).deleteAll();
                    Toast.makeText(context, "All workouts deleted!", Toast.LENGTH_SHORT).show();
                }).show().create();
        builder.show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        int fragment = 0;
        if (currentFragment.equals(homeFragment)) {
            fragment = R.layout.fragment_home;
        } else if (currentFragment.equals(routinesFragment)) {
            fragment = R.layout.fragment_routines;
        } else if (currentFragment.equals(historyFragment)) {
            fragment = R.layout.fragment_history;
        } else if (currentFragment.equals(statsFragment)) {
            fragment = R.layout.fragment_stats;
        } else if (currentFragment.equals(bodyFragment)) {
            fragment = R.layout.fragment_body;
        }

        //TODO: DO IT ONLY WHEN SCREEN ROTATION CHANGE! https://stackoverflow.com/questions/15496362/handle-fragment-duplication-on-screen-rotate-with-sample-code
        removeAllFragmentsFromContainer();
        outState.putInt(CURRENT_FRAGMENT_KEY, fragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void removeAllFragmentsFromContainer() {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        List<Fragment> fragments = fm.getFragments();
        for(Fragment f : fragments)
            ft.remove(f);

        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            int fragment = savedInstanceState.getInt(CURRENT_FRAGMENT_KEY);
            if (fragment == R.layout.fragment_home && !homeFragment.isVisible()) {
                getSupportFragmentManager().beginTransaction()
                        .show(homeFragment)
                        .hide(routinesFragment)
                        .hide(historyFragment)
                        .hide(statsFragment)
                        .hide(bodyFragment)
                        .commit();
                currentFragment = homeFragment;
            } else if (fragment == R.layout.fragment_routines && !routinesFragment.isVisible()) {
                getSupportFragmentManager().beginTransaction()
                        .hide(homeFragment)
                        .show(routinesFragment)
                        .hide(historyFragment)
                        .hide(statsFragment)
                        .hide(bodyFragment)
                        .commit();
                currentFragment = routinesFragment;
            } else if (fragment == R.layout.fragment_history && !historyFragment.isVisible()) {
                getSupportFragmentManager().beginTransaction()
                        .hide(homeFragment)
                        .hide(routinesFragment)
                        .show(historyFragment)
                        .hide(statsFragment)
                        .hide(bodyFragment)
                        .commit();
                currentFragment = historyFragment;
            } else if (fragment == R.layout.fragment_stats && !statsFragment.isVisible()) {
                getSupportFragmentManager().beginTransaction()
                        .hide(homeFragment)
                        .hide(routinesFragment)
                        .hide(historyFragment)
                        .show(statsFragment)
                        .hide(bodyFragment)
                        .commit();
                currentFragment = statsFragment;
            } else if (fragment == R.layout.fragment_body && !bodyFragment.isVisible()) {
                getSupportFragmentManager().beginTransaction()
                        .hide(homeFragment)
                        .hide(routinesFragment)
                        .hide(historyFragment)
                        .hide(statsFragment)
                        .show(bodyFragment)
                        .commit();
                currentFragment = bodyFragment;
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

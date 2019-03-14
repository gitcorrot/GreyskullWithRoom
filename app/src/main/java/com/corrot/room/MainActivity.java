package com.corrot.room;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton floatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingButton = findViewById(R.id.floating_button);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navItemListener);


        // Initiate home fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_activity_fragment_container, new HomeFragment()).commit();

        // Start NewWorkoutActivity on floatingButton click
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newWorkoutIntent =
                        new Intent(MainActivity.this, AddNewActivity.class);
                MainActivity.this.startActivity(newWorkoutIntent);
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navItemListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_bar_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.navigation_bar_history:
                    selectedFragment = new HistoryFragment();
                    break;
                case R.id.navigation_bar_stats:
                    selectedFragment = new StatsFragment();
                    break;
            }

            if(selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_activity_fragment_container, selectedFragment).commit();
                return true;
            }
            else {
                return false;
            }
        }
    };

    @Override
    protected void onDestroy() {
        WorkoutsDatabase.destroyInstance();
        super.onDestroy();
    }
}

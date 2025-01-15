package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set default fragment to "Add Truck"
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, new AddTruckFragment())
                .commit();

        // Set up BottomNavigationView listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_add_truck:
                    selectedFragment = new AddTruckFragment();
                    break;
                case R.id.nav_my_trucks:
                    selectedFragment = new MyTrucksFragment();
                    break;
                case R.id.nav_favorites:
                    selectedFragment = new FavoritesFragment();
                    break;
            }

            // Replace the fragment if a valid one is selected
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, selectedFragment)
                        .commit();
            }

            return true; // Indicate the event has been handled
        });
    }
}

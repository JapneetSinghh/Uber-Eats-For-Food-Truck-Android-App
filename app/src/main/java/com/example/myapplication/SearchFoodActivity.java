package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapters.SearchFoodAdapter;
import com.example.myapplication.fragements.FoodFragment;
import com.example.myapplication.models.SearchFoodModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFoodActivity extends AppCompatActivity {

    private EditText searchBar;
    private RecyclerView recyclerView;
    private SearchFoodAdapter adapter;
    private List<SearchFoodModel> foodTruckList;
    private DatabaseReference foodTruckReviewsRef, usersRef;
    private ProgressBar searchFoodBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);

        // Initialize views
        searchBar = findViewById(R.id.etSearchTrucks);
        recyclerView = findViewById(R.id.recyclerViewSearchResults);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchFoodBar = findViewById(R.id.progressBarSearchFood);

        // Set up the toolbar
        // In SearchFoodActivity, inside onCreate method for the toolbar back button click
        Toolbar toolbar = findViewById(R.id.toolbarTruck);
        setSupportActionBar(toolbar);

// Handle back icon click
        toolbar.setNavigationOnClickListener(v -> {
            // Start MainActivity and pass a flag to show the FoodFragment
            Intent intent = new Intent(SearchFoodActivity.this, MainActivity.class);
            intent.putExtra("show_food_fragment", true); // Flag to show FoodFragment
            startActivity(intent);
            finish();  // Finish SearchFoodActivity
        });



        // Retrieve food name passed from previous activity
        String foodName = getIntent().getStringExtra("food_name");

        foodTruckReviewsRef = FirebaseDatabase.getInstance().getReference("FoodTruckReviews");
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        // Set the food name in the search bar
        if (foodName != null && !foodName.isEmpty()) {
            searchBar.setText(foodName);
            performSearch(foodName); // Perform search with the food name
        }

        // Initialize the food truck list
        foodTruckList = new ArrayList<>();
        adapter = new SearchFoodAdapter(this, foodTruckList);
        recyclerView.setAdapter(adapter);

        // Handle text changes in the search bar
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    searchFoodBar.setVisibility(View.VISIBLE); // Show ProgressBar during search
                    performSearch(query);
                } else {
                    foodTruckList.clear();
                    adapter.notifyDataSetChanged();
                    searchFoodBar.setVisibility(View.GONE); // Hide ProgressBar if search is cleared
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, new FoodFragment())
                .addToBackStack(null)  // Optional: Adds the fragment to the back stack
                .commit();
    }


    // Perform the search query based on user input
    private void performSearch(String query) {
        if (query.isEmpty()) {
            foodTruckList.clear();
            adapter.notifyDataSetChanged();
            searchFoodBar.setVisibility(View.GONE);  // Hide ProgressBar
            return;
        }

        Toast.makeText(this, "Searching, please wait...", Toast.LENGTH_SHORT).show();

        Log.d("SearchFoodActivity", "Performing search with query: " + query);

        // Query Users node for food truck details
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodTruckList.clear();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    DataSnapshot truckDetails = userSnapshot.child("Food Truck Details");

                    if (truckDetails.exists()) {
                        String dish1 = truckDetails.child("specialDish1").getValue(String.class);
                        String dish2 = truckDetails.child("specialDish2").getValue(String.class);
                        String dish3 = truckDetails.child("specialDish3").getValue(String.class);

                        if ((dish1 != null && dish1.toLowerCase().contains(query.toLowerCase())) ||
                                (dish2 != null && dish2.toLowerCase().contains(query.toLowerCase())) ||
                                (dish3 != null && dish3.toLowerCase().contains(query.toLowerCase()))) {

                            String truckName = truckDetails.child("name").getValue(String.class);
                            String address = truckDetails.child("address").getValue(String.class);

                            if (truckName != null && address != null) {
                                countReviewsAndAddToList(truckName, address);
                            }
                        }
                    }
                }

                // If no matches are found, hide ProgressBar
                if (foodTruckList.isEmpty()) {
                    Log.d("SearchFoodActivity", "No food trucks matched the search query.");
                }

                searchFoodBar.setVisibility(View.GONE);  // Hide ProgressBar when query completes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("SearchFoodActivity", "Error fetching data: " + error.getMessage());
                searchFoodBar.setVisibility(View.GONE);  // Hide ProgressBar on error
            }
        });
    }

    private void countReviewsAndAddToList(String truckName, String address) {
        Log.d("SearchFoodActivity", "Counting reviews for truck: " + truckName);

        foodTruckReviewsRef.child(truckName).child("Reviews")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int reviewsCount = (int) snapshot.getChildrenCount();

                        Log.d("SearchFoodActivity", "Reviews count for " + truckName + ": " + reviewsCount);

                        // Create food truck model and add it to the list
                        SearchFoodModel foodTruck = new SearchFoodModel(truckName, address, reviewsCount);
                        foodTruckList.add(foodTruck);
                        adapter.notifyDataSetChanged();  // Refresh RecyclerView
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("SearchFoodActivity", "Error fetching reviews: " + error.getMessage());
                    }
                });
    }

}

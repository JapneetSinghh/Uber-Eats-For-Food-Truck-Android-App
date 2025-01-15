package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapters.SearchTruckAdapter;
import com.example.myapplication.fragements.TruckFragment;
import com.example.myapplication.models.SearchTruckModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchTruckActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchTruckAdapter truckAdapter;
    private List<SearchTruckModel> truckList;

    private EditText etSearchTrucks;
    private ImageView clearSearch;
    private ProgressBar searchTruckBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_truck);

        Toolbar toolbar = findViewById(R.id.toolbarTruck);
        setSupportActionBar(toolbar);

        // Enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Show back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);  // Show home icon

        // Handle back icon click
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(SearchTruckActivity.this, MainActivity.class);
            intent.putExtra("show_truck_fragment", true); // Flag to show FoodFragment
            startActivity(intent);
            finish();
        });



        recyclerView = findViewById(R.id.recyclerViewSearchResults);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        truckList = new ArrayList<>();
        truckAdapter = new SearchTruckAdapter(this, truckList, new SearchTruckAdapter.OnTruckClickListener() {
            @Override
            public void onTruckDetailsClick(SearchTruckModel truckModel) {
                // Handle truck details click
            }
        });
        recyclerView.setAdapter(truckAdapter);

        etSearchTrucks = findViewById(R.id.etSearchTrucks);
        clearSearch = findViewById(R.id.clearSearch);

        etSearchTrucks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String query = charSequence.toString();
                if (!query.isEmpty()) {
                    performSearch(query);
                } else {
                    truckList.clear();
                    truckAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        clearSearch.setOnClickListener(v -> etSearchTrucks.setText(""));
    }

    @Override
    public void onBackPressed() {
        // Check if TruckFragment is already in the fragment manager
        Fragment truckFragment = getSupportFragmentManager().findFragmentByTag(TruckFragment.class.getSimpleName());

        if (truckFragment != null) {
            // Replace the current activity's container with the TruckFragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, truckFragment)
                    .addToBackStack(null)  // Optionally add the transaction to back stack
                    .commit();
        } else {
            // If the fragment isn't in the manager, perform default behavior
            super.onBackPressed();
        }
    }


    private void performSearch(String query) {

        Toast.makeText(this, "Searching, please wait...", Toast.LENGTH_SHORT).show();
        // Fetch details from the Users node
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot usersSnapshot) {
                HashMap<String, SearchTruckModel> truckMap = new HashMap<>();

                // Search trucks by name or cuisines from Users node
                for (DataSnapshot userSnapshot : usersSnapshot.getChildren()) {
                    DataSnapshot truckSnapshot = userSnapshot.child("Food Truck Details");
                    if (truckSnapshot.exists()) {
                        String truckName = truckSnapshot.child("name").getValue(String.class);
                        String truckAddress = truckSnapshot.child("address").getValue(String.class);
                        Log.d("SearchTruckActivity", "Fetched address: " + truckAddress);
                        boolean matchesQuery = truckName != null && truckName.toLowerCase().contains(query.toLowerCase());

                        // Check if the truck name matches

                        // Check if any cuisine matches
                        DataSnapshot cuisinesSnapshot = truckSnapshot.child("cuisinesOffered");
                        if (cuisinesSnapshot.exists()) {
                            for (DataSnapshot cuisine : cuisinesSnapshot.getChildren()) {
                                String cuisineValue = cuisine.getValue(String.class);
                                if (cuisineValue != null && cuisineValue.toLowerCase().contains(query.toLowerCase())) {
                                    matchesQuery = true;
                                    break;
                                }
                            }
                        }

                        // If a match is found, add the truck to the map
                        if (matchesQuery) {
                            SearchTruckModel truck = truckSnapshot.getValue(SearchTruckModel.class);
                            if (truck != null) {
                                truckMap.put(truckName, truck);
                            }
                        }
                    }
                }

                // Fetch reviews from the FoodTruckReviews node
                DatabaseReference reviewsRef = FirebaseDatabase.getInstance().getReference("FoodTruckReviews");
                reviewsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot reviewsSnapshot) {
                        truckList.clear();

                        for (String truckName : truckMap.keySet()) {
                            SearchTruckModel truck = truckMap.get(truckName);

                            // Check if reviews exist for the truck
                            DataSnapshot truckReviewsSnapshot = reviewsSnapshot.child(truckName);
                            if (truckReviewsSnapshot.exists()) {
                                double totalRating = 0.0;
                                int reviewCount = 0;

                                for (DataSnapshot reviewSnapshot : truckReviewsSnapshot.child("Reviews").getChildren()) {
                                    Double rating = reviewSnapshot.child("rating").getValue(Double.class);
                                    if (rating != null) {
                                        totalRating += rating;
                                        reviewCount++;
                                    }
                                }

                                // Update truck with average rating and review count
                                if (reviewCount > 0) {
                                    truck.setAverageRating(totalRating / reviewCount);
                                    truck.setReviewCount(reviewCount);
                                }
                            }

                            // Add the truck to the list
                            truckList.add(truck);
                        }

                        // Notify adapter of data changes
                        truckAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle errors
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }


}
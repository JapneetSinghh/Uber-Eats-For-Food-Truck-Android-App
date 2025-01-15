package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.api.SentimentAnalysisAPI;
import com.example.myapplication.api.SentimentRequest;
import com.example.myapplication.api.SentimentResponse;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.fragements.BagFragment;
import com.example.myapplication.fragements.FoodFragment;
import com.example.myapplication.fragements.TruckFragment;
import com.example.myapplication.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.Intent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private ActivityMainBinding binding;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    private SentimentAnalysisAPI sentimentAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);

        if (!sessionManager.isLoggedIn()) {
            redirectToLogin();
            return;
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            redirectToLogin();
            return;
        }

        // Initialize Retrofit for sentiment analysis
        sentimentAPI = RetrofitClient.getClient().create(SentimentAnalysisAPI.class);

        // Initialize views and listeners
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        frameLayout = findViewById(R.id.frame_layout);

        if (getIntent().getBooleanExtra("show_food_fragment", false)) {
            // Show FoodFragment explicitly
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new FoodFragment())
                    .commit();
        } else if (getIntent().getBooleanExtra("show_truck_fragment", false)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new TruckFragment())
                    .commit();
        } else {
            // Default behavior (if no flag is set, show the default fragment or whatever is required)
            replaceFragment(new TruckFragment(), true);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.fast) {
                replaceFragment(new FoodFragment(), false);
                replaceFragment(new com.example.myapplication.fragements.FoodFragment(), false);

            }else if (itemId == R.id.truck) {
                replaceFragment(new com.example.myapplication.fragements.TruckFragment(), false);
            }
//            else (itemId == R.id.bag) {
//                replaceFragment(new BagFragment(), false);
//            }
            return false;
        });

        // Fetch food trucks and analyze their reviews
        fetchAllFoodTrucks();
    }

    private void fetchAllFoodTrucks() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodTruckReviews");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot foodTruckSnapshot : dataSnapshot.getChildren()) {
                        String foodTruckName = foodTruckSnapshot.getKey(); // Food truck name
                        Log.d("FetchFoodTrucks", "Food Truck Name: " + foodTruckName);

                        DataSnapshot reviewsSnapshot = foodTruckSnapshot.child("Reviews");
                        if (reviewsSnapshot.exists()) {
                            for (DataSnapshot reviewSnapshot : reviewsSnapshot.getChildren()) {
                                // Fetch review text, rating, and review ID
                                String reviewId = reviewSnapshot.getKey();
                                String reviewText = reviewSnapshot.child("review").getValue(String.class);
                                Double rating = reviewSnapshot.child("rating").getValue(Double.class);

                                if (reviewText == null || reviewText.trim().isEmpty()) {
                                    Log.w("SentimentAnalysis", "Empty review for food truck: " + foodTruckName);
                                } else {
                                    Log.d("SentimentAnalysis", "Review for " + foodTruckName + ": " + reviewText + ", Rating: " + rating);
                                    // Analyze sentiment and store the result
                                    analyzeSentiment(foodTruckName, reviewText, rating, reviewId);
                                }
                            }
                        } else {
                            Log.w("FetchFoodTrucks", "No reviews found for food truck: " + foodTruckName);
                        }
                    }
                } else {
                    Log.w("FetchFoodTrucks", "No food trucks found in database.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FetchFoodTrucks", "Error fetching data: " + databaseError.getMessage());
            }
        });
    }

    private void analyzeSentiment(String foodTruckName, String reviewText, Double rating, String reviewId) {
        if (reviewText == null || reviewText.isEmpty()) {
            Log.w("SentimentAnalysis", "Empty review for food truck: " + foodTruckName);
            return;
        }

        SentimentRequest request = new SentimentRequest(reviewText);
        sentimentAPI.analyzeSentiment(request).enqueue(new Callback<SentimentResponse>() {
            @Override
            public void onResponse(Call<SentimentResponse> call, Response<SentimentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SentimentResponse sentiment = response.body();
                    String sentimentResult = sentiment.getSentiment();
                    float score = sentiment.getScore();
                    float comparative = sentiment.getComparative();

                    // Create a map to store sentiment details in Firebase
                    Map<String, Object> sentimentData = new HashMap<>();
                    sentimentData.put("sentiment", sentimentResult);
                    sentimentData.put("score", score);
                    sentimentData.put("comparative", comparative);

                    // Store sentiment details under the respective review node in Firebase
                    DatabaseReference reviewRef = FirebaseDatabase.getInstance()
                            .getReference("FoodTruckReviews")
                            .child(foodTruckName)
                            .child("Reviews")
                            .child(reviewId);

                    reviewRef.updateChildren(sentimentData).addOnSuccessListener(aVoid -> {
                        Log.d("SentimentAnalysis", "Sentiment data saved successfully for review: " + reviewId);
                    }).addOnFailureListener(e -> {
                        Log.e("SentimentAnalysis", "Failed to save sentiment data for review: " + reviewId, e);
                    });
                } else {
                    Log.e("SentimentAnalysis", "Error analyzing sentiment: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<SentimentResponse> call, Throwable t) {
                Log.e("SentimentAnalysis", "Request failed: " + t.getMessage());
            }
        });
    }

    private void replaceFragment(Fragment fragment, boolean isAppInitialized) {
        Log.d("MainActivity", "Replacing fragment with: " + fragment.getClass().getSimpleName());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialized) {
            fragmentTransaction.add(R.id.frame_layout, fragment);
        } else {
            fragmentTransaction.replace(R.id.frame_layout, fragment);
        }

        fragmentTransaction.commit();
    }

    private void redirectToLogin() {
        sessionManager.logout();
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public class WorkingHours {
    }
}

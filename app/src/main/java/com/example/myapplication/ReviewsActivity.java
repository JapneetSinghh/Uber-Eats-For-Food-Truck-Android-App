package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseUser;
import java.util.HashMap;
import java.util.UUID;

public class ReviewsActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private TextView rateTxt, foodTruckName, foodTruckAddress;

    private EditText reviewInput;
    private Button submitRanking;

    private DatabaseReference databaseReference, usersReference;

    private String username; // Variable to store the username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        String truckName = getIntent().getStringExtra("truckName");
        String truckAddress = getIntent().getStringExtra("truckAddress");
        foodTruckName = findViewById(R.id.foodTruckName);
        foodTruckAddress = findViewById(R.id.foodTruckLocation);
        foodTruckName.setText(truckName);
        foodTruckAddress.setText(truckAddress);

        ratingBar = findViewById(R.id.ratingBar);
        rateTxt = findViewById(R.id.rateTxt);
        reviewInput = findViewById(R.id.editTextTextMultiLine2);
        submitRanking = findViewById(R.id.submitRanking);

        databaseReference = FirebaseDatabase.getInstance().getReference("FoodTruckReviews");
        usersReference = FirebaseDatabase.getInstance().getReference("Users");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            usersReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        username = snapshot.child("name").getValue(String.class);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e("FirebaseError", "Failed to fetch username: " + error.getMessage());
                }
            });
        }

        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            float roundedRating = Math.round(rating * 2) / 2.0f;
            ratingBar.setRating(roundedRating);
            rateTxt.setText(String.format("%.1f Rating", roundedRating));
        });

        submitRanking.setOnClickListener(v -> saveReview());
    }

    private void saveReview() {
        String truckName = foodTruckName.getText().toString().trim();
        String reviewText = reviewInput.getText().toString().trim();
        float rating = ratingBar.getRating();

        if (truckName.isEmpty()) {
            Toast.makeText(this, "Please enter the food truck name.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (reviewText.isEmpty()) {
            Toast.makeText(this, "Please write a review.", Toast.LENGTH_SHORT).show();
            return;
        }

        String reviewId = UUID.randomUUID().toString();

        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "Unable to fetch username. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        String sentiment = analyzeSentiment(rating, reviewText);

        HashMap<String, Object> reviewData = new HashMap<>();
        reviewData.put("userId", username); // Store username instead of userId
        reviewData.put("rating", rating);
        reviewData.put("review", reviewText);
        reviewData.put("sentiment", sentiment);

        databaseReference.child(truckName).child("Reviews").child(reviewId)
                .setValue(reviewData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Review submitted successfully!", Toast.LENGTH_SHORT).show();
                        reviewInput.setText("");
                        ratingBar.setRating(0);
                    } else {
                        Toast.makeText(this, "Failed to submit review", Toast.LENGTH_SHORT).show();
                        Log.e("FirebaseError", "Error: " + task.getException().getMessage());
                    }
                });
    }

    private String analyzeSentiment(float rating, String reviewText) {
        if (rating >= 4 && reviewText.toLowerCase().contains("good")) {
            return "Very Positive";
        } else if (rating >= 3) {
            return "Positive";
        } else if (rating >= 2) {
            return "Neutral";
        } else {
            return "Negative";
        }
    }
}

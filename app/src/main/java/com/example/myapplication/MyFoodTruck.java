package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;

public class MyFoodTruck extends AppCompatActivity {
    private Button addFoodTruckButton;
    private Button editFoodTruckButton;
    private Button deleteFoodTruckButton;
    private DatabaseReference mDatabase;
    private TextView foodTruckNameTextView;
    private ImageView foodTruckImageView, logoutIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_food_truck);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        addFoodTruckButton = findViewById(R.id.userAddFoodTruck);
        editFoodTruckButton = findViewById(R.id.userEditFoodTruck);
        deleteFoodTruckButton = findViewById(R.id.userDeleteFoodTruck);
        foodTruckNameTextView = findViewById(R.id.userFoodTruckName);
        foodTruckImageView = findViewById(R.id.MyFoodTruckImageView);
        TextView privacyPolicyTextView = findViewById(R.id.privacyPolicyTextView);
        logoutIcon = findViewById(R.id.logoutIcon);

        setupInitialState();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            checkExistingFoodTruck(currentUser.getUid());
        } else {
            Toast.makeText(MyFoodTruck.this, "Please sign in first", Toast.LENGTH_SHORT).show();
            finish();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        privacyPolicyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyFoodTruck.this, Policy.class);
                startActivity(intent);
            }
        });

        logoutIcon.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MyFoodTruck.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setupInitialState() {
        addFoodTruckButton.setVisibility(View.INVISIBLE);
        editFoodTruckButton.setVisibility(View.INVISIBLE);
        deleteFoodTruckButton.setVisibility(View.INVISIBLE);
        foodTruckNameTextView.setVisibility(View.INVISIBLE);
        foodTruckImageView.setVisibility(View.INVISIBLE);
    }

    private void checkExistingFoodTruck(String userId) {
        DatabaseReference userRef = mDatabase.child("Users").child(userId).child("Food Truck Details");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("MyFoodTruck", "Food truck found for user.");
                    setupEditAndDeleteButtons(dataSnapshot, userId);
                } else {
                    Log.d("MyFoodTruck", "No food truck found for user.");
                    setupAddButton();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyFoodTruck.this, "Error checking food truck status: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MyFoodTruck", "Database error: " + databaseError.getMessage(), databaseError.toException());
                setupInitialState();
            }
        });
    }

    private void setupAddButton() {
        addFoodTruckButton.setVisibility(View.VISIBLE);
        editFoodTruckButton.setVisibility(View.INVISIBLE);
        deleteFoodTruckButton.setVisibility(View.INVISIBLE);
        foodTruckNameTextView.setVisibility(View.INVISIBLE);
        foodTruckImageView.setVisibility(View.INVISIBLE);

        addFoodTruckButton.setOnClickListener(v -> {
            Intent intent = new Intent(MyFoodTruck.this, AddNewFoodTruckFormActivity.class);
            startActivity(intent);
        });
    }

    private void setupEditAndDeleteButtons(DataSnapshot dataSnapshot, String userId) {
        addFoodTruckButton.setVisibility(View.INVISIBLE);
        editFoodTruckButton.setVisibility(View.VISIBLE);
        deleteFoodTruckButton.setVisibility(View.VISIBLE);
        foodTruckNameTextView.setVisibility(View.VISIBLE);
        foodTruckImageView.setVisibility(View.VISIBLE);

        String foodTruckName = getValueOrDefault(dataSnapshot, "name");
        foodTruckNameTextView.setText(foodTruckName);

        String base64Image = getFirstImageUrl(dataSnapshot);
        if (base64Image == null || base64Image.isEmpty()) {
            Log.d("MyFoodTruck", "No valid image data found.");
            foodTruckImageView.setImageResource(android.R.color.transparent);
        } else {
            Bitmap bitmap = decodeBase64ToBitmap(base64Image);
            if (bitmap != null) {
                foodTruckImageView.setVisibility(View.VISIBLE);
                foodTruckImageView.setImageBitmap(bitmap);
            } else {
                Log.e("ImageDebug", "Failed to decode Base64 image.");
                foodTruckImageView.setImageResource(android.R.color.transparent);
            }
        }

        editFoodTruckButton.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("FoodTruckData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            Intent intent = new Intent(MyFoodTruck.this, AddNewFoodTruckFormActivity.class);
            intent.putExtra("isEditMode", true);
            intent.putExtra("foodTruckName", getValueOrDefault(dataSnapshot, "name"));
            intent.putExtra("foodTruckEmail", getValueOrDefault(dataSnapshot, "email"));
            intent.putExtra("foodTruckContact", getValueOrDefault(dataSnapshot, "contact"));
            intent.putExtra("foodTruckAddress", getValueOrDefault(dataSnapshot, "address"));
            intent.putExtra("foodTruckAvgPrice", getValueOrDefault(dataSnapshot, "averagePrice"));
            intent.putExtra("foodTruckDescription", getValueOrDefault(dataSnapshot, "description"));
            intent.putExtra("website", getValueOrDefault(dataSnapshot, "websiteURL"));
            intent.putExtra("customOffer1", getValueOrDefault(dataSnapshot, "customOffer1"));
            intent.putExtra("customOffer2", getValueOrDefault(dataSnapshot, "customOffer2"));
            intent.putExtra("customOffer3", getValueOrDefault(dataSnapshot, "customOffer3"));
            intent.putExtra("specialDish1", getValueOrDefault(dataSnapshot, "specialDish1"));
            intent.putExtra("specialDish2", getValueOrDefault(dataSnapshot, "specialDish2"));
            intent.putExtra("specialDish3", getValueOrDefault(dataSnapshot, "specialDish3"));
            intent.putExtra("foodTruckFamousFor", getValueOrDefault(dataSnapshot, "famousFor"));
            intent.putExtra("servingSince", getValueOrDefault(dataSnapshot, "servingSince"));
            intent.putExtra("todaysDiscount", getValueOrDefault(dataSnapshot, "discount"));

            GenericTypeIndicator<ArrayList<String>> typeIndicator = new GenericTypeIndicator<ArrayList<String>>() {};
            ArrayList<String> cuisines = dataSnapshot.child("cuisinesOffered").getValue(typeIndicator);
            editor.putStringSet("cuisines", cuisines != null ? new HashSet<>(cuisines) : new HashSet<>());

            ArrayList<String> imageUrls = dataSnapshot.child("imageUrls").getValue(typeIndicator);
            if (imageUrls != null) {
                editor.putString("imageUrlsJson", new Gson().toJson(imageUrls));
            }

            for (DataSnapshot workingHourSnapshot : dataSnapshot.child("workingHours").getChildren()) {
                String day = workingHourSnapshot.child("day").getValue(String.class);
                String openingTime = workingHourSnapshot.child("openingTime").getValue(String.class);
                String closingTime = workingHourSnapshot.child("closingTime").getValue(String.class);

                if (day != null && openingTime != null && closingTime != null) {
                    intent.putExtra(day.toLowerCase() + "Status", "Open");
                    intent.putExtra(day.toLowerCase() + "OpenTime", openingTime);
                    intent.putExtra(day.toLowerCase() + "CloseTime", closingTime);
                }
            }

            editor.putString("servingSince", getValueOrDefault(dataSnapshot, "servingSince"));
            editor.putString("todaysDiscount", getValueOrDefault(dataSnapshot, "discount"));
            editor.apply();

            intent.putExtra("isEditMode", true);
            startActivity(intent);

        });
        deleteFoodTruckButton.setOnClickListener(v -> showDeleteConfirmationDialog(userId));
    }

    private String getFirstImageUrl(DataSnapshot dataSnapshot) {
        GenericTypeIndicator<ArrayList<String>> typeIndicator = new GenericTypeIndicator<ArrayList<String>>() {};
        ArrayList<String> imageUrls = dataSnapshot.child("imageUrls").getValue(typeIndicator);
        if (imageUrls != null && !imageUrls.isEmpty()) {
            return imageUrls.get(0);
        }
        return "";
    }

    private Bitmap decodeBase64ToBitmap(String base64Image) {
        try {
            byte[] decodedBytes = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (IllegalArgumentException e) {
            Log.e("ImageDebug", "Base64 decoding failed.", e);
            return null;
        }
    }


    private String getValueOrDefault(DataSnapshot snapshot, String key) {
        return snapshot.child(key).exists() ? snapshot.child(key).getValue(String.class) : "";
    }

    private void showDeleteConfirmationDialog(String userId) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Food Truck")
                .setMessage("Are you sure you want to delete your food truck? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> deleteFoodTruck(userId))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteFoodTruck(String userId) {
        DatabaseReference foodTruckRef = mDatabase.child("Users").child(userId).child("Food Truck Details");
        foodTruckRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(MyFoodTruck.this, "Food truck deleted successfully", Toast.LENGTH_SHORT).show();
                    setupAddButton();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MyFoodTruck.this, "Error deleting food truck: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("MyFoodTruck", "Error deleting food truck", e);
                });
    }
}
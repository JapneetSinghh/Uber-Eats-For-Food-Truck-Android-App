package com.example.myapplication.fragements;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ImageUtils;
import com.example.myapplication.MyFoodTruck;
import com.example.myapplication.R;
import com.example.myapplication.SearchTruckActivity;
import com.example.myapplication.adapters.AllTrucksGridAdapter;
import com.example.myapplication.adapters.PopularTruckAdapter;
import com.example.myapplication.adapters.TopRatedTruckAdapter;
import com.example.myapplication.models.AllTrucksGridModel;
import com.example.myapplication.models.PopularTruckModel;
import com.example.myapplication.models.TopRatedTruckModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TruckFragment extends Fragment {

    private RecyclerView topRatedTrucks;
    private RecyclerView popularTruck;
    private RecyclerView allTrucks;
    private ProgressBar topRatedProgressBar;
    private ProgressBar popularTruckProgressBar;
    private ProgressBar allTrucksProgressBar;
    private List<TopRatedTruckModel> topRatedTruckModels;
    private List<PopularTruckModel> popularTruckModels;
    private List<AllTrucksGridModel> allTruckModels;
    private TextView textViewUsername;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_truck, container, false);


        textViewUsername = root.findViewById(R.id.textView4);
        EditText searchTruck = root.findViewById(R.id.search_truck);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        searchTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchTruckActivity.class);
                startActivity(intent);
            }
        });

        // Find the ImageView by its ID
        ImageView profileImageView = root.findViewById(R.id.profileImageView);

        // Set a click listener to open MyFoodTruck activity
        profileImageView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MyFoodTruck.class);
            startActivity(intent);
        });


        // Initialize RecyclerView and ProgressBars
        topRatedTrucks = root.findViewById(R.id.topRatedTruckRecyclerView);
        popularTruck = root.findViewById(R.id.popularTrucksRecyclerView);
        allTrucks = root.findViewById(R.id.allTruckRecyclerView);
        topRatedProgressBar = root.findViewById(R.id.progressBarTopRatedTruck);
        popularTruckProgressBar = root.findViewById(R.id.progressBarPopularTrucks);
        allTrucksProgressBar = root.findViewById(R.id.progressBarAllTrucks);

        // Initialize data lists
        topRatedTruckModels = new ArrayList<>();
        popularTruckModels = new ArrayList<>();
        allTruckModels = new ArrayList<>();


        // Set up RecyclerView adapters and layouts
        setupRecyclerViews();

        // Fetch data from Firebase
        fetchUserName(userId);
        fetchAllTrucksData();
        fetchPopularTrucksData();
        fetchTopRatedTrucksData();

        return root;
    }

    private void fetchUserName(String userId) {
        // Retrieve the user's name from the "Users" node in Firebase
        databaseReference.child("Users").child(userId).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if the name exists
                String username = snapshot.getValue(String.class);
                if (username != null) {
                    // Set the retrieved username to the TextView
                    textViewUsername.setText(username);
                } else {
                    // Set a default value if the username is not found
                    textViewUsername.setText("User");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors if needed
                textViewUsername.setText("Error fetching username");
            }
        });
    }

    private void setupRecyclerViews() {
        // Top Rated Trucks
        TopRatedTruckAdapter topRatedTruckAdapter = new TopRatedTruckAdapter(getContext(), topRatedTruckModels);
        topRatedTrucks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        topRatedTrucks.setAdapter(topRatedTruckAdapter);

        // Popular Trucks
        PopularTruckAdapter popularTruckAdapter = new PopularTruckAdapter(getContext(), popularTruckModels);
        popularTruck.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        popularTruck.setAdapter(popularTruckAdapter);

        // All Trucks
        AllTrucksGridAdapter allTrucksGridAdapter = new AllTrucksGridAdapter(getContext(), allTruckModels);
        allTrucks.setLayoutManager(new GridLayoutManager(getContext(), 2));
        allTrucks.setAdapter(allTrucksGridAdapter);
    }

    private void fetchAllTrucksData() {
        allTrucksProgressBar.setVisibility(View.VISIBLE);

        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allTruckModels.clear();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    DataSnapshot truckSnapshot = userSnapshot.child("Food Truck Details");
                    if (truckSnapshot.exists()) {
                        String title = truckSnapshot.child("name").getValue(String.class);
                        String ratingCount = truckSnapshot.child("ratingCount").getValue(String.class);
                        Double averageRating = truckSnapshot.child("averageRating").getValue(Double.class);
                        String locationAddress = truckSnapshot.child("address").getValue(String.class);

                        GenericTypeIndicator<ArrayList<String>> typeIndicator = new GenericTypeIndicator<ArrayList<String>>() {};
                        ArrayList<String> imageUrls = truckSnapshot.child("imageUrls").getValue(typeIndicator);

                        Bitmap firstImageBitmap = null;
                        if (imageUrls != null && !imageUrls.isEmpty()) {
                            String firstImageBase64 = imageUrls.get(0);
                            firstImageBitmap = ImageUtils.decodeBase64ToBitmap(firstImageBase64);

                            if (firstImageBitmap == null) {
                                Log.e("ImageError", "Failed to decode first image for truck: " + title);
                            } else {
                                Log.d("ImageUtils", "Bitmap decoded for truck: " + title);
                            }
                        }

                        // Fallbacks for missing data
                        if (ratingCount == null || ratingCount.isEmpty()) {
                            ratingCount = "0";
                        }
                        if (averageRating == null) {
                            averageRating = 0.0;
                        }
                        if (locationAddress == null) {
                            locationAddress = "Unknown Location";
                        }

                        allTruckModels.add(new AllTrucksGridModel(
                                firstImageBitmap,
                                title,
                                ratingCount + " Ratings",
                                averageRating.intValue(),
                                R.drawable.star,
                                R.drawable.png_location,
                                locationAddress
                        ));
                    }
                }

                getActivity().runOnUiThread(() -> {
                    allTrucks.getAdapter().notifyDataSetChanged();
                    Log.d("TruckFragment", "Adapter updated with " + allTruckModels.size() + " items.");
                    allTrucksProgressBar.setVisibility(View.GONE);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TruckFragment", "Error fetching data: " + error.getMessage());
                allTrucksProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void fetchPopularTrucksData() {
        popularTruckProgressBar.setVisibility(View.VISIBLE);

        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                popularTruckModels.clear();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    DataSnapshot truckSnapshot = userSnapshot.child("Food Truck Details");
                    if (truckSnapshot.exists()) {
                        String title = truckSnapshot.child("name").getValue(String.class);
                        String ratingCount = truckSnapshot.child("ratingCount").getValue(String.class);
                        Double averageRating = truckSnapshot.child("averageRating").getValue(Double.class);
                        String locationAddress = truckSnapshot.child("address").getValue(String.class);

                        GenericTypeIndicator<ArrayList<String>> typeIndicator = new GenericTypeIndicator<ArrayList<String>>() {};
                        ArrayList<String> imageUrls = truckSnapshot.child("imageUrls").getValue(typeIndicator);

                        Bitmap firstImageBitmap = null;
                        if (imageUrls != null && !imageUrls.isEmpty()) {
                            String firstImageBase64 = imageUrls.get(0);
                            firstImageBitmap = ImageUtils.decodeBase64ToBitmap(firstImageBase64);

                            if (firstImageBitmap == null) {
                                Log.e("ImageError", "Failed to decode first image for truck: " + title);
                            } else {
                                Log.d("ImageUtils", "Bitmap decoded for truck: " + title);
                            }
                        }

                        if (ratingCount == null || ratingCount.isEmpty()) {
                            ratingCount = "0";
                        }
                        if (averageRating == null) {
                            averageRating = 0.0;
                        }

                        popularTruckModels.add(new PopularTruckModel(
                                title,
                                averageRating.intValue(),
                                locationAddress,
                                firstImageBitmap,
                                R.drawable.star,
                                Integer.parseInt(ratingCount)
                        ));
                    }
                }

                popularTruck.getAdapter().notifyDataSetChanged();
                popularTruckProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TruckFragment", "Error fetching popular trucks", error.toException());
                popularTruckProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void fetchTopRatedTrucksData() {
        topRatedProgressBar.setVisibility(View.VISIBLE);

        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                topRatedTruckModels.clear();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    DataSnapshot truckSnapshot = userSnapshot.child("Food Truck Details");
                    if (truckSnapshot.exists()) {
                        String title = truckSnapshot.child("name").getValue(String.class);
                        String ratingCount = truckSnapshot.child("ratingCount").getValue(String.class);
                        Double averageRating = truckSnapshot.child("averageRating").getValue(Double.class);
                        String locationAddress = truckSnapshot.child("address").getValue(String.class);

                        GenericTypeIndicator<ArrayList<String>> typeIndicator = new GenericTypeIndicator<ArrayList<String>>() {};
                        ArrayList<String> imageUrls = truckSnapshot.child("imageUrls").getValue(typeIndicator);

                        Bitmap firstImageBitmap = null;
                        if (imageUrls != null && !imageUrls.isEmpty()) {
                            String firstImageBase64 = imageUrls.get(0);
                            firstImageBitmap = ImageUtils.decodeBase64ToBitmap(firstImageBase64);

                            if (firstImageBitmap == null) {
                                Log.e("ImageError", "Failed to decode first image for truck: " + title);
                            } else {
                                Log.d("ImageUtils", "Bitmap decoded for truck: " + title);
                            }
                        }

                        if (ratingCount == null || ratingCount.isEmpty()) {
                            ratingCount = "0";
                        }
                        if (averageRating == null) {
                            averageRating = 0.0;
                        }

                        topRatedTruckModels.add(new TopRatedTruckModel(
                                title,
                                averageRating.intValue(),
                                locationAddress,
                                firstImageBitmap,
                                R.drawable.star,
                                Integer.parseInt(ratingCount)
                        ));
                    }
                }

                topRatedTrucks.getAdapter().notifyDataSetChanged();
                topRatedProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TruckFragment", "Error fetching top-rated trucks", error.toException());
                topRatedProgressBar.setVisibility(View.GONE);
            }
        });
    }

}

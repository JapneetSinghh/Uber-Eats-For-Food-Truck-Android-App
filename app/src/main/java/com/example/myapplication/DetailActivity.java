package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    private ReviewAdapter reviewAdapter;
    private List<Review> reviewsList;
    private DatabaseReference foodTruckRef;
    private String truckAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        EdgeToEdge.enable(this);
        foodTruckRef = FirebaseDatabase.getInstance().getReference("Users/6tSdXX4hVKcCkrbyNe29QpFPGP33/Food Truck Details");

        fetchDataAndUpdateUI();

        ImageView backArrow = findViewById(R.id.imageView4);
        backArrow.setOnClickListener(v -> {
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<OfferModel> offerList = new ArrayList<>();
        offerList.add(new OfferModel("INSTANT OFFER", "Flat 20% OFF", "Valid all day"));
        offerList.add(new OfferModel("EXCLUSIVE OFFER", "Get 10% OFF", "up to ₹150"));
        offerList.add(new OfferModel("EXCLUSIVE OFFER", "Get 10% OFF", "up to ₹150"));

        OfferAdapter adapter = new OfferAdapter(this, offerList);
        recyclerView.setAdapter(adapter);

    }

    private void fetchDataAndUpdateUI() {
        Intent intent = getIntent();
        String truckName = intent.getStringExtra("truckName");

        if (truckName == null || truckName.isEmpty()) {
            Toast.makeText(this, "Truck name not provided", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean truckFound = false;

                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        DataSnapshot foodTruckSnapshot = userSnapshot.child("Food Truck Details");

                        if (foodTruckSnapshot.exists()) {
                            String name = foodTruckSnapshot.child("name").getValue(String.class);
                            if (name != null && name.equalsIgnoreCase(truckName)) {
                                FoodTruck foodTruck = new FoodTruck();
                                foodTruck.name = name;
                                foodTruck.address = foodTruckSnapshot.child("address").getValue(String.class);
                                foodTruck.description = foodTruckSnapshot.child("description").getValue(String.class);
                                foodTruck.contact = foodTruckSnapshot.child("contact").getValue(String.class);
                                foodTruck.email = foodTruckSnapshot.child("email").getValue(String.class);
                                foodTruck.famousFor = foodTruckSnapshot.child("famousFor").getValue(String.class);
                                foodTruck.averagePrice = foodTruckSnapshot.child("averagePrice").getValue(String.class);
                                foodTruck.customOffer1 = foodTruckSnapshot.child("customOffer1").getValue(String.class);
                                foodTruck.customOffer2 = foodTruckSnapshot.child("customOffer2").getValue(String.class);
                                foodTruck.specialDish1 = foodTruckSnapshot.child("specialDish1").getValue(String.class);
                                foodTruck.specialDish2 = foodTruckSnapshot.child("specialDish2").getValue(String.class);
                                foodTruck.specialDish3 = foodTruckSnapshot.child("specialDish3").getValue(String.class);
                                foodTruck.description = foodTruckSnapshot.child("description").getValue(String.class);
                                foodTruck.discount = foodTruckSnapshot.child("discount").getValue(String.class);
                                foodTruck.servingSince = foodTruckSnapshot.child("servingSince").getValue(String.class);

                                foodTruck.cuisinesOffered = foodTruckSnapshot.child("cuisinesOffered").getValue(new GenericTypeIndicator<List<String>>() {
                                });
                                GenericTypeIndicator<List<Map<String, String>>> typeIndicator = new GenericTypeIndicator<List<Map<String, String>>>() {
                                };
                                List<Map<String, String>> workingHoursList = foodTruckSnapshot.child("workingHours").getValue(typeIndicator);

                                if (workingHoursList != null) {
                                    for (Map<String, String> dayHours : workingHoursList) {
                                        String day = dayHours.get("day");
                                        String openingTime = dayHours.get("openingTime");
                                        String closingTime = dayHours.get("closingTime");

                                        if (day != null && openingTime != null && closingTime != null) {
                                            String hours = openingTime + " - " + closingTime;

                                            Log.d("WorkingHours", "Updating hours for: " + day + " -> " + hours);

                                            switch (day) {
                                                case "Monday":
                                                    ((TextView) findViewById(R.id.monday_hours)).setText(hours);
                                                    break;
                                                case "Tuesday":
                                                    ((TextView) findViewById(R.id.tuesday_hours)).setText(hours);
                                                    break;
                                                case "Wednesday":
                                                    ((TextView) findViewById(R.id.wednesday_hours)).setText(hours);
                                                    break;
                                                case "Thursday":
                                                    ((TextView) findViewById(R.id.thursday_hours)).setText(hours);
                                                    break;
                                                case "Friday":
                                                    ((TextView) findViewById(R.id.friday_hours)).setText(hours);
                                                    break;
                                                case "Saturday":
                                                    ((TextView) findViewById(R.id.saturday_hours)).setText(hours);
                                                    break;
                                                case "Sunday":
                                                    ((TextView) findViewById(R.id.sunday_hours)).setText(hours);
                                                    break;
                                                default:
                                                    Log.w("WorkingHours", "Unknown day: " + day);
                                                    break;
                                            }
                                        } else {
                                            Log.e("WorkingHours", "Invalid or missing data for dayHours: " + dayHours);
                                        }
                                    }
                                } else {
                                    Log.w("WorkingHours", "No working hours found.");
                                }
                                truckAddress = foodTruck.address;

                                setupGallery(foodTruckSnapshot);
                                setupReviews(foodTruck.name);
                                updateUI(foodTruck);
                                truckFound = true;
                                break;
                            }
                        }
                    }

                    if (!truckFound) {
                        Toast.makeText(DetailActivity.this, "No truck found with the given name", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DetailActivity.this, "No data found under Users node.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DetailActivity.this, "Error fetching data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(FoodTruck foodTruck) {

        Button openMapButton = findViewById(R.id.openMapButton);
        TextView foodTruckNameTextView = findViewById(R.id.foodTruckName);
        TextView addressTextView = findViewById(R.id.foodTruckLocation);
        TextView cuisinesOfferedTextView = findViewById(R.id.cuisines_offered_text_view);
        TextView famousForTextView = findViewById(R.id.famous_for_text_view);
        TextView top3DishesTextView = findViewById(R.id.top_3_dishes_text_view);
        TextView priceForTwoTextView = findViewById(R.id.price_for_two_text_view);
        TextView servingSince = findViewById(R.id.serving_since_text_view);
        TextView about_description = findViewById(R.id.about_description);

        TextView timeTxt = findViewById(R.id.timeTxt);

        about_description.setText(foodTruck.description != null ? foodTruck.description : "Not Available");
        foodTruckNameTextView.setText(foodTruck.name != null ? foodTruck.name : "Not Available");
        addressTextView.setText(foodTruck.address != null ? foodTruck.address : "Not Available");
        cuisinesOfferedTextView.setText(foodTruck.cuisinesOffered != null ? TextUtils.join(", ", foodTruck.cuisinesOffered) : "Not Available");
        famousForTextView.setText(foodTruck.famousFor != null ? foodTruck.famousFor : "Not Available");
        top3DishesTextView.setText("1. " + (foodTruck.specialDish1 != null ? foodTruck.specialDish1 : "N/A") +
                "\n2. " + (foodTruck.specialDish2 != null ? foodTruck.specialDish2 : "N/A") +
                "\n3. " + (foodTruck.specialDish3 != null ? foodTruck.specialDish3 : "N/A"));
        priceForTwoTextView.setText(foodTruck.averagePrice != null ? foodTruck.averagePrice : "Not Available");
        timeTxt.setText(foodTruck.averagePrice != null ? foodTruck.averagePrice : "Not Available");
        servingSince.setText(foodTruck.servingSince != null ? foodTruck.servingSince : "Not Available");

        openMapButton.setOnClickListener(v -> {
            if (foodTruck.address != null && !foodTruck.address.isEmpty()) {
                openGoogleMaps(foodTruck.address);
            } else {
                Toast.makeText(this, "Address not available", Toast.LENGTH_SHORT).show();
            }
        });

        List<OfferModel> offerModels = new ArrayList<>();
        if (foodTruck.discount != null && !foodTruck.discount.trim().isEmpty()) {
            offerModels.add(new OfferModel("Discount", foodTruck.discount, "Valid for all orders!"));
        }
        if (foodTruck.customOffer1 != null && !foodTruck.customOffer1.trim().isEmpty()) {
            offerModels.add(new OfferModel("Exclusive Deal!", foodTruck.customOffer1, "Exclusive Deal!"));
        }
        if (foodTruck.customOffer2 != null && !foodTruck.customOffer2.trim().isEmpty()) {
            offerModels.add(new OfferModel("Special Discount!", foodTruck.customOffer2, "Special Discount!"));
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        OfferAdapter adapter = new OfferAdapter(this, offerModels);
        recyclerView.setAdapter(adapter);
    }

    private void setupReviews(String truckName) {
        RecyclerView reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView);
        Button writeReviewButton = findViewById(R.id.writeReviewButton);
        TextView calText = findViewById(R.id.calTxt);
        reviewsList = new ArrayList<>();

        DatabaseReference reviewsRef = FirebaseDatabase.getInstance()
                .getReference("FoodTruckReviews")
                .child(truckName)
                .child("Reviews");

        Log.d("setupReviews", "Fetching reviews from path: " + reviewsRef);

        reviewsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long numOfReviews = dataSnapshot.getChildrenCount();
                    calText.setText(String.format("%d", numOfReviews));

                    float totalRating = 0.0f;
                    for (DataSnapshot reviewSnapshot : dataSnapshot.getChildren()) {
                        String userName = reviewSnapshot.child("userId").getValue(String.class);
                        String reviewText = reviewSnapshot.child("review").getValue(String.class);
                        Float rating = reviewSnapshot.child("rating").getValue(Float.class);
                        String sentiment = reviewSnapshot.child("sentiment").getValue(String.class);

                        userName = userName != null ? userName : "Anonymous";
                        reviewText = reviewText != null ? reviewText : "No review text provided.";
                        rating = rating != null ? rating : 0.0f;
                        sentiment = sentiment != null ? sentiment : "Unknown";

                        reviewsList.add(new Review(userName, reviewText, rating, sentiment));
                        totalRating += rating;
                        Log.d("setupReviews", "Fetched review: " + userName + ", " + reviewText + ", " + rating + ", " + sentiment);
                    }


                    float averageRating = totalRating / numOfReviews;
                    Log.d("setupReviews", "Average Rating: " + averageRating);
                    TextView starTxt = findViewById(R.id.StarTxt);
                    starTxt.setText(String.format("%.1f", averageRating));
                } else {
                    Log.d("setupReviews", "No reviews found for truck: " + truckName);
                    calText.setText("0");
                }

                reviewAdapter = new ReviewAdapter(DetailActivity.this, reviewsList);
                reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(DetailActivity.this));
                reviewsRecyclerView.setAdapter(reviewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("setupReviews", "Database error: " + databaseError.getMessage());
                Toast.makeText(DetailActivity.this, "Failed to fetch reviews: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        writeReviewButton.setOnClickListener(v -> {

            Intent intent = new Intent(DetailActivity.this, ReviewsActivity.class);
            intent.putExtra("truckName", truckName);
            intent.putExtra("truckAddress", truckAddress);
            startActivity(intent);
        });
    }


    private void openGoogleMaps(String destinationAddress) {
        try {
            String encodedAddress = Uri.encode(destinationAddress);

            String uri = "https://www.google.com/maps/dir/?api=1&destination=" + encodedAddress + "&travelmode=driving";

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "Google Maps is not installed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("GoogleMapsError", "Error opening Google Maps: " + e.getMessage());
            Toast.makeText(this, "Failed to open Google Maps", Toast.LENGTH_SHORT).show();
        }
    }


    public static class FoodTruck {
        public String name;
        public String address;
        public String contact;
        public String email;
        public String specialDish1;
        public String specialDish2;
        public String specialDish3;
        public String famousFor;
        public String description;
        public String discount;
        public String websiteURL;
        public String servingSince;
        public String averagePrice;
        public String customOffer1;
        public String customOffer2;
        public String customOffer3;
        public List<String> cuisinesOffered;
        public List<MainActivity.WorkingHours> workingHours;

        public FoodTruck() {
        }

        public FoodTruck(String name, String address, String contact, String email,
                         String specialDish1, String specialDish2, String specialDish3,
                         String famousFor, String description, String discount, String websiteURL,
                         String servingSince, String averagePrice, String customOffer1,
                         String customOffer2, String customOffer3, List<String> cuisinesOffered,
                         List<MainActivity.WorkingHours> workingHours) {
            this.name = name;
            this.address = address;
            this.contact = contact;
            this.email = email;
            this.specialDish1 = specialDish1;
            this.specialDish2 = specialDish2;
            this.specialDish3 = specialDish3;
            this.famousFor = famousFor;
            this.description = description;
            this.discount = discount;
            this.websiteURL = websiteURL;
            this.servingSince = servingSince;
            this.averagePrice = averagePrice;
            this.customOffer1 = customOffer1;
            this.customOffer2 = customOffer2;
            this.customOffer3 = customOffer3;
            this.cuisinesOffered = cuisinesOffered;
            this.workingHours = workingHours;
        }
    }

    public static class WorkingHours {
        public String day;
        public String openingTime;
        public String closingTime;


        public WorkingHours() {
        }

        public WorkingHours(String day, String openingTime, String closingTime) {
            this.day = day;
            this.openingTime = openingTime;
            this.closingTime = closingTime;
        }
    }

    private void setupGallery(DataSnapshot dataSnapshot) {
        RecyclerView galleryRecyclerView = findViewById(R.id.galleryRecyclerView);
        ImageView featuredImageView = findViewById(R.id.imagePreviewActivityDetails);

        GenericTypeIndicator<ArrayList<String>> typeIndicator = new GenericTypeIndicator<ArrayList<String>>() {
        };
        ArrayList<String> imageUrls = dataSnapshot.child("imageUrls").getValue(typeIndicator);

        if (imageUrls != null && !imageUrls.isEmpty()) {
            String firstImageBase64 = imageUrls.get(0);
            Bitmap firstImageBitmap = ImageUtils.decodeBase64ToBitmap(firstImageBase64);
            if (firstImageBitmap != null) {
                featuredImageView.setImageBitmap(firstImageBitmap);
            } else {
                Log.e("ImageError", "Failed to decode the first image.");
            }

            ImageAdapter imageAdapter = new ImageAdapter( imageUrls);
            galleryRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            galleryRecyclerView.setAdapter(imageAdapter);
        } else {
            Log.d("FoodTruckInfo", "No images available for this food truck.");
        }
    }

}
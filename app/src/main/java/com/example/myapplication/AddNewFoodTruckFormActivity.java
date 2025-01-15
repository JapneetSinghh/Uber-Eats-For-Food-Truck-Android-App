package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class AddNewFoodTruckFormActivity extends AppCompatActivity {

    private EditText foodTruckName, foodTruckEmail, foodTruckContact, foodTruckAddress, foodTrucKCuisines, foodTruckSpecial1, foodTruckSpecial2, foodTruckSpecial3,
            foodTruckFamousFor, foodTruckDescription, foodTruckOwnerWebsite, averagePrice, customOffer1, customOffer2, customOffer3;
    private DatabaseReference databaseReference;
    private static final int MAX_IMAGES = 6;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button uploadImageButton;
    private Spinner mondayOpeningTimeSpinner, mondayClosingTimeSpinner;
    private Spinner tuesdayOpeningTimeSpinner, tuesdayClosingTimeSpinner;
    private Spinner wednesdayOpeningTimeSpinner, wednesdayClosingTimeSpinner;
    private Spinner thursdayOpeningTimeSpinner, thursdayClosingTimeSpinner;
    private Spinner fridayOpeningTimeSpinner, fridayClosingTimeSpinner;
    private Spinner saturdayOpeningTimeSpinner, saturdayClosingTimeSpinner;
    private Spinner sundayOpeningTimeSpinner, sundayClosingTimeSpinner;
    private Spinner servingSinceSpinner, todaysDiscountSpinner;
    private ImageAdapter imageAdapter;
    private List<Uri> imageUris;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_food_truck_form);

        requestStoragePermission();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        String currentUserId = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Food Truck Details");

        boolean isEditMode = getIntent().getBooleanExtra("isEditMode", false);

        foodTruckName = findViewById(R.id.foodTruckName);
        foodTruckEmail = findViewById(R.id.foodTruckEmail);
        foodTruckContact = findViewById(R.id.foodTruckContact);
        foodTruckAddress = findViewById(R.id.foodTruckAddress);
        foodTrucKCuisines = findViewById(R.id.foodTrucKCuisines);
        foodTruckSpecial1 = findViewById(R.id.specialDish1);
        foodTruckSpecial2 = findViewById(R.id.specialDish2);
        foodTruckSpecial3 = findViewById(R.id.specialDish3);
        foodTruckFamousFor = findViewById(R.id.foodTruckFamousFor);
        foodTruckDescription = findViewById(R.id.foodTruckDescription);
        foodTruckOwnerWebsite = findViewById(R.id.foodTruckOwnerWebsite);
        averagePrice = findViewById(R.id.foodTruckAvgPrice);
        customOffer1 = findViewById(R.id.CustomOffer1);
        customOffer2 = findViewById(R.id.CustomOffer2);
        customOffer3 = findViewById(R.id.CustomOffer3);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        servingSinceSpinner = findViewById(R.id.servingSinceSpinner);
        todaysDiscountSpinner = findViewById(R.id.todaysDiscountSpinner);
        Button cancelButton = findViewById(R.id.cancelFoodTruckBtn);
        Button submitBtn = findViewById(R.id.addFoodTruckBtn);

        imageUris = new ArrayList<>();
        imageAdapter = new ImageAdapter(new ArrayList<>());
        RecyclerView imagePreviewRecyclerView = findViewById(R.id.imagePreviewRecyclerView);
        imagePreviewRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imagePreviewRecyclerView.setAdapter(imageAdapter);

        uploadImageButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                launchImagePicker();
            } else {
                requestStoragePermission();
            }
        });

        LinearLayout mondayTimePicker = findViewById(R.id.mondayTimePicker);
        RadioGroup mondayRadioGroup = findViewById(R.id.mondayRadioGroup);
        mondayOpeningTimeSpinner = findViewById(R.id.mondayOpeningTimeSpinner);
        mondayClosingTimeSpinner = findViewById(R.id.mondayClosingTimeSpinner);

        LinearLayout tuesdayTimePicker = findViewById(R.id.tuesdayTimePicker);
        RadioGroup tuesdayRadioGroup = findViewById(R.id.tuesdayRadioGroup);
        tuesdayOpeningTimeSpinner = findViewById(R.id.tuesdayOpeningTimeSpinner);
        tuesdayClosingTimeSpinner = findViewById(R.id.tuesdayClosingTimeSpinner);

        LinearLayout wednesdayTimePicker = findViewById(R.id.wednesdayTimePicker);
        RadioGroup wednesdayRadioGroup = findViewById(R.id.wednesdayRadioGroup);
        wednesdayOpeningTimeSpinner = findViewById(R.id.wednesdayOpeningTimeSpinner);
        wednesdayClosingTimeSpinner = findViewById(R.id.wednesdayClosingTimeSpinner);

        LinearLayout thursdayTimePicker = findViewById(R.id.thursdayTimePicker);
        RadioGroup thursdayRadioGroup = findViewById(R.id.thursdayRadioGroup);
        thursdayOpeningTimeSpinner = findViewById(R.id.thursdayOpeningTimeSpinner);
        thursdayClosingTimeSpinner = findViewById(R.id.thursdayClosingTimeSpinner);

        LinearLayout fridayTimePicker = findViewById(R.id.fridayTimePicker);
        RadioGroup fridayRadioGroup = findViewById(R.id.fridayRadioGroup);
        fridayOpeningTimeSpinner = findViewById(R.id.fridayOpeningTimeSpinner);
        fridayClosingTimeSpinner = findViewById(R.id.fridayClosingTimeSpinner);

        LinearLayout saturdayTimePicker = findViewById(R.id.saturdayTimePicker);
        RadioGroup saturdayRadioGroup = findViewById(R.id.saturdayRadioGroup);
        saturdayOpeningTimeSpinner = findViewById(R.id.saturdayOpeningTimeSpinner);
        saturdayClosingTimeSpinner = findViewById(R.id.saturdayClosingTimeSpinner);

        LinearLayout sundayTimePicker = findViewById(R.id.sundayTimePicker);
        RadioGroup sundayRadioGroup = findViewById(R.id.sundayRadioGroup);
        sundayOpeningTimeSpinner = findViewById(R.id.sundayOpeningTimeSpinner);
        sundayClosingTimeSpinner = findViewById(R.id.sundayClosingTimeSpinner);

        ((RadioGroup) findViewById(R.id.mondayRadioGroup)).check(R.id.mondayClosed);
        ((RadioGroup) findViewById(R.id.tuesdayRadioGroup)).check(R.id.tuesdayClosed);
        ((RadioGroup) findViewById(R.id.wednesdayRadioGroup)).check(R.id.wednesdayClosed);
        ((RadioGroup) findViewById(R.id.thursdayRadioGroup)).check(R.id.thursdayClosed);
        ((RadioGroup) findViewById(R.id.fridayRadioGroup)).check(R.id.fridayClosed);
        ((RadioGroup) findViewById(R.id.saturdayRadioGroup)).check(R.id.saturdayClosed);
        ((RadioGroup) findViewById(R.id.sundayRadioGroup)).check(R.id.sundayClosed);


        ArrayAdapter<String> timeAdapter = getTimeAdapter();

        mondayOpeningTimeSpinner.setAdapter(timeAdapter);
        mondayClosingTimeSpinner.setAdapter(timeAdapter);

        tuesdayOpeningTimeSpinner.setAdapter(timeAdapter);
        tuesdayClosingTimeSpinner.setAdapter(timeAdapter);

        wednesdayOpeningTimeSpinner.setAdapter(timeAdapter);
        wednesdayClosingTimeSpinner.setAdapter(timeAdapter);

        thursdayOpeningTimeSpinner.setAdapter(timeAdapter);
        thursdayClosingTimeSpinner.setAdapter(timeAdapter);

        fridayOpeningTimeSpinner.setAdapter(timeAdapter);
        fridayClosingTimeSpinner.setAdapter(timeAdapter);

        saturdayOpeningTimeSpinner.setAdapter(timeAdapter);
        saturdayClosingTimeSpinner.setAdapter(timeAdapter);

        sundayOpeningTimeSpinner.setAdapter(timeAdapter);
        sundayClosingTimeSpinner.setAdapter(timeAdapter);

        setRadioGroupListener(mondayRadioGroup, mondayTimePicker, R.id.mondayOpen, R.id.mondayClosed);
        setRadioGroupListener(tuesdayRadioGroup, tuesdayTimePicker, R.id.tuesdayOpen, R.id.tuesdayClosed);
        setRadioGroupListener(wednesdayRadioGroup, wednesdayTimePicker, R.id.wednesdayOpen, R.id.wednesdayClosed);
        setRadioGroupListener(thursdayRadioGroup, thursdayTimePicker, R.id.thursdayOpen, R.id.thursdayClosed);
        setRadioGroupListener(fridayRadioGroup, fridayTimePicker, R.id.fridayOpen, R.id.fridayClosed);
        setRadioGroupListener(saturdayRadioGroup, saturdayTimePicker, R.id.saturdayOpen, R.id.saturdayClosed);
        setRadioGroupListener(sundayRadioGroup, sundayTimePicker, R.id.sundayOpen, R.id.sundayClosed);

        ArrayAdapter<String> discountAdapter = getDiscountAdapter();
        todaysDiscountSpinner.setAdapter(discountAdapter);

        String[] years = {"2024","2023","2022","2021","2020","2019","2018","2017","2016","2015","2014","2013","2012","2011","2010"};

        ArrayAdapter<String> servingSinceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        servingSinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servingSinceSpinner.setAdapter(servingSinceAdapter);

        if (isEditMode) {

            submitBtn.setText("Update");
            SharedPreferences sharedPreferences = getSharedPreferences("FoodTruckData", MODE_PRIVATE);
            String imageUrlsJson = sharedPreferences.getString("imageUrlsJson", null);


            if (imageUrlsJson != null) {
                List<String> imageUrlsList = new Gson().fromJson(imageUrlsJson, new TypeToken<List<String>>() {
                }.getType());
                imageAdapter = new ImageAdapter(imageUrlsList);
                imagePreviewRecyclerView.setAdapter(imageAdapter);
            }

            imageAdapter.notifyDataSetChanged();

            populateWorkingHours("monday", getIntent().getStringExtra("mondayStatus"),
                    getIntent().getStringExtra("mondayOpenTime"), getIntent().getStringExtra("mondayCloseTime"));
            populateWorkingHours("tuesday", getIntent().getStringExtra("tuesdayStatus"),
                    getIntent().getStringExtra("tuesdayOpenTime"), getIntent().getStringExtra("tuesdayCloseTime"));
            populateWorkingHours("wednesday", getIntent().getStringExtra("wednesdayStatus"),
                    getIntent().getStringExtra("wednesdayOpenTime"), getIntent().getStringExtra("wednesdayCloseTime"));
            populateWorkingHours("thursday", getIntent().getStringExtra("thursdayStatus"),
                    getIntent().getStringExtra("thursdayOpenTime"), getIntent().getStringExtra("thursdayCloseTime"));
            populateWorkingHours("friday", getIntent().getStringExtra("fridayStatus"),
                    getIntent().getStringExtra("fridayOpenTime"), getIntent().getStringExtra("fridayCloseTime"));
            populateWorkingHours("saturday", getIntent().getStringExtra("saturdayStatus"),
                    getIntent().getStringExtra("saturdayOpenTime"), getIntent().getStringExtra("saturdayCloseTime"));
            populateWorkingHours("sunday", getIntent().getStringExtra("sundayStatus"),
                    getIntent().getStringExtra("sundayOpenTime"), getIntent().getStringExtra("sundayCloseTime"));

            foodTruckName.setText(getIntent().getStringExtra("foodTruckName"));
            foodTruckEmail.setText(getIntent().getStringExtra("foodTruckEmail"));
            foodTruckContact.setText(getIntent().getStringExtra("foodTruckContact"));
            foodTruckAddress.setText(getIntent().getStringExtra("foodTruckAddress"));
            averagePrice.setText(getIntent().getStringExtra("foodTruckAvgPrice"));
            foodTruckDescription.setText(getIntent().getStringExtra("foodTruckDescription"));
            foodTruckOwnerWebsite.setText(getIntent().getStringExtra("website"));
            customOffer1.setText(getIntent().getStringExtra("customOffer1"));
            customOffer2.setText(getIntent().getStringExtra("customOffer2"));
            customOffer3.setText(getIntent().getStringExtra("customOffer3"));
            foodTruckFamousFor.setText(getIntent().getStringExtra("foodTruckFamousFor"));
            foodTruckSpecial1.setText(getIntent().getStringExtra("specialDish1"));
            foodTruckSpecial2.setText(getIntent().getStringExtra("specialDish2"));
            foodTruckSpecial3.setText(getIntent().getStringExtra("specialDish3"));

            Set<String> cuisinesSet = sharedPreferences.getStringSet("cuisines", new HashSet<>());
            foodTrucKCuisines.setText(TextUtils.join(", ", cuisinesSet));

            String servingSince = sharedPreferences.getString("servingSince", "");
            if (!TextUtils.isEmpty(servingSince)) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) servingSinceSpinner.getAdapter();
                int position = adapter.getPosition(servingSince);
                servingSinceSpinner.setSelection(position);
            }

            String todaysDiscount = sharedPreferences.getString("todaysDiscount", "");
            if (!TextUtils.isEmpty(todaysDiscount)) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) todaysDiscountSpinner.getAdapter();
                int position = adapter.getPosition(todaysDiscount);
                todaysDiscountSpinner.setSelection(position);
            }
        }

        cancelButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddNewFoodTruckFormActivity.this, MyFoodTruck.class);
            startActivity(intent);
            finish();
        });

        submitBtn.setOnClickListener(v -> addFoodTruck());
    }

    private static final Map<String, Integer> dayRadioGroups = new HashMap<>();
    private static final Map<String, Integer> dayOpenSpinners = new HashMap<>();
    private static final Map<String, Integer> dayCloseSpinners = new HashMap<>();
    private static final Map<String, Integer> dayOpenButtons = new HashMap<>();
    private static final Map<String, Integer> dayClosedButtons = new HashMap<>();

    static {
        dayRadioGroups.put("monday", R.id.mondayRadioGroup);
        dayRadioGroups.put("tuesday", R.id.tuesdayRadioGroup);
        dayRadioGroups.put("wednesday", R.id.wednesdayRadioGroup);
        dayRadioGroups.put("thursday", R.id.thursdayRadioGroup);
        dayRadioGroups.put("friday", R.id.fridayRadioGroup);
        dayRadioGroups.put("saturday", R.id.saturdayRadioGroup);
        dayRadioGroups.put("sunday", R.id.sundayRadioGroup);

        dayOpenSpinners.put("monday", R.id.mondayOpeningTimeSpinner);
        dayOpenSpinners.put("tuesday", R.id.tuesdayOpeningTimeSpinner);
        dayOpenSpinners.put("wednesday", R.id.wednesdayOpeningTimeSpinner);
        dayOpenSpinners.put("thursday", R.id.thursdayOpeningTimeSpinner);
        dayOpenSpinners.put("friday", R.id.fridayOpeningTimeSpinner);
        dayOpenSpinners.put("saturday", R.id.saturdayOpeningTimeSpinner);
        dayOpenSpinners.put("sunday", R.id.sundayOpeningTimeSpinner);

        dayCloseSpinners.put("monday", R.id.mondayClosingTimeSpinner);
        dayCloseSpinners.put("tuesday", R.id.tuesdayClosingTimeSpinner);
        dayCloseSpinners.put("wednesday", R.id.wednesdayClosingTimeSpinner);
        dayCloseSpinners.put("thursday", R.id.thursdayClosingTimeSpinner);
        dayCloseSpinners.put("friday", R.id.fridayClosingTimeSpinner);
        dayCloseSpinners.put("saturday", R.id.saturdayClosingTimeSpinner);
        dayCloseSpinners.put("sunday", R.id.sundayClosingTimeSpinner);

        dayOpenButtons.put("monday", R.id.mondayOpen);
        dayOpenButtons.put("tuesday", R.id.tuesdayOpen);
        dayOpenButtons.put("wednesday", R.id.wednesdayOpen);
        dayOpenButtons.put("thursday", R.id.thursdayOpen);
        dayOpenButtons.put("friday", R.id.fridayOpen);
        dayOpenButtons.put("saturday", R.id.saturdayOpen);
        dayOpenButtons.put("sunday", R.id.sundayOpen);

        dayClosedButtons.put("monday", R.id.mondayClosed);
        dayClosedButtons.put("tuesday", R.id.tuesdayClosed);
        dayClosedButtons.put("wednesday", R.id.wednesdayClosed);
        dayClosedButtons.put("thursday", R.id.thursdayClosed);
        dayClosedButtons.put("friday", R.id.fridayClosed);
        dayClosedButtons.put("saturday", R.id.saturdayClosed);
        dayClosedButtons.put("sunday", R.id.sundayClosed);
    }

    private void populateWorkingHours(String day, String status, String openTime, String closeTime) {
        int openButtonId = dayOpenButtons.get(day);
        int closedButtonId = dayClosedButtons.get(day);
        int openingTimeSpinnerId = dayOpenSpinners.get(day);
        int closingTimeSpinnerId = dayCloseSpinners.get(day);

        RadioGroup dayRadioGroup = findViewById(dayRadioGroups.get(day));
        Spinner openingTimeSpinner = findViewById(openingTimeSpinnerId);
        Spinner closingTimeSpinner = findViewById(closingTimeSpinnerId);

        if ("Open".equalsIgnoreCase(status)) {
            dayRadioGroup.check(openButtonId);
            setSpinnerSelection(openingTimeSpinner, openTime);
            setSpinnerSelection(closingTimeSpinner, closeTime);
        } else {
            dayRadioGroup.check(closedButtonId);
        }
    }


    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        if (adapter != null) {
            int position = adapter.getPosition(value);
            spinner.setSelection(position);
        }
    }

    private @NonNull ArrayAdapter<String> getDiscountAdapter() {
        String[] discounts = {
                "No discount", "Flat 5% OFF", "Flat 10% OFF", "Flat 15% OFF", "Flat 20% OFF", "Flat 25% OFF", "Flat 30% OFF",
                "Flat 35% OFF", "Flat 40% OFF", "Flat 45% OFF", "Flat 50% OFF", "Flat 55% OFF", "Flat 60% OFF", "Flat 65% OFF",
                "Flat 70% OFF", "Flat 75% OFF"
        };

        ArrayAdapter<String> discountAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, discounts);
        discountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return discountAdapter;
    }

    private @NonNull ArrayAdapter<String> getTimeAdapter() {
        String[] timeSlots = {
                "5:00 AM","6:00 AM", "7:00 AM", "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
                "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM", "6:00 PM", "7:00 PM", "8:00 PM", "9:00 PM", "10:00 PM", "11:00 AM", "12:00 PM"
        };
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeSlots);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return timeAdapter;
    }

    private void setRadioGroupListener(RadioGroup radioGroup, LinearLayout timePickerLayout, int openId, int closedId) {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == openId) {
                timePickerLayout.setVisibility(View.VISIBLE);
            } else if (checkedId == closedId) {
                timePickerLayout.setVisibility(View.GONE);
            }
        });
    }

    private boolean areFieldsValid() {
        if (TextUtils.isEmpty(foodTruckName.getText().toString())) {
            foodTruckName.setError("Please enter food truck name");
            return false;
        }
        if (TextUtils.isEmpty(foodTruckEmail.getText().toString()) || !Patterns.EMAIL_ADDRESS.matcher(foodTruckEmail.getText().toString()).matches()) {
            foodTruckEmail.setError("Please enter a valid email address");
            return false;
        }
        if (TextUtils.isEmpty(foodTruckContact.getText().toString())) {
            foodTruckContact.setError("Please enter contact number");
            return false;
        }
        try {
            Integer.parseInt(foodTruckContact.getText().toString());
        } catch (NumberFormatException e) {
            foodTruckContact.setError("Please enter a valid number (digits only)");
            return false;
        }
        if (TextUtils.isEmpty(foodTruckAddress.getText().toString())) {
            foodTruckAddress.setError("Please enter address");
            return false;
        }
        int totalImageCount = imageAdapter.getItemCount();
        if (totalImageCount < 2) {
            uploadImageButton.setError("Please upload at least two images");
            return false;
        }
        if (!TextUtils.isDigitsOnly(averagePrice.getText().toString())) {
            averagePrice.setError("Please enter a valid numeric price");
            return false;
        }
        try {
            Integer.parseInt(averagePrice.getText().toString());
        } catch (NumberFormatException e) {
            averagePrice.setError("Please enter a valid number");
            return false;
        }
        if (TextUtils.isEmpty(foodTrucKCuisines.getText().toString())) {
            foodTrucKCuisines.setError("Please enter food truck cuisines");
            return false;
        }
        if (TextUtils.isEmpty(foodTruckSpecial1.getText().toString())) {
            foodTruckSpecial1.setError("Please enter a special dish");
            return false;
        }
        if (TextUtils.isEmpty(foodTruckSpecial2.getText().toString())) {
            foodTruckSpecial2.setError("Please enter a special dish");
            return false;
        }
        if (TextUtils.isEmpty(foodTruckSpecial3.getText().toString())) {
            foodTruckSpecial3.setError("Please enter a special dish");
            return false;
        }
        if (TextUtils.isEmpty(foodTruckFamousFor.getText().toString())) {
            foodTruckFamousFor.setError("Please enter what the truck is famous for");
            return false;
        }
        if (TextUtils.isEmpty(foodTruckDescription.getText().toString())) {
            foodTruckDescription.setError("Please enter a description");
            return false;
        }
        if (TextUtils.isEmpty(foodTruckOwnerWebsite.getText().toString())) {
            foodTruckOwnerWebsite.setError("Please enter a website link");
            return false;
        }
        if (!validateTimeForOpenDays()) {
            return false;
        }
        if (!isAtLeastOneOpenSelected()) {
            Toast.makeText(this, "At least one day should be Open.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean validateTimeForOpenDays() {
        if (isDayOpen(R.id.mondayOpen) && !isTimeValid(mondayOpeningTimeSpinner, mondayClosingTimeSpinner)) {
            return false;
        }
        if (isDayOpen(R.id.tuesdayOpen) && !isTimeValid(tuesdayOpeningTimeSpinner, tuesdayClosingTimeSpinner)) {
            return false;
        }
        if (isDayOpen(R.id.wednesdayOpen) && !isTimeValid(wednesdayOpeningTimeSpinner, wednesdayClosingTimeSpinner)) {
            return false;
        }
        if (isDayOpen(R.id.thursdayOpen) && !isTimeValid(thursdayOpeningTimeSpinner, thursdayClosingTimeSpinner)) {
            return false;
        }
        if (isDayOpen(R.id.fridayOpen) && !isTimeValid(fridayOpeningTimeSpinner, fridayClosingTimeSpinner)) {
            return false;
        }
        if (isDayOpen(R.id.saturdayOpen) && !isTimeValid(saturdayOpeningTimeSpinner, saturdayClosingTimeSpinner)) {
            return false;
        }
        return !isDayOpen(R.id.sundayOpen) || isTimeValid(sundayOpeningTimeSpinner, sundayClosingTimeSpinner);
    }

    private boolean isDayOpen(int radioButtonId) {
        RadioButton openRadioButton = findViewById(radioButtonId);
        return openRadioButton.isChecked();
    }

    private boolean isTimeValid(Spinner openingSpinner, Spinner closingSpinner) {
        String openingTime = openingSpinner.getSelectedItem().toString();
        String closingTime = closingSpinner.getSelectedItem().toString();

        try {
            int openingMinutes = convertToMinutes(openingTime);
            int closingMinutes = convertToMinutes(closingTime);

            if (openingMinutes == closingMinutes) {
                Toast.makeText(this, "Opening time and closing time cannot be the same.", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (closingMinutes < openingMinutes) {
                Toast.makeText(this, "Closing time must be after opening time.", Toast.LENGTH_SHORT).show();
                return false;
            }

            return true;
        } catch (Exception e) {
            Toast.makeText(this, "Invalid time format. Please select valid times.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private int convertToMinutes(String time) {
        try {
            String[] parts = time.split(" ");
            String[] hourMinute = parts[0].split(":");

            int hour = Integer.parseInt(hourMinute[0]);
            int minute = Integer.parseInt(hourMinute[1]);
            boolean isPM = parts[1].equalsIgnoreCase("PM");

            if (isPM && hour != 12) {
                hour += 12;
            } else if (!isPM && hour == 12) {
                hour = 0;
            }

            return (hour * 60) + minute;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    private boolean isAtLeastOneOpenSelected() {
        boolean mondayOpen = ((RadioButton) findViewById(R.id.mondayOpen)).isChecked();
        boolean tuesdayOpen = ((RadioButton) findViewById(R.id.tuesdayOpen)).isChecked();
        boolean wednesdayOpen = ((RadioButton) findViewById(R.id.wednesdayOpen)).isChecked();
        boolean thursdayOpen = ((RadioButton) findViewById(R.id.thursdayOpen)).isChecked();
        boolean fridayOpen = ((RadioButton) findViewById(R.id.fridayOpen)).isChecked();
        boolean saturdayOpen = ((RadioButton) findViewById(R.id.saturdayOpen)).isChecked();
        boolean sundayOpen = ((RadioButton) findViewById(R.id.sundayOpen)).isChecked();

        return mondayOpen || tuesdayOpen || wednesdayOpen || thursdayOpen || fridayOpen || saturdayOpen || sundayOpen;
    }


    private void addWorkingHours(List<WorkingHours> workingHoursList, String day, Spinner openingSpinner, Spinner closingSpinner, int openRadioId) {
        if (((RadioButton) findViewById(openRadioId)).isChecked()) {
            if (!isTimeValid(openingSpinner, closingSpinner)) {
                return;
            }

            String openingTime = openingSpinner.getSelectedItem().toString();
            String closingTime = closingSpinner.getSelectedItem().toString();

            workingHoursList.add(new WorkingHours(day, openingTime, closingTime));
        }
    }

    private void launchImagePicker() {
        Log.d("ImagePicker", "Launching Image Picker");
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Images"), PICK_IMAGE_REQUEST);
    }

    private String convertImageToBase64(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] bytes = new byte[Objects.requireNonNull(inputStream).available()];
            inputStream.read(bytes);
            inputStream.close();
            String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

            return base64Image;
        } catch (IOException e) {
            Log.e("Base64Debug", "Error converting image to Base64", e);
            return null;
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    if (imageUris.size() + count > MAX_IMAGES) {
                        Toast.makeText(this, "You cannot upload more than 6 images.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imageUris.add(imageUri);
                        Bitmap bitmap = getBitmapFromUri(imageUri);
                        if (bitmap != null) {
                            String base64Image = convertImageToBase64(imageUri);
                            if (base64Image != null) {
                                imageAdapter.addBase64Image(base64Image);
                            }
                        }
                    }
                } else if (data.getData() != null) {
                    if (imageUris.size() >= MAX_IMAGES) {
                        Toast.makeText(this, "You cannot upload more than 6 images.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Uri imageUri = data.getData();
                    imageUris.add(imageUri);
                    Bitmap bitmap = getBitmapFromUri(imageUri);
                    if (bitmap != null) {
                        String base64Image = convertImageToBase64(imageUri);
                        if (base64Image != null) {
                            imageAdapter.addBase64Image(base64Image);
                        }
                    }
                }
            }
        }
    }

    private void uploadImages(FoodTruck foodTruck) {
        List<String> newBase64Images = new ArrayList<>();
        for (Uri uri : imageUris) {
            String base64Image = convertImageToBase64(uri);
            if (base64Image != null) {
                newBase64Images.add(base64Image);
            }
        }

        databaseReference.child("imageUrls").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> combinedImages = new ArrayList<>();

                if (snapshot.exists()) {
                    for (DataSnapshot imageSnapshot : snapshot.getChildren()) {
                        String existingImage = imageSnapshot.getValue(String.class);
                        if (existingImage != null) {
                            combinedImages.add(existingImage);
                        }
                    }
                }

                combinedImages.addAll(newBase64Images);

                if (combinedImages.isEmpty()) {
                    Toast.makeText(AddNewFoodTruckFormActivity.this, "No images available to upload.", Toast.LENGTH_SHORT).show();
                    return;
                }

                foodTruck.setImageUrls(combinedImages);

                saveFoodTruckToDatabase(foodTruck);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddNewFoodTruckFormActivity.this, "Failed to fetch existing images: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ImageUploadDebug", "Error fetching existing images", error.toException());
            }
        });
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchImagePicker();
            } else {
                Toast.makeText(this, "Storage permission is required to select images", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveFoodTruckToDatabase(FoodTruck foodTruck) {
        Log.d("FoodTruckDebug", "Saving Food Truck: " + new Gson().toJson(foodTruck));
        databaseReference.setValue(foodTruck)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Food Truck updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddNewFoodTruckFormActivity.this, MyFoodTruck.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update Food Truck: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("FoodTruckError", "Error saving food truck", e);
                });
    }


    public void addFoodTruck() {
        if (areFieldsValid()) {

            List<WorkingHours> workingHoursList = new ArrayList<>();
            addWorkingHours(workingHoursList, "Monday", mondayOpeningTimeSpinner, mondayClosingTimeSpinner, R.id.mondayOpen);
            addWorkingHours(workingHoursList, "Tuesday", tuesdayOpeningTimeSpinner, tuesdayClosingTimeSpinner, R.id.tuesdayOpen);
            addWorkingHours(workingHoursList, "Wednesday", wednesdayOpeningTimeSpinner, wednesdayClosingTimeSpinner, R.id.wednesdayOpen);
            addWorkingHours(workingHoursList, "Thursday", thursdayOpeningTimeSpinner, thursdayClosingTimeSpinner, R.id.thursdayOpen);
            addWorkingHours(workingHoursList, "Friday", fridayOpeningTimeSpinner, fridayClosingTimeSpinner, R.id.fridayOpen);
            addWorkingHours(workingHoursList, "Saturday", saturdayOpeningTimeSpinner, saturdayClosingTimeSpinner, R.id.saturdayOpen);
            addWorkingHours(workingHoursList, "Sunday", sundayOpeningTimeSpinner, sundayClosingTimeSpinner, R.id.sundayOpen);

            SharedPreferences sharedPreferences = getSharedPreferences("FoodTruckData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("imageUrls");
            editor.apply();

            for (WorkingHours hours : workingHoursList) {
                editor.putString(hours.day + "Status", "Open");
                editor.putString(hours.day + "OpenTime", hours.openingTime);
                editor.putString(hours.day + "CloseTime", hours.closingTime);
            }
            editor.apply();

            List<String> cuisines = new ArrayList<>();
            String cuisinesInput = foodTrucKCuisines.getText().toString();
            if (!TextUtils.isEmpty(cuisinesInput)) {
                String[] cuisinesArray = cuisinesInput.split(",");
                for (String cuisine : cuisinesArray) {
                    cuisines.add(cuisine.trim());
                }
            }

            String selectedDiscount = todaysDiscountSpinner.getSelectedItem().toString();
            String customOffer1Value = customOffer1.getText().toString().trim();
            String customOffer2Value = customOffer2.getText().toString().trim();
            String customOffer3Value = customOffer3.getText().toString().trim();
            String descriptionInput = foodTruckDescription.getText().toString().trim();

            if (descriptionInput.length() < 30) {
                foodTruckDescription.setError("Description must be at least 30 characters long");
                return;
            }

            FoodTruck foodTruck = new FoodTruck(
                    foodTruckName.getText().toString(),
                    foodTruckEmail.getText().toString(),
                    foodTruckContact.getText().toString(),
                    foodTruckAddress.getText().toString(),
                    cuisines,
                    foodTruckSpecial1.getText().toString(),
                    foodTruckSpecial2.getText().toString(),
                    foodTruckSpecial3.getText().toString(),
                    foodTruckFamousFor.getText().toString(),
                    descriptionInput,
                    foodTruckOwnerWebsite.getText().toString(),
                    servingSinceSpinner.getSelectedItem().toString(),
                    selectedDiscount,
                    averagePrice.getText().toString(),
                    workingHoursList,
                    customOffer1Value,
                    customOffer2Value,
                    customOffer3Value
            );

            uploadImages(foodTruck);
            saveFoodTruckToDatabase(foodTruck);
        }
    }

    static class WorkingHours {
        public String day;
        public String openingTime;
        public String closingTime;

        public WorkingHours() {}

        public WorkingHours(String day, String openingTime, String closingTime) {
            this.day = day;
            this.openingTime = openingTime;
            this.closingTime = closingTime;
        }
    }

    static class FoodTruck {
        public String name, email, address, specialDish1, specialDish2, specialDish3, famousFor, customOffer1, customOffer2, customOffer3, discount, websiteURL, description, contact, averagePrice, servingSince;
        public List<WorkingHours> workingHours;
        public List<String> cuisinesOffered;
        public List<String> imageUrls;

        public FoodTruck() {
        }

        public FoodTruck(String name, String email, String contact, String address, List<String> cuisinesOffered,
                         String specialDish1, String specialDish2, String specialDish3, String famousFor,
                         String description, String websiteURL, String servingSince, String discount, String averagePrice, List<WorkingHours> workingHours, String customOffer1, String customOffer2, String customOffer3) {
            this.name = name;
            this.email = email;
            this.contact = contact;
            this.address = address;
            this.cuisinesOffered = cuisinesOffered;
            this.specialDish1 = specialDish1;
            this.specialDish2 = specialDish2;
            this.specialDish3 = specialDish3;
            this.famousFor = famousFor;
            this.description = description;
            this.websiteURL = websiteURL;
            this.servingSince = (servingSince);
            this.discount = discount;
            this.averagePrice = averagePrice;
            this.workingHours = workingHours;
            this.customOffer1 = customOffer1;
            this.customOffer2 = customOffer2;
            this.customOffer3 = customOffer3;
        }

        public void setImageUrls(List<String> imageUrls) {
            this.imageUrls = imageUrls;
        }
    }
}
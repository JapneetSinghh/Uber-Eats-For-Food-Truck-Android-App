package com.example.myapplication.models;

import android.graphics.Bitmap;

public class AllTrucksGridModel {
    int allTrucksImage;
    public String allTruckName;
    public String allTruckRatings;
    public int allTruckNumRating;
    int allTruckImageStar;

    // New fields for location
    int allTruckLocationIcon;
    String allTruckLocationAddress;

    // New field for Bitmap image
    private Bitmap truckImageBitmap;


    public AllTrucksGridModel(Bitmap truckImageBitmap, String allTruckName, String allTruckRatings, int truckNumRating,
                              int allTruckImageStar, int allTruckLocationIcon, String allTruckLocationAddress) {
        this.truckImageBitmap = truckImageBitmap;
        this.allTruckName = allTruckName;
        this.allTruckRatings = allTruckRatings;
        this.allTruckNumRating = truckNumRating;
        this.allTruckImageStar = allTruckImageStar;
        this.allTruckLocationIcon = allTruckLocationIcon;
        this.allTruckLocationAddress = allTruckLocationAddress;
    }

    // Getters and setters for the location fields
    public int getAllTruckLocationIcon() {
        return allTruckLocationIcon;
    }

    public void setAllTruckLocationIcon(int allTruckLocationIcon) {
        this.allTruckLocationIcon = allTruckLocationIcon;
    }

    public String getAllTruckLocationAddress() {
        return allTruckLocationAddress;
    }

    public void setAllTruckLocationAddress(String allTruckLocationAddress) {
        this.allTruckLocationAddress = allTruckLocationAddress;
    }

    // Getters and setters for the number of ratings field
    public int getAllTruckNumRating() {
        return allTruckNumRating;
    }

    public void setAllTruckNumRating(int truckNumRating) {
        this.allTruckNumRating = truckNumRating;
    }

    // Getters and setters for the truck image (Drawable resource)
    public int getAllTrucksImage() {
        return allTrucksImage;
    }

    public void setAllTrucksImage(int allTrucksImage) {
        this.allTrucksImage = allTrucksImage;
    }

    // Getters and setters for the star image field
    public int getAllTruckImageStar() {
        return allTruckImageStar;
    }

    public void setAllTruckImageStar(int allTruckImageStar) {
        this.allTruckImageStar = allTruckImageStar;
    }

    // Getters and setters for the ratings string
    public String getAllTruckRatings() {
        return allTruckRatings;
    }

    public void setAllTruckRatings(String allTruckRatings) {
        this.allTruckRatings = allTruckRatings;
    }

    // Getters and setters for the truck name
    public String getAllTruckName() {
        return allTruckName;
    }

    public void setAllTruckName(String allTruckName) {
        this.allTruckName = allTruckName;
    }

    // Getters and setters for the Bitmap image
    public Bitmap getTruckImageBitmap() {
        return truckImageBitmap;
    }

    public void setTruckImageBitmap(Bitmap truckImageBitmap) {
        this.truckImageBitmap = truckImageBitmap;
    }
}


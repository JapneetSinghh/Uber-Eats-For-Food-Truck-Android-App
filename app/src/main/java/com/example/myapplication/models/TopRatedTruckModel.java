package com.example.myapplication.models;

import android.graphics.Bitmap;

public class TopRatedTruckModel {
    private Bitmap topRatedImage; // Changed type from int to Bitmap
    public String topRatedTruckName;
    public int topRatedRating;
    public String topRatedLocation;
    private int topRatedImageStar;
    public int topRatedNumRatings; // Added number of ratings

    public TopRatedTruckModel(String topRatedTruckName, int topRatedRating, String topRatedLocation, Bitmap topRatedImage, int topRatedImageStar, int topRatedNumRatings) {
        this.topRatedTruckName = topRatedTruckName;
        this.topRatedRating = topRatedRating;
        this.topRatedLocation = topRatedLocation;
        this.topRatedImage = topRatedImage; // No error now
        this.topRatedImageStar = topRatedImageStar;
        this.topRatedNumRatings = topRatedNumRatings; // Initialize new attribute
    }

    public String getTopRatedTruckName() {
        return topRatedTruckName;
    }

    public void setTopRatedTruckName(String topRatedTruckName) {
        this.topRatedTruckName = topRatedTruckName;
    }

    public int getTopRatedRating() {
        return topRatedRating;
    }

    public void setTopRatedRating(int topRatedRating) {
        this.topRatedRating = topRatedRating;
    }

    public String getTopRatedLocation() {
        return topRatedLocation;
    }

    public void setTopRatedLocation(String topRatedLocation) {
        this.topRatedLocation = topRatedLocation;
    }

    public Bitmap getTopRatedImage() { // Getter for Bitmap
        return topRatedImage;
    }

    public void setTopRatedImage(Bitmap topRatedImage) { // Setter for Bitmap
        this.topRatedImage = topRatedImage;
    }

    public int getTopRatedImageStar() {
        return topRatedImageStar;
    }

    public void setTopRatedImageStar(int topRatedImageStar) {
        this.topRatedImageStar = topRatedImageStar;
    }

    public int getTopRatedNumRatings() {
        return topRatedNumRatings; // Getter for new attribute
    }

    public void setTopRatedNumRatings(int topRatedNumRatings) {
        this.topRatedNumRatings = topRatedNumRatings; // Setter for new attribute
    }
}

package com.example.myapplication.models;

import android.graphics.Bitmap;

public class PopularTruckModel {
    public String popularTruckName;
    public int popularRating;
    public String popularLocation;
    int popularImageStar;
    public int popularNumRatings;
    public Bitmap popularImage;

    public PopularTruckModel(String popularTruckName, int popularRating, String popularLocation, Bitmap popularImage, int popularImageStar, int popularNumRatings) {
        this.popularTruckName = popularTruckName;
        this.popularRating = popularRating;
        this.popularLocation = popularLocation;
        this.popularImage = popularImage;  // This is now correct
        this.popularImageStar = popularImageStar;
        this.popularNumRatings = popularNumRatings;
    }

    public String getPopularTruckName() {
        return popularTruckName;
    }

    public void setPopularTruckName(String popularTruckName) {
        this.popularTruckName = popularTruckName;
    }

    public int getPopularRating() {
        return popularRating;
    }

    public void setPopularRating(int popularRating) {
        this.popularRating = popularRating;
    }

    public String getPopularLocation() {
        return popularLocation;
    }

    public void setPopularLocation(String popularLocation) {
        this.popularLocation = popularLocation;
    }

    public Bitmap getPopularImage() {
        return popularImage;
    }

    public void setPopularImage(Bitmap popularImage) {
        this.popularImage = popularImage;
    }

    public int getPopularImageStar() {
        return popularImageStar;
    }

    public void setPopularImageStar(int popularImageStar) {
        this.popularImageStar = popularImageStar;
    }

    public int getPopularNumRatings() {
        return popularNumRatings; // Getter for new attribute
    }

    public void setPopularNumRatings(int popularNumRatings) {
        this.popularNumRatings = popularNumRatings; // Setter for new attribute
    }
}

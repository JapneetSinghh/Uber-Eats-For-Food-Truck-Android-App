package com.example.myapplication.models;

// FoodTruck.java
public class SearchFoodModel {
    private String name;
    private String location;
    private int reviewsCount;

    // Constructor
    public SearchFoodModel(String name, String location, int reviewsCount) {
        this.name = name;
        this.location = location;
        this.reviewsCount = reviewsCount;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(int reviewsCount) {
        this.reviewsCount = reviewsCount;
    }
}

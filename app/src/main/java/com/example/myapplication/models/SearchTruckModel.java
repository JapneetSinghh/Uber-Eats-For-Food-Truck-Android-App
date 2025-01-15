package com.example.myapplication.models;

public class SearchTruckModel {

    private String image;      // URL of the truck's image
    private String name;       // Truck name
    private String address;   // Truck location
    private int reviewCount;   // Number of reviews
    private double rating;     // Average rating

    public SearchTruckModel() {
        // Default constructor required for Firebase
    }

    public SearchTruckModel(String image, String name, String location, int reviewCount, double rating) {
        this.image = image;
        this.name = name;
        this.address = location;
        this.reviewCount = reviewCount;
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * Sets the average rating based on total ratings and review count.
     * @param totalRating The sum of all ratings.
     */
    public void setAverageRating(double totalRating) {
        if (reviewCount > 0) {
            this.rating = totalRating / reviewCount;
        } else {
            this.rating = 0; // Default to 0 if no reviews
        }
    }
}

package com.example.myapplication;
public class Review {
    private final String userName;
    private final String reviewText;
    private final float rating;
    private final String sentiment;

    public Review(String userName, String reviewText, float rating, String sentiment) {
        this.userName = userName;
        this.reviewText = reviewText;
        this.rating = rating;
        this.sentiment = sentiment;
    }

    public String getUserName() {
        return userName;
    }

    public String getReviewText() {
        return reviewText;
    }

    public float getRating() {
        return rating;
    }

    public String getSentiment() {
        return sentiment;
    }
}

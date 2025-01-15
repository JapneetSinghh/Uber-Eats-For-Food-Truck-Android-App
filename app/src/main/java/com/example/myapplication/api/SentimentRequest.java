package com.example.myapplication.api;


public class SentimentRequest {
    private String reviewText;

    public SentimentRequest(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}

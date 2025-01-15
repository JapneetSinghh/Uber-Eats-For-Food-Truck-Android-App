package com.example.myapplication.api;


public class SentimentResponse {
    private float score;
    private float comparative;
    private String sentiment;

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public float getComparative() {
        return comparative;
    }

    public void setComparative(float comparative) {
        this.comparative = comparative;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }
}

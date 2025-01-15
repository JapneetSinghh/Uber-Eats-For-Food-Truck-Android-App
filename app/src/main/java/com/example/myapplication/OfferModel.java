package com.example.myapplication;


public class OfferModel {
    private final String title;
    private final String description;
    private final String terms;

    public OfferModel(String title, String description, String terms) {
        this.title = title;
        this.description = description;
        this.terms = terms;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getTerms() { return terms; }
}


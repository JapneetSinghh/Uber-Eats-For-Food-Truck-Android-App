package com.example.myapplication.models;

import java.util.Map;

public class User {
    private String name;
    private String email;
    private String password;
    private Map<String, Object> foodTruckDetails; // Can be null initially

    public User() {
    }

    public User(String name, String email, String password, Map<String, Object> foodTruckDetails) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.foodTruckDetails = foodTruckDetails;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Map<String, Object> getFoodTruckDetails() {
        return foodTruckDetails;
    }

    public void setFoodTruckDetails(Map<String, Object> foodTruckDetails) {
        this.foodTruckDetails = foodTruckDetails;
    }
}

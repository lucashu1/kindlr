package com.example.team19.kindlr;

import java.util.ArrayList;

public class User {
    private String username;
    private String hashedPassword;
    private String firstName;
    private String lastName;
    private String city;
    private String state;
    private String phoneNum;
    private String email;
    private double rating;
    private ArrayList<Integer> likedBooks; // array of bookIDs
    private ArrayList<Integer> dislikedBooks; // array of bookIDs

    public User(String username, String hashedPassword, String firstName, String lastName) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public double getRating() {
        return rating;
    }

    public ArrayList<Integer> getLikedBooks() {
        return likedBooks;
    }

    public ArrayList<Integer> getDislikedBooks() {
        return dislikedBooks;
    }

    public String getUsername() {
        return username;
    }
}

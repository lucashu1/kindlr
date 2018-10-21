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
    private int totalRating;
    private int numRatingsReceived;
    private ArrayList<String> likedBookIDs; // array of bookIDs
    private ArrayList<String> dislikedBookIDs; // array of bookIDs

    public User(String username, String hashedPassword, String firstName, String lastName,
                String city, String state, String phoneNum, String email) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.state = state;
        this.phoneNum = phoneNum;
        this.email = email;

        totalRating = 0;
        numRatingsReceived = 0;

        likedBookIDs = new ArrayList<String>();
        dislikedBookIDs = new ArrayList<String>();
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

    // Get average rating (totalRating/numRatingsReceived)
    public double getRating() {
        // don't div
        if (numRatingsReceived == 0)
            return 0;
        return ((double)totalRating)/numRatingsReceived;
    }

    public ArrayList<String> getLikedBooks() {
        return likedBookIDs;
    }

    public ArrayList<String> getDislikedBooks() {
        return dislikedBookIDs;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void likeBook(String bookID) {
        likedBookIDs.add(bookID);
    }

    public void dislikeBook(String bookID) {
        dislikedBookIDs.add(bookID);
    }
}

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
    private ArrayList<Integer> likedBookIDs; // array of bookIDs
    private ArrayList<Integer> dislikedBookIDs; // array of bookIDs

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

        likedBookIDs = new ArrayList<Integer>();
        dislikedBookIDs = new ArrayList<Integer>();
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

    public ArrayList<Integer> getLikedBooks() {
        return likedBookIDs;
    }

    public ArrayList<Integer> getDislikedBooks() {
        return dislikedBookIDs;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void likeBook(int bookID) {
        likedBookIDs.add(bookID);
    }

    public void dislikeBook(int bookID) {
        dislikedBookIDs.add(bookID);
    }
}

package com.example.team19.kindlr;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
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
    private ArrayList<String> likedBooks; // array of bookIDs
    private ArrayList<String> dislikedBooks; // array of bookIDs

    public User() {
        totalRating = 0;
        numRatingsReceived = 0;
    }

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

    public void setFirstName(String name){
        firstName = name;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setHashedPassword(String pass){
        hashedPassword = pass;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setCity(String city){
        this.city = city;
    }

    public void setState(String state){
        this.state = state;
    }

    public void setPhoneNum(String phone){
        phoneNum = phone;
    }

    public void setEmail(String email){
        this.email = email;
    }



    public ArrayList<String> getLikedBooks() {
        return likedBooks;
    }

    public ArrayList<String> getDislikedBooks() {
        return dislikedBooks;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void likeBook(String bookID) {
        likedBooks.add(bookID);
    }

    public void dislikeBook(String bookID) {
        dislikedBooks.add(bookID);
    }
}

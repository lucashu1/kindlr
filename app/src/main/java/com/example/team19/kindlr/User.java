package com.example.team19.kindlr;

import com.google.firebase.firestore.Exclude;

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

        likedBooks = new ArrayList<>();
        dislikedBooks = new ArrayList<>();
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

        likedBooks = new ArrayList<>();
        dislikedBooks = new ArrayList<>();
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

    public int getTotalRating() { return totalRating; }
    public int getNumRatingsReceived() { return numRatingsReceived; }

    // Get average rating (totalRating/numRatingsReceived)
    @Exclude
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

    public void addRating(int rating) {
        this.totalRating += rating;
        this.numRatingsReceived++;
    }

    // Get comma-separated books that this user has liked
        // E.g. "Harry Potter, Software Engineering, Go Dog Go"
    public String getLikedBooksString() {
        String s = "";
        for (int i = 0; i < likedBooks.size() - 1; i++) {
            String bookID = likedBooks.get(i);
            String bookTitle = BookManager.getBookManager().getItemByID(bookID).getBookName();
            s += (bookTitle + ", ");
        }

        String lastBookID = likedBooks.get(likedBooks.size() - 1);
        String lastBookTitle = BookManager.getBookManager().getItemByID(lastBookID).getBookName();
        s += lastBookTitle;

        return s;
    }

    // Return comma-separated books that this user has posted
        // E.g. "Harry Potter, Software Engineering, Go Dog Go"
    public String getPostedBooksString() {
        String s = "";
        ArrayList<Book> ownedBooks = BookManager.getBookManager().getBooksOwnedByUser(this.username);

        for (int i = 0; i < ownedBooks.size() - 1; i++) {
            String bookTitle = ownedBooks.get(i).getBookName();
            s += (bookTitle + ", ");
        }

        String lastBookTitle = ownedBooks.get(ownedBooks.size() - 1).getBookName();
        s += lastBookTitle;

        return s;
    }
}

package com.example.team19.kindlr;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UserManager {

    private FirebaseDatabase database;
    private DatabaseReference ref;
    private DatabaseReference usersRef;
    private Map<String, User> usersMap;
    private Map<Integer, String> bookIDToUsername;
    private User currentUser;

    // Singleton initializer
    private static UserManager userManagerSingleton;
    public static UserManager getUserManager() {
        if (userManagerSingleton == null)
            userManagerSingleton = new UserManager();
        return userManagerSingleton;
    }

    // Constructor
    public UserManager() {
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
//        usersRef = ref.child("users");
        usersMap = new HashMap<String, User>();
        bookIDToUsername = new HashMap<Integer, String>();
        refreshUsers(); // pull from DB
    }

    // Save usersMap to Firebase (write to DB)
    public void saveToFirebase() {
        usersRef.setValue(usersMap);
    }

    // Refresh users (pull from firebase)
    public void refreshUsers() {
        // TODO
    }

    // Add user to UserManager
    public void addUser(User u) {
        usersMap.put(u.getUsername(), u);
        this.saveToFirebase();
    }

    // Return User with given username
    public User getUserByUsername(String username) {
        if (!usersMap.containsKey(username))
            return null;
        return usersMap.get(username);
    }

    // Get currently logged in user object
    public User getCurrentUser() {
        return currentUser;
    }

    // Attempt to login and set currentUser. Return true if successful
    public boolean attemptLogin(String username, String hashedPassword) {
        if (!usersMap.containsKey(username))
            return false;
        User u = usersMap.get(username);
        if (!u.getHashedPassword().equals(hashedPassword))
            return false;
        currentUser = u;
        return true;
    }

    // Get User owner for given bookID
    public User getBookOwner(int bookID) {
        if (!bookIDToUsername.containsKey(bookID))
            return null;
        String username = bookIDToUsername.get(bookID);
        if (!usersMap.containsKey(username))
            return null;
        return usersMap.get(username);
    }

    // Make user like book, and update DB. Return true if successful
    public boolean makeUserLikeBook(String username, int bookID) {
        if (!usersMap.containsKey(username))
            return false;
        usersMap.get(username).likeBook(bookID);
        this.saveToFirebase();
        return true;
    }

    // Make user dislike book, and update DB. Return true if successful
    public boolean makeUserDislikeBook(String username, int bookID) {
        if (!usersMap.containsKey(username))
            return false;
        usersMap.get(username).dislikeBook(bookID);
        this.saveToFirebase();
        return true;
    }
}

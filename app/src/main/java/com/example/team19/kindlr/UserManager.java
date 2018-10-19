package com.example.team19.kindlr;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UserManager {

    private FirebaseDatabase database;
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
        usersMap = new HashMap<String, User>();
        bookIDToUsername = new HashMap<Integer, String>();
        refreshUsers(); // pull from DB

        // On data change, read read usersMap from the database
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                usersMap = (HashMap<String, User>) dataSnapshot.getValue(HashMap.class);
                Log.d("INFO", "Refreshed usersMap");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("WARN", "Failed to read value.", error.toException());
            }
        });
    }

    // Save usersMap to Firebase (write to DB)
    public void saveToFirebase() {
        usersRef.setValue(usersMap);
    }

    // Refresh users (pull from firebase)
    public void refreshUsers() {
        // TODO
    }

    // Return true if given usename is taken
    public boolean usernameTaken(String username) {
        return usersMap.containsKey(username);
    }

    // Add user to UserManager; return true if successful
    public boolean addUser(User u) {
        if (usernameTaken(u.getUsername()) {
            return false;
        }
        usersMap.put(u.getUsername(), u);
        this.saveToFirebase();
        return true;
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

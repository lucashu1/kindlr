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
//    private Map<Integer, String> bookIDToUsername;
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
        usersMap = new HashMap<String, User>();
    }

    // Save usersMap to Firebase (write to DB)
    public void saveToFirebase() {
        usersRef.setValue(usersMap);
    }

    public void initialize() {
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
//        bookIDToUsername = new HashMap<Integer, String>();
//        refreshUsers(); // pull from DB

        // On data change, read read usersMap from the database
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d("TESTINFO", "Users being updated");
                usersMap = new HashMap<String, User>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    usersMap.put(snapshot.getKey(), user);
                }
                Log.d("TESTINFO", "Refreshed usersMap to be " + usersMap.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("WARN", "Failed to read value.", error.toException());
            }
        });
    }

    public HashMap<String, User> getAllUsers() {
        return (HashMap<String, User>) usersMap;
    }

    // Return true if given usename is taken
    public boolean usernameTaken(String username) {
        return usersMap.containsKey(username);
    }

    // Add user to UserManager (using inputted fields); return true if successful
    public boolean addUser(String username, String hashedPassword, String firstName, String lastName,
                           String city, String state, String phoneNum, String email) {
        // Check if username is taken
        if (usernameTaken(username))
            return false;

        // Create new user, add to firebase
        User u = new User(username, hashedPassword, firstName, lastName, city, state, phoneNum, email);
        usersMap.put(username, u);
        saveToFirebase();

        currentUser = u;

        return true;
    }

    public boolean doesUserExist(String username) {
        return (usersMap.containsKey(username));
    }

    public void deleteUser(String username) {
        if (!doesUserExist(username))
            return;

        usersMap.remove(username);
        DatabaseReference userRef = usersRef.child(username);
        userRef.removeValue();
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
        Log.i("TESTLOG", "Attempting to log user in");
        Log.i("TESTLOG", "User map " + usersMap.toString());
        if (!usersMap.containsKey(username)) {
            Log.i("TESTLOG", "User " + username + " does not exist");
            return false;
        }

        Log.i("TESTLOG","Fetching user");
        User u = usersMap.get(username);
        Log.i("TESTLOG", "Real password is " + u.getHashedPassword());

        if (!u.getHashedPassword().equals(hashedPassword))
            return false;

        Log.i("TESTLOG", "Assigning current user");
        currentUser = u;
        return true;
    }

    // Make user like book, and update DB. Return true if successful
    public boolean makeUserLikeBook(String username, String bookID) {
        if (!usersMap.containsKey(username))
            return false;
        if (!BookManager.getBookManager().bookExists(bookID))
            return false;
        usersMap.get(username).likeBook(bookID);
        this.saveToFirebase();
        return true;
    }

    // Make user dislike book, and update DB. Return true if successful
    public boolean makeUserDislikeBook(String username, String bookID) {
        if (!usersMap.containsKey(username))
            return false;
        if (!BookManager.getBookManager().bookExists(bookID))
            return false;
        usersMap.get(username).dislikeBook(bookID);
        this.saveToFirebase();
        return true;
    }

    // Clear all users from Firebase. Can't undo!
    public void clearAllUsers() {
//        usersMap = new HashMap<String, User>();
//        usersRef.setValue(usersMap);
        usersRef.removeValue();
    }
}

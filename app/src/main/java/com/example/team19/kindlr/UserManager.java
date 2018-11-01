package com.example.team19.kindlr;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager {

    private static final boolean ENABLE_FIREBASE_READS = false;

    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private Map<String, User> usersMap;
//    private Map<Integer, String> bookIDToUsername;
    private User currentUser;
    private boolean initialized;

    // Singleton initializer
    private static UserManager userManagerSingleton;
    public synchronized static UserManager getUserManager() {
        if (userManagerSingleton == null)
            userManagerSingleton = new UserManager();
        return userManagerSingleton;
    }

    // Constructor
    public UserManager() {
        usersMap = Collections.synchronizedMap(new HashMap<String, User>());
        initialized = false;
        Log.d("INIT", "Called UserManager constructor");
    }

    // Save usersMap to Firebase (write to DB)
//    public synchronized void saveToFirebase() {
//        usersRef.setValue(usersMap);
//    }

    public void initialize() {
        if (initialized)
            return;

        Log.d("INIT", "Initializing UserManager");

        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
//        bookIDToUsername = new HashMap<Integer, String>();
//        refreshUsers(); // pull from DB

        if (ENABLE_FIREBASE_READS) {
            usersRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    User newUser = dataSnapshot.getValue(User.class);
                    if (!usersMap.containsKey(usersMap)) {
                        Log.d("USERMANAGER", "Adding new user via onChildAdded: " + newUser.getUsername());
                        usersMap.put(newUser.getUsername(), newUser);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                    User changedUser = dataSnapshot.getValue(User.class);
                    usersMap.put(changedUser.getUsername(), changedUser); // overwrite
                    Log.d("USERMANAGER", "Updating user via onChildChanged: " + changedUser.getUsername());
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    User removedUser = dataSnapshot.getValue(User.class);
                    if (usersMap.containsKey(removedUser.getUsername())) {
                        Log.d("USERMANAGER", "Removing user via onChildRemoved: " + removedUser.getUsername());
                        usersMap.remove(removedUser.getUsername());
                    }

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("WARN", "Failed to read value.", error.toException());
                }
            });
        }

        initialized = true;
    }

//    public HashMap<String, User> getAllUsers() {
//        return (HashMap<String, User>) usersMap;
//    }

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
//        usersMap.put(username, u);

        usersRef.child(username).setValue(u); // add user to firebase
        if (!usersMap.containsKey(username))
            usersMap.put(username, u); // update in Map

        currentUser = u;

        return true;
    }

    public boolean doesUserExist(String username) {
        return (usersMap.containsKey(username));
    }

    public void deleteUser(String username) {
        if (!doesUserExist(username))
            return;

        usersRef.child(username).removeValue();
        if (usersMap.containsKey(username))
            usersMap.remove(username);
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
    public synchronized boolean attemptLogin(String username, String hashedPassword) {
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
        usersRef.child(username).setValue(usersMap.get(username)); // update user in firebase
        return true;
    }

    // Make user dislike book, and update DB. Return true if successful
    public boolean makeUserDislikeBook(String username, String bookID) {
        if (!usersMap.containsKey(username))
            return false;
        if (!BookManager.getBookManager().bookExists(bookID))
            return false;

        usersMap.get(username).dislikeBook(bookID);
        usersRef.child(username).setValue(usersMap.get(username)); // update user in firebase
        return true;
    }

    // Clear all users from Firebase. Can't undo!
    public void clearAllUsers() {
//        usersMap = new HashMap<String, User>();
//        usersRef.setValue(usersMap);
        usersRef.removeValue();
    }
}

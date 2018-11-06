package com.example.team19.kindlr;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UserManager extends FirebaseAccessor<User> {

    private static final String TAG = "USERMGR";
    private User currentUser;

    // Singleton initializer
    private static UserManager userManagerSingleton;
    public synchronized static UserManager getUserManager() {
        if (userManagerSingleton == null)
            userManagerSingleton = new UserManager();
        return userManagerSingleton;
    }

    // Constructor
    public UserManager() {
        super(User.class);
    }

    public String getFirebaseRefName() {
        return "users";
    }

    // Return true if given usename is taken
    public boolean usernameTaken(String username) {
        return this.doesUserExist(username);
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

        Log.d(TAG, "Adding user: " + username);
        this.getItemsMap().put(username, u); // update in Map
        this.getItemsMap().put(username, u); // update in Map

        currentUser = u;

        return true;
    }

    public void updateCurrentUser(User updatedUser){
        DatabaseReference updateRef = usersRef.child(updatedUser.getUsername());
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put(updatedUser.getUsername(), updatedUser);
        Log.d("update",updatedUser.getCity());
        usersRef.updateChildren(userUpdates);

//        userUpdates.put("alanisawesome/nickname", "Alan The Machine");
//        usersRef.child(username).child("username").s
    }

    public boolean doesUserExist(String username) {
        return this.getItemsMap().containsKey(username);
    }

    // Return User with given username
    public User getUserByUsername(String username) {
        if (!this.getItemsMap().containsKey(username))
            return null;
        return this.getItemsMap().get(username);
    }

    // Get currently logged in user object
    public User getCurrentUser() {
        return currentUser;
    }

    // Attempt to login and set currentUser. Return true if successful
    public boolean attemptLogin(String username, String hashedPassword) {
        if (!this.getItemsMap().containsKey(username)) {
            Log.i(TAG, "User " + username + " does not exist");
            return false;
        }

        Log.i(TAG,"Fetching user");
        User u = this.getItemsMap().get(username);
        Log.i(TAG, "Real password is " + u.getHashedPassword());

        if (!u.getHashedPassword().equals(hashedPassword))
            return false;

        Log.i(TAG, "Assigning current user");
        currentUser = u;
        return true;
    }

    // Make user like book, and update DB. Return true if successful
    public boolean makeUserLikeBook(String username, String bookID) {
        if (!this.getItemsMap().containsKey(username))
            return false;
        if (!BookManager.getBookManager().doesItemExist(bookID))
            return false;

        this.getItemsMap().get(username).likeBook(bookID);
        updateChild(username);
        return true;
    }

    // Make user dislike book, and update DB. Return true if successful
    public boolean makeUserDislikeBook(String username, String bookID) {
        if (!this.getItemsMap().containsKey(username))
            return false;
        if (!BookManager.getBookManager().doesItemExist(bookID))
            return false;

        this.getItemsMap().get(username).dislikeBook(bookID);
        updateChild(username);
        return true;
    }
}

package com.example.team19.kindlr;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UserManager {

    FirebaseDatabase database;
    DatabaseReference ref;
    DatabaseReference usersRef;
    Map<String, User> usersMap;

    // Singleton initializer
    private static UserManager userManagerSingleton;
    public static UserManager getUserManager() {
        if (userManagerSingleton == null)
            userManagerSingleton = new UserManager();
        return userManagerSingleton;
    }

    // Constructor
    public UserManager() {
        database  = FirebaseDatabase.getInstance();
        ref = database.getReference("users");
        usersRef = ref.child("users");
        usersMap = new HashMap<>();
    }

    // Add user to UserManager
    public void addUser(User u) {
        usersMap.put(u.getUsername(), u);
        usersRef.setValue(usersMap); // update Firebase
    }

    // Return User with given username
    public User getUserByUsername(String username) {
        if (!usersMap.containsKey(username))
            return null;
        return usersMap.get(username);
    }

}

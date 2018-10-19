package com.example.team19.kindlr;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FirebaseTests {
    @Test
    public void initFirebaseTest() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        FirebaseApp.initializeApp(appContext);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("testMessage");
        myRef.setValue("Hello, World!");
    }

    @Test
    public void testUserManager() {
        FirebaseApp.initializeApp(InstrumentationRegistry.getTargetContext());
        UserManager um = UserManager.getUserManager();
        User u = new User("user1", "abcdef", "Lucas", "Hu",
                "Los Angeles", "California", "111 111 1234", "lucashu@usc.edu");
        um.addUser(u);
        User uAgain = um.getUserByUsername("user1");
        System.out.println(uAgain.getUsername());
        assertEquals(uAgain.getFirstName(), "Lucas");
    }
}

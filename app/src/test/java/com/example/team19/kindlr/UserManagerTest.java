package com.example.team19.kindlr;

import com.google.firebase.FirebaseApp;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

public class UserManagerTest {

    @Test
    public void testUserManager() {
        UserManager um = UserManager.getUserManager();
        User u = new User("user1", "abcdef", "Lucas", "Hu",
                "Los Angeles", "California", "111 111 1234", "lucashu@usc.edu");
        um.addUser(u);
        User uAgain = um.getUserByUsername("user1");
        assertEquals(uAgain.getFirstName(), "Lucas");
    }
}

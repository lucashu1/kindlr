package com.example.team19.kindlr;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserManagerTest {
    @Test
    public void testUserManager() {
        UserManager um = new UserManager();
        User u = new User("user1", "abcdef", "Lucas", "Hu");
        um.addUser(u);
        User uAgain = um.getUserByUsername("user1");
        assertEquals(uAgain.getUsername(), "user1");
    }
}

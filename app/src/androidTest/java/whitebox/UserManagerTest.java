package whitebox;

import com.example.team19.kindlr.UserManager;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserManagerTest {

    static String username = "andyTest";
    static String password = "asdfasdf";
    static String email = "szot@usc.edu";

//    @BeforeClass
//    public static void prepUserManagerTests() {
//        UserManager.getUserManager().initialize(true);
//        UserManager um = UserManager.getUserManager();
//    }

    @Before
    public void createTestUser() {
        UserManager.getUserManager().addUser(username, password, "Andrew", "Szot", "Los Angeles", "California", "123-456-7890", email);
    }

    @Test
    public void testGetUser() {
        UserManager um = UserManager.getUserManager();
        assertTrue(um.doesUserExist(username));
        assertEquals(um.getUserByUsername(username).getEmail(), email);
        um.deleteItem(username);
    }

    @Test
    public void testLoginSuccessful() {
        UserManager um = UserManager.getUserManager();
        assertTrue(um.attemptLogin(username, password));
    }

    @Test
    public void testLoginUnsuccessful() {
        UserManager um = UserManager.getUserManager();
        assertTrue(!um.attemptLogin(username, "wrongpassword"));
    }

    @After
    public void deleteTestUser() {
        UserManager.getUserManager().deleteItem(username);
    }
}

package whitebox;

import com.example.team19.kindlr.UserManager;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserManagerTest {
    @BeforeClass
    public static void initUserManager() {
        UserManager.getUserManager().initialize();
    }

    @Test
    public void testAddUser() {
        String username = "andyTest";
        String email = "szot@usc.edu";
        UserManager um = UserManager.getUserManager();
        um.addUser(username, "asdf", "Andrew", "Szot", "Los Angeles", "California", "123-456-7890", email);
        assertTrue(um.doesUserExist(username));
        assertEquals(um.getUserByUsername(username).getEmail(), email);
        um.deleteUser(username);
    }
}

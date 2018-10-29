package whitebox;

import com.example.team19.kindlr.BookManager;
import com.example.team19.kindlr.UserManager;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

public class BookManagerTests {

    private static String username = "andyTest";
    private static String password = "asdfasdf";
    private static String email = "szot@usc.edu";

    @BeforeClass
    public static void initFirebase() {
        UserManager.getUserManager().initialize();
        BookManager.getBookManager().initialize();
    }

    // TODO (Lucas)

    @Before
    public void createTestUser() {
        UserManager.getUserManager().addUser(username, password, "Andrew", "Szot", "Los Angeles", "California", "123-456-7890", email);
    }

    @After
    public void deleteTestUser() {
        UserManager.getUserManager().deleteUser(username);
    }

}

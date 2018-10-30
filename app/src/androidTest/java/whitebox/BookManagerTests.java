package whitebox;

import com.example.team19.kindlr.BookManager;
import com.example.team19.kindlr.UserManager;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

public class BookManagerTests {

    private static String username = "andyTest";
    private static String password = "asdfasdf";
    private static String email = "szot@usc.edu";

    @BeforeClass
    public static void initFirebase() {
        UserManager.getUserManager().initialize();
        BookManager.getBookManager().initialize();
    }

    @Before
    public void createTestUser() {
        UserManager.getUserManager().addUser(username, password, "Andrew", "Szot", "Los Angeles", "California", "123-456-7890", email);
    }

    @Test
    public void testPostBookForSale() {
        BookManager bm = BookManager.getBookManager();
        ArrayList<String> sweTags = new ArrayList<String>();
        sweTags.add("Textbook");
        String sweID = bm.postBookForSale("Software Engineering", "978-0133943030", "Ian Sommerville", "Comedy", 500, sweTags, username);
        assertTrue(bm.bookExists(sweID));
        assertTrue(bm.getBookByID(sweID).getForSale());
        assertEquals(bm.getBookByID(sweID).getIsbn(), "978-0133943030");
        bm.deleteBook(sweID);
    }

    @Test
    public void testPostBookForExchange() {
        BookManager bm = BookManager.getBookManager();
        ArrayList<String> harryPotterTags = new ArrayList<String>();
        harryPotterTags.add("Good for kids");
        harryPotterTags.add("Magic");
        String harryPotterID = bm.postBookForExchange("Harry Potter and the Philosopher's Stone", "9789604533084", "J.K. Rowling", "Fantasy", 300, harryPotterTags, username);
        assertTrue(bm.bookExists(harryPotterID));
        assertTrue(!bm.getBookByID(harryPotterID).getForSale());
        assertEquals(bm.getBookByID(harryPotterID).getIsbn(), "9789604533084");
        bm.deleteBook(harryPotterID);
    }

    @Test
    public void testBookFiltering() {
        // TODO (Lucas)
    }

    @After
    public void deleteTestUser() {
        UserManager.getUserManager().deleteUser(username);
    }

}

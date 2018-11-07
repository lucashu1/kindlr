package whitebox;

import android.util.Log;

import com.example.team19.kindlr.Book;
import com.example.team19.kindlr.BookFilter;
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
        UserManager.getUserManager().addUser("testUser2", password, "Test", "User", "Los Angeles", "California", "123-456-7890", email);
    }

    @Test
    public void testPostBookForSale() {
        BookManager bm = BookManager.getBookManager();
        ArrayList<String> sweTags = new ArrayList<String>();
        sweTags.add("Textbook");
        String sweID = bm.postBookForSale("Software Engineering", "978-0133943030", "Ian Sommerville", "Comedy", 500, sweTags, username);
        assertTrue(bm.doesItemExist(sweID));
        assertTrue(bm.getItemByID(sweID).getForSale());
        assertEquals(bm.getItemByID(sweID).getIsbn(), "978-0133943030");
        bm.deleteItem(sweID);
    }

    @Test
    public void testPostBookForExchange() {
        BookManager bm = BookManager.getBookManager();
        ArrayList<String> harryPotterTags = new ArrayList<String>();
        harryPotterTags.add("Good for kids");
        harryPotterTags.add("Magic");
        String harryPotterID = bm.postBookForExchange("Harry Potter and the Philosopher's Stone", "9789604533084", "J.K. Rowling", "Fantasy", 300, harryPotterTags, username);
        assertTrue(bm.doesItemExist(harryPotterID));
        assertTrue(!bm.getItemByID(harryPotterID).getForSale());
        assertEquals(bm.getItemByID(harryPotterID).getIsbn(), "9789604533084");
        bm.deleteItem(harryPotterID);
    }

    @Test
    public void testBookFiltering() {
        BookManager bm = BookManager.getBookManager();
        ArrayList<String> chaoWangTags = new ArrayList<String>();
        chaoWangTags.add("Professorial");
        String book1ID = bm.postBookForExchange("Hello World", "12345", "Chao Wang", "Knowledge", 999, chaoWangTags, username);
        ArrayList<String> halfondTags = new ArrayList<String>();
        halfondTags.add("Sabbatical");
        String book2ID = bm.postBookForExchange("I Am Legend", "54321", "William Halfond", "Wisdom", 999, halfondTags, username);
        BookFilter bf = new BookFilter("Hello World");
        bf.setAuthor("Chao Wang");
        ArrayList<Book> filteredBooks = new ArrayList<Book>(bm.getFilteredBooks(bf, UserManager.getUserManager().getUserByUsername("testUser2")));
        Log.d("BOOKFILTERTEST", "Length of filteredBooks: " + filteredBooks.size());
        for (int i = 0; i < filteredBooks.size(); i++) {
            Log.d("BOOKFILTERTEST", "Filtered book name: " + filteredBooks.get(i).getBookName());
        }
        assertEquals(filteredBooks.size(), 1);
        assertEquals(filteredBooks.get(0).getBookName(), "Hello World");
        bm.deleteItem(book1ID);
        bm.deleteItem(book2ID);
    }

    @After
    public void deleteTestUsers() {
        UserManager.getUserManager().deleteItem(username);
        UserManager.getUserManager().deleteItem("testUser2");
    }

}

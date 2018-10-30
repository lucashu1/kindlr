package whitebox;

import com.example.team19.kindlr.BookManager;
import com.example.team19.kindlr.Transaction;
import com.example.team19.kindlr.TransactionManager;
import com.example.team19.kindlr.User;
import com.example.team19.kindlr.UserManager;
import com.example.team19.kindlr.ViewNotificationsActivity;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ViewNotificationsActivityTest {

    private static UserManager um;
    private static TransactionManager tm;
    private static BookManager bm;
    private static String bookId1;
    private static String bookId2;
    private static String txId1;
    private static String txId2;
    private static ArrayList<Transaction> matches;

    @BeforeClass
    public static void initManagers() {
        UserManager.getUserManager().initialize();
        BookManager.getBookManager().initialize();
        TransactionManager.getTransactionManager().initialize();
    }

    @Before
    public void initialize() {
        um = UserManager.getUserManager();
        tm = TransactionManager.getTransactionManager();
        bm = BookManager.getBookManager();

        um.addUser("bhahntest", "asdf", "Ben", "Hahn", "Los Angeles", "California", "314-555-5555", "bhahn@usc.edu");
        um.addUser("shahntest", "asdf", "Sam", "Hahn", "St. Louis", "Missouri", "314-555-5556", "shahn@wust.edu");
        bookId1 = bm.postBookForExchange("Software Engineering", "978-3-16-148410-3", "Ian Sommerville", "Computer Science", 9001, new ArrayList<String>(), "bhahntest");
        bookId2 = bm.postBookForExchange("It", "978-3-16-148410-4", "Stephen King", "Horror", 47, new ArrayList<String>(), "shahntest");
        txId1 = tm.makeUserLikeBook("bhahntest", bookId2);
        txId2 = tm.makeUserLikeBook("shahntest", bookId1);
        matches = tm.getAllMatchedTransactionsForUser("bhahntest");
    }

    @Test
    public void testMatchesFound() {
        assertTrue(matches.size() == 1);
        assertTrue(matches.get(0) != null);
    }

    @Test
    public void testMatchesAreCorrect() {
        assertTrue(matches.get(0).getOtherUsernameInTransaction().equals("shahntest"));
        assertTrue(matches.get(0).getOtherUsersBook().getBookName().equals("It"));
    }

    @AfterClass
    public static void deleteTestVariables() {
        um.deleteUser("bhahntest");
        um.deleteUser("shahntest");
        bm.deleteBook(bookId1);
        bm.deleteBook(bookId2);
        tm.deleteExchangeTransaction(txId1);
        tm.deleteExchangeTransaction(txId2);
    }

}

package whitebox;

import android.util.Log;

import com.example.team19.kindlr.BookManager;
import com.example.team19.kindlr.Transaction;
import com.example.team19.kindlr.TransactionManager;
import com.example.team19.kindlr.UserManager;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ViewNotificationsActivityTest {

//    private static String bookId1;
//    private static String bookId2;
//    private static String txId1;
//    private static String txId2;
//    private static ArrayList<Transaction> matches;

    private static final String TAG = "ViewNotificationsActivityTest";

    @BeforeClass
    public static void initManagers() {
        UserManager.getUserManager().initialize(true);
        BookManager.getBookManager().initialize(true);
        TransactionManager.getTransactionManager().initialize(true);
    }

    @Before
    public void initialize() {
        UserManager.getUserManager().addUser("bhahntest", "asdf", "Ben", "Hahn", "Los Angeles", "California", "314-555-5555", "bhahn@usc.edu");
        UserManager.getUserManager().addUser("shahntest", "asdf", "Sam", "Hahn", "St. Louis", "Missouri", "314-555-5556", "shahn@wust.edu");
    }

    @Test
    public void testMatchesFound() {
        boolean loggedIn = UserManager.getUserManager().attemptLogin("bhahntest", "asdf");
        assertTrue(loggedIn);

        // Set up transaction
        String bookId1 = BookManager.getBookManager().postBookForExchange("Software Engineering", "978-3-16-148410-3", "Ian Sommerville", "Computer Science", 9001, new ArrayList<String>(), "bhahntest");
        String bookId2 = BookManager.getBookManager().postBookForExchange("It", "978-3-16-148410-4", "Stephen King", "Horror", 47, new ArrayList<String>(), "shahntest");

        Log.d(TAG, "testMatchesFound(): posted books with IDs " + bookId1 + "; " + bookId2);

        String txId1 = TransactionManager.getTransactionManager().makeUserLikeBook("bhahntest", bookId2);
        String txId2 = TransactionManager.getTransactionManager().makeUserLikeBook("shahntest", bookId1);
        ArrayList<Transaction> matches = TransactionManager.getTransactionManager().exchangeTransMgr.getAllMatchedTransactionsForUser("bhahntest");

        assertTrue(matches.size() == 1);
        assertTrue(matches.get(0) != null);
        assertTrue(matches.get(0).getOtherUserInTransaction().getUsername().equals("shahntest"));
        assertTrue(matches.get(0).getOtherUsersBook().getBookName().equals("It"));

        // Clean up
        TransactionManager.getTransactionManager().exchangeTransMgr.deleteItem(txId1);
        BookManager.getBookManager().deleteItem(bookId1);
        BookManager.getBookManager().deleteItem(bookId2);
    }

//    @Test
//    public void testMatchesAreCorrect() {
//        assertTrue(matches.get(0).getOtherUserInTransaction().getUsername().equals("shahntest"));
//        assertTrue(matches.get(0).getOtherUsersBook().getBookName().equals("It"));
//    }

    @After
    public void cleanUp() {
        UserManager.getUserManager().deleteItem("bhahntest");
        UserManager.getUserManager().deleteItem("shahntest");
//        TransactionManager.getTransactionManager().deleteExchangeTransaction(txId1);
//        BookManager.getBookManager().deleteBook(bookId1);
//        BookManager.getBookManager().deleteBook(bookId2);
    }


}

package whitebox;

import com.example.team19.kindlr.Book;
import com.example.team19.kindlr.BookManager;
import com.example.team19.kindlr.ExchangeTransaction;
import com.example.team19.kindlr.ForSaleTransaction;
import com.example.team19.kindlr.Transaction;
import com.example.team19.kindlr.TransactionManager;
import com.example.team19.kindlr.UserManager;
import java.util.List;
import java.util.ArrayList;

import android.support.v4.widget.TextViewCompat;
import android.util.Log;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;


public class TransactionManagerUnitTests {

    private String exchangeBookID;
    private String saleBookID;

    @BeforeClass
    public static void initManagers() {
        UserManager.getUserManager().initialize();
        BookManager.getBookManager().initialize();
        TransactionManager.getTransactionManager().initialize();
    }

    @Before
    public void initialize()
    {
        UserManager.getUserManager().addUser("testUser1", "", "Mr", "Test", "Los Angeles", "California", "3333333333", "test1@usc.edu");
        UserManager.getUserManager().addUser("testUser2", "", "Mrs", "Test", "Los Angeles", "California", "4444444444", "test2@usc.edu");
        List<String> tags = new ArrayList<String>();
        Log.d("TESTEXCHANGE", "TESTING");
        exchangeBookID = BookManager.getBookManager().postBookForExchange("TestBook1", "978-3-16-148410-0", "author1", "TestGenre", 300, tags, "testUser1");
        saleBookID = BookManager.getBookManager().postBookForSale("testBook2", "978-3-16-148410-2", "author2", "TestGenre", 20, tags, "testUser1");
    }

    @Test
    public void testUserLikeSaleBook()
    {
        Log.d("TESTEXCHANGE", "TESTING");
        Log.d("EXCHANGE", exchangeBookID);
        Log.d("SALE", saleBookID);
        String saleID = TransactionManager.getTransactionManager().makeUserLikeBook("testUser2", saleBookID);
        ForSaleTransaction fst = TransactionManager.getTransactionManager().getForSaleTransactionByID(saleID);
        Log.d("SALE", "Other book id: " + fst.getOtherUsersBook().getBookID());
        assertEquals(fst.getUsername1(), "testUser1");
        assertEquals(fst.getUsername2(), "testUser2");
        assertEquals(fst.getOtherUsersBook().getBookName(), "TestBook1");
    }

    @Test
    public void testUserLikeExchangeBook()
    {
        String exchangeID = TransactionManager.getTransactionManager().makeUserLikeBook("testUser2", exchangeBookID);
        ExchangeTransaction et = TransactionManager.getTransactionManager().getExchangeTransactionByID(exchangeBookID);
        assertEquals(et.getUsername1(), "testUser1");
        assertEquals(et.getUsername2(), "testUser2");
        assertEquals(et.getOtherUsersBook().getBookName(), "TestBook2");
    }
    @Test
    public void testUserDislikeBook()
    {
        List<String> tags = new ArrayList<String>();
        boolean disliked = TransactionManager.getTransactionManager().makeUserDislikeBook("testUser2", saleBookID);
        assertTrue(disliked);
    }

    @Test
    public void testAddNewForSaleTransaction()
    {
        String transactionId = TransactionManager.getTransactionManager().addNewForSaleTransaction("testUser2", saleBookID, "testUser1");
        ForSaleTransaction fst = TransactionManager.getTransactionManager().getForSaleTransactionByID(transactionId);
        assertEquals(fst.getUsername1(), "testUser2");
        assertEquals(fst.getUsername2(), "testUser1");
        assertEquals(fst.getOtherUsersBook().getBookName(), "TestBook1");
    }

    @Test
    public void testForSaleTransactionExists()
    {
        String transactionId = TransactionManager.getTransactionManager().addNewForSaleTransaction("testUser2", saleBookID, "testUser1");
        boolean forSaleExists = TransactionManager.getTransactionManager().forSaleTransactionExists(transactionId);
        assert(forSaleExists, true);
    }

    @Test
    public void testExchangeTransactionExists()
    {
        String transactionId = TransactionManager.getTransactionManager().makeUserLikeBook("testUser2", exchangeBookID);
        boolean exchangeTransactionExists = TransactionManager.getTransactionManager().exchangeTransactionExists(transactionId);
        assert(exchangeTransactionExists, true);
    }

    @After
    public void deleteTestVariables() {
        UserManager.getUserManager().deleteUser("testUser1");
        UserManager.getUserManager().deleteUser("testUser2");
        BookManager.getBookManager().deleteBook(exchangeBookID);
        BookManager.getBookManager().deleteBook(saleBookID);
    }
}

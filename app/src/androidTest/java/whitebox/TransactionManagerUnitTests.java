package whitebox;

import com.example.team19.kindlr.Book;
import com.example.team19.kindlr.BookManager;
import com.example.team19.kindlr.ExchangeTransaction;
import com.example.team19.kindlr.ForSaleTransaction;
import com.example.team19.kindlr.TransactionManager;
import com.example.team19.kindlr.UserManager;

import android.util.Log;


import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;


public class TransactionManagerUnitTests {

    @BeforeClass
    public static void initManagers() {
        UserManager.getUserManager().initialize();
        BookManager.getBookManager().initialize();
        TransactionManager.getTransactionManager().initialize();
    }

    @Before
    public void addUsers()
    {
        UserManager.getUserManager().addUser("testUser1", "", "Mr", "Test", "Los Angeles", "California", "3333333333", "test1@usc.edu");
        UserManager.getUserManager().addUser("testUser2", "", "Mrs", "Test", "Los Angeles", "California", "4444444444", "test2@usc.edu");
    }


    @Test
    public void testAddNewForSaleTransaction()
    {
        // testUser1 posts "testBook1"
        String saleBookID = BookManager.getBookManager().postBookForSale("testBook1", "978-3-16-148410-2", "author2", "TestGenre", 20, null, "testUser1");

        // Make new transaction: testUser2 likes book
        String transactionId = TransactionManager.getTransactionManager().addNewForSaleTransaction("testUser2", saleBookID, "testUser1");
        ForSaleTransaction fst = TransactionManager.getTransactionManager().getForSaleTransactionByID(transactionId);

        assertEquals(fst.getUsername1(), "testUser2");
        assertEquals(fst.getUsername2(), "testUser1");
        assertEquals(fst.getUser1LikedBookID(), saleBookID);

        // Clean up
        BookManager.getBookManager().deleteBook(saleBookID);
        TransactionManager.getTransactionManager().deleteForSaleTransaction(transactionId);
    }


    @Test
    public void testForSaleTransactionExists()
    {
        // testUser1 posts "testBook1"
        String saleBookID = BookManager.getBookManager().postBookForSale("testBook1", "978-3-16-148410-2", "author2", "TestGenre", 20, null, "testUser1");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String transactionId = TransactionManager.getTransactionManager().addNewForSaleTransaction("testUser2", saleBookID, "testUser1");
        boolean forSaleExists = TransactionManager.getTransactionManager().forSaleTransactionExists(transactionId);
        assertTrue(forSaleExists);

        // Clean up
        BookManager.getBookManager().deleteBook(saleBookID);
        TransactionManager.getTransactionManager().deleteForSaleTransaction(transactionId);
    }

    @Test
    public void testExchangeTransactionExists()
    {
        // testUser1 and testUser2 post books for exchange
        String book1ID = BookManager.getBookManager().postBookForExchange("testBook1", "978-3-16-148410-2", "author2", "TestGenre", 20, null, "testUser1");
        String book2ID = BookManager.getBookManager().postBookForExchange("testBook2", "978-3-16-148410-2", "author2", "TestGenre", 20, null, "testUser2");

        String transactionID = TransactionManager.getTransactionManager().addNewUnmatchedExchangeTransaction("testUser1", "testBook2");
        boolean exchangeTransactionExists = TransactionManager.getTransactionManager().exchangeTransactionExists(transactionID);

        assertTrue(exchangeTransactionExists);

        // Clean up
        TransactionManager.getTransactionManager().deleteExchangeTransaction(transactionID);
        BookManager.getBookManager().deleteBook(book1ID);
        BookManager.getBookManager().deleteBook(book2ID);
    }

    @Test
    public void testMatchExchangeTransaction()
    {
        // testUser1 and testUser2 post books for exchange
        String book1ID = BookManager.getBookManager().postBookForExchange("testBook1", "978-3-16-148410-2", "author2", "TestGenre", 20, null, "testUser1");
        String book2ID = BookManager.getBookManager().postBookForExchange("testBook2", "978-3-16-148410-2", "author2", "TestGenre", 20, null, "testUser2");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String transactionID = TransactionManager.getTransactionManager().addNewUnmatchedExchangeTransaction("testUser1", book2ID);
        ExchangeTransaction t = TransactionManager.getTransactionManager().getExchangeTransactionByID(transactionID);
        t.matchExchangeTransaction("testUser2", book1ID);
        assertTrue(t.isMatched());

        // Clean up
        TransactionManager.getTransactionManager().deleteExchangeTransaction(transactionID);
        BookManager.getBookManager().deleteBook(book1ID);
        BookManager.getBookManager().deleteBook(book2ID);
    }

    @Test
    public void testUserLikeSaleBook()
    {
        // testUser2 posts "testBook2" for sale
        String saleBookID = BookManager.getBookManager().postBookForSale("testBook2", "978-3-16-148410-2", "author2", "TestGenre", 20, null, "testUser2");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d("TESTEXCHANGE", "TRYING testUserLikeSaleBook()");
        Log.d("TESTEXCHANGE", "forSaleBookID: " + saleBookID);

        // testUser1 likes testBook2
        String saleID = TransactionManager.getTransactionManager().makeUserLikeBook("testUser1", saleBookID);
        ForSaleTransaction fst = TransactionManager.getTransactionManager().getForSaleTransactionByID(saleID);

        assertEquals(fst.getUsername1(), "testUser1");
        assertEquals(fst.getUser1LikedBookID(), saleBookID);
        assertEquals(fst.getUsername2(), "testUser2");
        assertTrue(fst.isMatched());

        TransactionManager.getTransactionManager().deleteForSaleTransaction(saleID);
        BookManager.getBookManager().deleteBook(saleBookID);
    }

    @Test
    public void testUserLikeExchangeBook()
    {
        // testUser1 and testUser2 post books for exchange
        String book1ID = BookManager.getBookManager().postBookForExchange("testBook1", "978-3-16-148410-2", "author2", "TestGenre", 20, null, "testUser1");
        String book2ID = BookManager.getBookManager().postBookForExchange("testBook2", "1234567890", "author2", "Comedy", 100, null, "testUser2");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // testUser1 likes testBook2 --> unmatched
        String transactionID = TransactionManager.getTransactionManager().makeUserLikeBook("testUser1", book2ID);
        ExchangeTransaction t = TransactionManager.getTransactionManager().getExchangeTransactionByID(transactionID);
        assertTrue(!t.isMatched());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // testUser2 likes testBook1 --> matched
        transactionID = TransactionManager.getTransactionManager().makeUserLikeBook("testUser2", book1ID);
//        assertEquals(transactionID, transactionIDAgain);
        t = TransactionManager.getTransactionManager().getExchangeTransactionByID(transactionID);
        assertTrue(t.isMatched());

        assertEquals(t.getUsername1(), "testUser1");
        assertEquals(t.getUsername2(), "testUser2");
        assertEquals(BookManager.getBookManager().getBookByID(t.getUser1LikedBookID()).getBookName(), "testBook2");
        assertEquals(BookManager.getBookManager().getBookByID(t.getUser2LikedBookID()).getBookName(), "testBook1");

        // Clean up
        TransactionManager.getTransactionManager().deleteExchangeTransaction(transactionID);
        BookManager.getBookManager().deleteBook(book1ID);
        BookManager.getBookManager().deleteBook(book2ID);
    }


    @Test
    public void testUserDislikeBook()
    {
        // testUser1 posts "testBook1"
        String saleBookID = BookManager.getBookManager().postBookForSale("testBook1", "978-3-16-148410-2", "author2", "TestGenre", 20, null, "testUser1");
        boolean disliked = TransactionManager.getTransactionManager().makeUserDislikeBook("testUser2", saleBookID);
        assertTrue(disliked);

        BookManager.getBookManager().deleteBook(saleBookID);
    }


    @After
    public void deleteTestVariables() {
        UserManager.getUserManager().deleteUser("testUser1");
        UserManager.getUserManager().deleteUser("testUser2");
    }
}

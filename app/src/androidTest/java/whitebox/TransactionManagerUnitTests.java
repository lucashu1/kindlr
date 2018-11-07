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
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TransactionManagerUnitTests {

    @BeforeClass
    public static void initManagers() {
        UserManager.getUserManager().initialize(true);
        BookManager.getBookManager().initialize(true);
        TransactionManager.getTransactionManager().initialize(true);
    }

    @Before
    public void addUsers() {
        Log.d("TESTINFO", "Adding users");
        UserManager.getUserManager().addUser("testUser1", "",
                "Mr", "Test", "Los Angeles", "California",
                "3333333333", "test1@usc.edu");
        UserManager.getUserManager().addUser("testUser2",
                "", "Mrs", "Test",
                "Los Angeles", "California", "4444444444",
                "test2@usc.edu");
    }


    @Test
    public void testAddNewForSaleTransaction() {
        Log.i("TESTINFO", "STARTING for add transaction");
        // testUser1 posts "testBook1"
        String saleBookID = BookManager.getBookManager().postBookForSale("testBook1",
                "978-3-16-148410-2", "author2", "TestGenre", 20,
                null, "testUser1");

        // Make new transaction: testUser2 likes book
        String transactionId = TransactionManager.getTransactionManager().forSaleTransMgr
                .addNewForSaleTransaction("testUser2",
                        saleBookID, "testUser1");

        ForSaleTransaction fst = TransactionManager.getTransactionManager().forSaleTransMgr
                .getItemByID(transactionId);
        assertTrue(fst != null);

        assertEquals(fst.getUsername1(), "testUser2");
        assertEquals(fst.getUsername2(), "testUser1");
        assertEquals(fst.getUser1LikedBookID(), saleBookID);

        // Clean up
        BookManager.getBookManager().deleteItem(saleBookID);
        TransactionManager.getTransactionManager().forSaleTransMgr.deleteItem(transactionId);
        Log.i("TESTINFO", "done");
    }

    @Test
    public void testForSaleTransactionExists() {
        Log.i("TESTINFO", "STARTING for sale");
        // testUser1 posts "testBook1"
        String saleBookID = BookManager.getBookManager().postBookForSale("testBook1",
                "978-3-16-148410-2", "author2", "TestGenre", 20,
                null, "testUser1");

        String transactionId = TransactionManager.getTransactionManager().forSaleTransMgr
                .addNewForSaleTransaction("testUser2", saleBookID, "testUser1");
        boolean forSaleExists = TransactionManager.getTransactionManager().forSaleTransMgr
                .doesItemExist(transactionId);
        assertTrue(forSaleExists);

        // Clean up
        BookManager.getBookManager().deleteItem(saleBookID);
        TransactionManager.getTransactionManager().forSaleTransMgr.deleteItem(transactionId);
        Log.i("TESTINFO", "done");
    }

    @Test
    public void testExchangeTransactionExists() {
        Log.i("TESTINFO", "STARTING if a transaction exists");
        // testUser1 and testUser2 post books for exchange
        String book1ID = BookManager.getBookManager().postBookForExchange("testBook1",
                "978-3-16-148410-2", "author2", "TestGenre", 20, null, "testUser1");
        String book2ID = BookManager.getBookManager().postBookForExchange("testBook2",
                "978-3-16-148410-2", "author2", "TestGenre", 20, null, "testUser2");

        String transactionID = TransactionManager.getTransactionManager().exchangeTransMgr.addNewUnmatchedExchangeTransaction("testUser1", "testBook2");
        boolean exchangeTransactionExists = TransactionManager.getTransactionManager().exchangeTransMgr.doesItemExist(transactionID);

        assertTrue(exchangeTransactionExists);

        // Clean up
        TransactionManager.getTransactionManager().exchangeTransMgr.deleteItem(transactionID);
        BookManager.getBookManager().deleteItem(book1ID);
        BookManager.getBookManager().deleteItem(book2ID);
        Log.i("TESTINFO", "done");
    }

    @Test
    public void testMatchExchangeTransaction() {
        Log.i("TESTINFO", "STARTING a match test");
        // testUser1 and testUser2 post books for exchange
        String book1ID = BookManager.getBookManager().postBookForExchange("testBook1",
                "978-3-16-148410-2", "author2", "TestGenre", 20,
                null, "testUser1");

        String book2ID = BookManager.getBookManager().postBookForExchange("testBook2",
                "978-3-16-148410-2", "author2", "TestGenre", 20,
                null, "testUser2");


        String transactionID = TransactionManager.getTransactionManager().exchangeTransMgr.addNewUnmatchedExchangeTransaction("testUser1", book2ID);
        ExchangeTransaction t = TransactionManager.getTransactionManager().exchangeTransMgr.getItemByID(transactionID);
        assertTrue(t != null);
        t.matchExchangeTransaction("testUser2", book1ID);
        assertTrue(t.isMatched());

        // Clean up
        TransactionManager.getTransactionManager().exchangeTransMgr.deleteItem(transactionID);
        BookManager.getBookManager().deleteItem(book1ID);
        BookManager.getBookManager().deleteItem(book2ID);
        Log.i("TESTINFO", "done");
    }

    @Test
    public void testUserLikeSaleBook() {
        // testUser2 posts "testBook2" for sale
        String saleBookID = BookManager.getBookManager().postBookForSale("testBook2",
                "978-3-16-148410-2", "author2", "TestGenre", 20, null, "testUser2");

        Log.d("TESTEXCHANGE", "TRYING testUserLikeSaleBook()");
        Log.d("TESTEXCHANGE", "forSaleBookID: " + saleBookID);

        // testUser1 likes testBook2
        String saleID = TransactionManager.getTransactionManager().makeUserLikeBook("testUser1", saleBookID);
        ForSaleTransaction fst = TransactionManager.getTransactionManager().forSaleTransMgr.getItemByID(saleID);
        assertTrue(fst != null);

        assertEquals(fst.getUsername1(), "testUser1");
        assertEquals(fst.getUser1LikedBookID(), saleBookID);
        assertEquals(fst.getUsername2(), "testUser2");
        assertTrue(fst.isMatched());

        TransactionManager.getTransactionManager().forSaleTransMgr.deleteItem(saleID);
        BookManager.getBookManager().deleteItem(saleBookID);
    }

    @Test
    public void testUserLikeExchangeBook() {
        // testUser1 and testUser2 post books for exchange
        String book1ID = BookManager.getBookManager().postBookForExchange("testBook1",
                "978-3-16-148410-2", "author2", "TestGenre",
                20, null, "testUser1");
        String book2ID = BookManager.getBookManager().postBookForExchange("testBook2",
                "1234567890", "author2", "Comedy", 100, null,
                "testUser2");

        Log.d("TESTINFO", "Book 1 ID " + book1ID + " book 2 ID " + book2ID);

        // testUser1 likes testBook2 --> unmatched
        String transactionID = TransactionManager.getTransactionManager().makeUserLikeBook("testUser1", book2ID);
        ExchangeTransaction t = TransactionManager.getTransactionManager().exchangeTransMgr.getItemByID(transactionID);
        assertTrue(!t.isMatched());

        // testUser2 likes testBook1 --> matched
        transactionID = TransactionManager.getTransactionManager().makeUserLikeBook("testUser2", book1ID);
//        assertEquals(transactionID, transactionIDAgain);
        t = TransactionManager.getTransactionManager().exchangeTransMgr.getItemByID(transactionID);
        assertTrue(t != null);
        assertTrue(t.isMatched());

        assertEquals(t.getUsername1(), "testUser1");
        assertEquals(t.getUsername2(), "testUser2");
        assertEquals(BookManager.getBookManager().getItemByID(t.getUser1LikedBookID()).getBookName(), "testBook2");
        assertEquals(BookManager.getBookManager().getItemByID(t.getUser2LikedBookID()).getBookName(), "testBook1");

        // Clean up
        TransactionManager.getTransactionManager().exchangeTransMgr.deleteItem(transactionID);
        BookManager.getBookManager().deleteItem(book1ID);
        BookManager.getBookManager().deleteItem(book2ID);
    }


    @Test
    public void testUserDislikeBook() {
        // testUser1 posts "testBook1"
        String saleBookID = BookManager.getBookManager().postBookForSale("testBook1", "978-3-16-148410-2",
                "author2", "TestGenre", 20, null, "testUser1");
        boolean disliked = TransactionManager.getTransactionManager().makeUserDislikeBook("testUser2", saleBookID);
        assertTrue(disliked);

        BookManager.getBookManager().deleteItem(saleBookID);
    }


    @After
    public void deleteTestVariables() {
        UserManager.getUserManager().deleteItem("testUser1");
        UserManager.getUserManager().deleteItem("testUser2");
    }
}

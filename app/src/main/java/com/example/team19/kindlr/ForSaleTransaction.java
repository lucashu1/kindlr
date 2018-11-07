package com.example.team19.kindlr;

import android.util.Log;

import java.io.Serializable;
import java.util.Date;

public class ForSaleTransaction extends Transaction implements Serializable {

    private final static String TAG = "ForSaleTransaction";

    public ForSaleTransaction() {
        wasAccepted = false;
        wasRejected = false;
        username1 = null;
        username2 = null;
        user1LikedBookID = null;
        user2LikedBookID = null;
        timestamp = new Date();
        isMatched = false;
    }

    // FORSALE TRANSACTION CONSTRUCTOR: User 1 likes User 2's forSale book
    public ForSaleTransaction(String transactionID, String userThatLikedBook, String likedForSaleBookID, String bookOwner) {
        super(transactionID);
        this.username1 = userThatLikedBook;
        this.user1LikedBookID = likedForSaleBookID;
        this.username2 = bookOwner;
        this.user2LikedBookID = null; // forSale transaction: only 1 book involved
        this.forSaleTransaction = true;
        this.wasAccepted = false;
        this.isMatched = true; // forSaleTransactions are matched automatically

        // Make book invisible temporarily
        BookManager.getBookManager().getItemByID(user1LikedBookID).makeInvisible();

        // TODO: notify book owner?
    }

    // Accept transaction: remove book(s) from circulation
    @Override
    public void acceptTransaction() {
        this.wasAccepted = true;
        // Remove books from circulation - SIKE, use makeInvisible() instead
//        if (user1LikedBookID != null && user1LikedBookID.length() > 0)
//            BookManager.getBookManager().removeBook(user1LikedBookID);
    }

    // Reject transaction --> make books visible again
    @Override
    public void rejectTransaction() {
        Log.d(TAG, "rejected transaction: " + this.transactionID);
        BookManager bm = BookManager.getBookManager();
        if (user1LikedBookID != null && user1LikedBookID.length() > 0)
            bm.getItemByID(user1LikedBookID).makeVisible();
        wasRejected = true;
    }

    @Override
    public boolean isMatched() {
        return true; // for sale transactions are automatically matched (1-sided!)
    }

}

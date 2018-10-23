package com.example.team19.kindlr;

import java.io.Serializable;
import java.util.Date;

public class ExchangeTransaction extends Transaction implements Serializable {

    public ExchangeTransaction(String transactionID, String username1, String user1LikedBookID) {
        super(transactionID);
        this.username1 = username1;
        this.user1LikedBookID = user1LikedBookID;
        this.forSaleTransaction = false;
        this.isMatched = false;
    }

    public void matchExchangeTransaction(String username2, String user2LikedBookID) {
        // If this transaction is not already full, then fill it
        if (isMatched) {
            System.out.println("WARNING: attemped to match an already matched transaction!");
            return;
        }

        this.username2 = username2;
        this.user2LikedBookID = user2LikedBookID;
        isMatched = true;

        // mark books involved as 'invisible' so they don't show up
        BookManager.getBookManager().getBookByID(user1LikedBookID).makeInvisible();
        BookManager.getBookManager().getBookByID(user2LikedBookID).makeInvisible();

        // TODO:
        // notify both users involved?
    }

    @Override
    public boolean isMatched() {
        return isMatched;
    }

    // Accept transaction: remove book(s) from circulation
    @Override
    public void acceptTransaction() {
        this.wasAccepted = true;
        // Remove books from circulation - sike, don't do this. use makeInvisible() instead
//        if (user1LikedBookID != null && user1LikedBookID.length() > 0)
//            BookManager.getBookManager().removeBook(user1LikedBookID);
//        if (user2LikedBookID != null && user2LikedBookID.length() > 0)
//            BookManager.getBookManager().removeBook(user2LikedBookID);
    }

    // Reject transaction --> make books visible again
    @Override
    public void rejectTransaction() {
        BookManager bm = BookManager.getBookManager();
        if (user1LikedBookID != null && user1LikedBookID.length() > 0)
            bm.getBookByID(user1LikedBookID).makeVisible();
        if (user2LikedBookID != null && user2LikedBookID.length() > 0)
            bm.getBookByID(user2LikedBookID).makeVisible();
        wasRejected = true;
    }

//    // gets other user's (non current user's) book in the transaction
//    public Book getTheirBook()
//    {
//        return BookManager.getBookManager().getBookByID(book2ID);
//    }
//
//    //gets your (current user's) book in the transaction
//    public Book getYourBook()
//    {
//        return BookManager.getBookManager().getBookByID(book1ID);
//    }
}

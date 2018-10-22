package com.example.team19.kindlr;
import java.util.Date;
import java.util.List;

public abstract class Transaction {
    // TRANSACTION FLOW
        // Exchange: User likes book --> Create new unmatched transaction --> Other book's owner likes one of User 1's books --> match! --> accept transaction
        // Sale: User likes a forSale book --> Create a matched forSale transaction immediately

    protected String transactionID;
    protected boolean forSaleTransaction;
    protected boolean wasAccepted;
    protected boolean wasRejected;
    protected String username1; // User 1 in the transaction (owner of the book that User 2 liked)
    protected String username2; // User 2 in the transaction (owner of the book that User 1 liked) - OR the book owner in a forSale transaction
    protected String user1LikedBookID; // The book that User 1 liked (should be owned by User 2)
    protected String user2LikedBookID; // The book that User 2 liked (should be owned by User 1) - OR empty if this is a forSale Transaction
    protected Date timestamp;

    // Default constructor
    public Transaction(String transactionID) {
        this.transactionID = transactionID;
        this.timestamp = new Date();
        this.wasAccepted = false;
        this.wasRejected = false;
    }

//    //gets the other user in the transaction
//    public User getOtherUser()
//    {
//        return UserManager.getUserManager().getUserByUsername(username2);
//    }


    public String getUsername1() {
        return username1;
    }

    public String getUsername2() {
        return username2;
    }

    public String getUser1LikedBookID() {
        return user1LikedBookID;
    }

    public String getUser2LikedBookID() {
        return user2LikedBookID;
    }

    //checks if transaction is a sale or an exchange
    public boolean isSale()
    {
        return forSaleTransaction;
    }

    //if the transaction has been completed, returns true
    public boolean wasAccepted()
    {
        return wasAccepted;
    }

    public boolean wasRejected() {
        return wasRejected;
    }

    //returns the date that the transaction happened
    public Date getDate()
    {
        return timestamp;
    }

    public abstract boolean isMatched();
    public abstract void acceptTransaction();
    public abstract void rejectTransaction();


}

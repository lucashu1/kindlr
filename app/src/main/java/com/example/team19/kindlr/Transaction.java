package com.example.team19.kindlr;
import java.util.Date;
import java.util.List;

public class Transaction {
    private String transactionID;
    private UserManager um;
    private BookManager bm;
    private boolean forSaleTransaction;
    private boolean wasAccepted;
    private String username1;
    private String username2;
    private String book1ID;
    private String book2ID;
    private Date timestamp;

    public Transaction(String transactionID, String username1, String username2, String book1ID, String book2ID, boolean forSaleTransaction, boolean wasAccepted, Date timestamp)
    {
        this.transactionID = transactionID;
        this.username1 = username1;
        this.username2 = username2;
        this.book1ID = book1ID;
        this.book2ID = book2ID;
        this.forSaleTransaction = forSaleTransaction;
        this.wasAccepted = wasAccepted;
        this.timestamp = timestamp;
    }

    //gets the other user in the transaction
    public User getOtherUser()
    {
        return um.getUserByUsername(username2);
    }

    //gets the current user in the transaction
    public User getCurUser()
    {
        return um.getUserByUsername(username1);
    }

    //gets the other user's book
    public Book getTheirBook()
    {
        return bm.getBookByID(book2ID);
    }

    //gets your book in the transaction
    public Book getYourBook()
    {
        return bm.getBookByID(book1ID);
    }

    //checks if transaction is a sale or an exchange
    public boolean isSale()
    {
        return forSaleTransaction;
    }

    //if the transaction has been completed, returns true
    public boolean isCompleted()
    {
        return wasAccepted;
    }

    //returns the date that the transaction happened
    public Date getDate()
    {
        return timestamp;
    }

}

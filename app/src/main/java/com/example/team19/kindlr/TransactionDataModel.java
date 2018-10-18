package com.example.team19.kindlr;
import java.util.Date;

public class TransactionDataModel {
    private String username1;
    private String username2;
    private String book1ID;
    private String book2ID;
    private boolean forSaleTransaction;
    private boolean wasAccepted;
    private Date timestamp;

    public TransactionDataModel(String username1, String username2, String book1ID, String book2ID, boolean forSaleTransaction, boolean wasAccepted, Date timestamp)
    {
        this.username1 = username1;
        this.username2 = username1;
        this.book1ID = book1ID;
        this.book2ID = book2ID;
        this.forSaleTransaction = forSaleTransaction;
        this.wasAccepted = wasAccepted;
        this.timestamp = timestamp;
    }

    //returns the name of the current user
    public String getUsername1()
    {
        return username1;
    }

    //returns the name of the other user
    public String getUsername2()
    {
        return username2;
    }

    //returns the book id of the current user
    public String getBookID1()
    {
        return book1ID;
    }

    //returns the book id of the other user
    public String getBookID2()
    {
        return book2ID;
    }

    //returns if the transaction is for sale or not
    public boolean isForSaleTransaction()
    {
        return forSaleTransaction;
    }

    public boolean wasAccepted()
    {
        return wasAccepted;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }
}

package com.example.team19.kindlr;
import java.util.Date;
import java.util.List;

public class Transaction {
    private TransactionManager transactionManager;
    private UserManager um;
    private BookManager bm;
    private boolean forSaleTransaction;
    private boolean wasAccepted;
    private String username1;
    private String username2;
    private int book1ID;
    private int book2ID;
    private Date timestamp;

    public Transaction(String username1, String username2, int book1ID, int book2ID, boolean forSaleTransaction, boolean wasAccepted, Date timestamp)
    {
        transactionManager = TransactionManager.getUserManager();
        um = UserManager.getUserManager();
        bm = BookManager.getBookManager();
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

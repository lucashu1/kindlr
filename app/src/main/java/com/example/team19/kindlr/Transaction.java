package com.example.team19.kindlr;
import java.util.Date;
import java.util.List;

public class Transaction {
    private String transactionID;
    private boolean forSaleTransaction;
    private boolean wasAccepted;
    private String username1;
    private String username2;
    private String user1LikedBookID;
    private String user2LikedBookID;
    private Date timestamp;

    // Potential/partial transaction constructor: e.g. User 1 likes User 2's book
        // May potentially be completed in future by User 2, if User 2 likes one of User 1's books
    public Transaction(String transactionID, String username1, String user1LikedBookID, boolean forSaleTransaction) {
        this.transactionID = transactionID;
        this.username1 = username1;
        this.user1LikedBookID = user1LikedBookID;
        this.forSaleTransaction = forSaleTransaction;
        this.timestamp = new Date();
        this.wasAccepted = false;

        // If the liked book is forSale, then this is a forSaleTransaction
        this.forSaleTransaction = BookManager.getBookManager().getBookByID(user1LikedBookID).getForSale();
    }

    // Full transaction constructor - THIS SHOULD NEVER HAVE TO BE CALLED
    // Call the partial constructor first, and then fillPotentialTransaction()
//    public Transaction(String transactionID, String username1, String username2, String user1LikedBookID, String user2LikedBookID, boolean forSaleTransaction, boolean wasAccepted)
//    {
//        this.transactionID = transactionID;
//        this.username1 = username1;
//        this.username2 = username2;
//        this.user1LikedBookID = user1LikedBookID;
//        this.user2LikedBookID = user2LikedBookID;
//        this.forSaleTransaction = forSaleTransaction;
//        this.wasAccepted = wasAccepted;
//        this.timestamp = new Date();
//    }

//    //gets the other user in the transaction
//    public User getOtherUser()
//    {
//        return UserManager.getUserManager().getUserByUsername(username2);
//    }

    // Return true if this transaction is filled out
        // I.e.: is forSale, and a user has liked the book
        // Or: is forExchange, and 2 users have liked each other's books (book1ID and book2ID)
    public boolean isFullTransaction() {
        if (forSaleTransaction && (username1 != null) && (user1LikedBookID != null)) {
            return true;
        }
        else if (!forSaleTransaction && (username1 != null) && (username2 != null) && (user1LikedBookID != null) && (user2LikedBookID != null)) {
            return true;
        }
        else {
            return false;
        }
    }

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

//    //gets the other user's book
//    public Book getTheirBook()
//    {
//        return BookManager.getBookManager().getBookByID(book2ID);
//    }
//
//    //gets your book in the transaction
//    public Book getYourBook()
//    {
//        return BookManager.getBookManager().getBookByID(book1ID);
//    }

    public void fillPotentialTransaction(String username2, String user2LikedBookID) {
        // If this transaction is not already full, then fill it
        if (!this.isFullTransaction())
            this.username2 = username2;
            this.user2LikedBookID = user2LikedBookID;
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

    // Complete transaction: remove book(s) from circulation
    public void completeTransaction() {
        this.wasAccepted = true;
        // Remove books from circulation
        if (user1LikedBookID != null && user1LikedBookID.length() > 0)
            BookManager.getBookManager().removeBook(user1LikedBookID);
        if (user2LikedBookID != null && user2LikedBookID.length() > 0)
            BookManager.getBookManager().removeBook(user2LikedBookID);
    }

}

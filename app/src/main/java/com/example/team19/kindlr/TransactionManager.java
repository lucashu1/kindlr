package com.example.team19.kindlr;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;





public class TransactionManager {
    public ExchangeTransMgr exchangeTransMgr = new ExchangeTransMgr();
    public ForSaleTransMgr forSaleTransMgr = new ForSaleTransMgr();

    private final static String TAG = "TransactionManager";

    private static TransactionManager transactionManagerSingleton;
    public synchronized static TransactionManager getTransactionManager() {
        if (transactionManagerSingleton == null)
            transactionManagerSingleton = new TransactionManager();
        return transactionManagerSingleton;
    }

    public void initialize() {
        initialize(false);
    }

    public void refresh() {
        initialize(false);
    }

    public void initialize(boolean shouldWait) {
        this.forSaleTransMgr.initialize(shouldWait);
        this.exchangeTransMgr.initialize(shouldWait);
    }

    public void saveAllToFirebase() {
        this.forSaleTransMgr.saveToFirebase();
        this.exchangeTransMgr.saveToFirebase();
    }

    // Make user like book, and return the String of the resulting transactionID
    public String makeUserLikeBook(String username, String bookID) {
        boolean success = UserManager.getUserManager().makeUserLikeBook(username, bookID);
        if (!success) {
            Log.d("WARN","Unsuccessful makeUserLIkeBook! username: " + username + "; bookID: " + bookID);
            return null;
        }
        return processLikedBook(username, bookID);
    }

    // Make user dislike book, return true if successful
    public boolean makeUserDislikeBook(String username, String bookID) {
        boolean success = UserManager.getUserManager().makeUserDislikeBook(username, bookID);
        if (!success)
            return false;
        return true;
    }

    public void clearAllTransactions() {
        exchangeTransMgr.removeAllItems();
        forSaleTransMgr.removeAllItems();
    }

    // Process liked book, return corresponding transactionID
    public String processLikedBook(String username, String bookID) {
        // If this is a forSale book and has not been completed, then create a new transaction
        // Otherwise (book is for exchange -- not for sale), Look at all other existing partial transactions:
        // If this could complete a transaction, then make it do so, and notify both participants
        // Otherwise, call addNewUnMatchedTransaction so future 'likes' can potentially make a 'match'

        String transactionID = null;

        // Case 1: Liked book is for sale
        if (BookManager.getBookManager().getItemByID(bookID).getForSale()) {
            transactionID = forSaleTransMgr.addNewForSaleTransaction(username, bookID, BookManager.getBookManager().getItemByID(bookID).getOwner());

            String ownerName = BookManager.getBookManager().getBookOwner(bookID);
            User likeUsr = UserManager.getUserManager().getUserByUsername(username);
            User owner = UserManager.getUserManager().getUserByUsername(ownerName);
            EmailNotifier toOwner = new EmailNotifier(owner.getEmail());
            //liker is the one that works
            EmailNotifier toLiker = new EmailNotifier(likeUsr.getEmail());
            Log.d("OWNEREMAIL", owner.getEmail());
            Log.d("LIKEUSREMAIL", likeUsr.getEmail());
            Book book = BookManager.getBookManager().getItemByID(bookID);
            String subject = "Your book " + book.getBookName() + " has entered a for sale transaction";
            String ownerNotif = username + " has liked your book that is for sale.";
            String likerNotif = "You have liked " + ownerName + "'s book that is for sale.";
            toOwner.sendFromGMail(subject, ownerNotif);
            toLiker.sendFromGMail(subject, likerNotif);

            this.forSaleTransMgr.saveToFirebase();
        }
        // Case 2: Liked book is for exchange
        else {
            boolean foundMatch = false;
            // See if this 'like' could complete a 'match' with any existing transactions
            for (Map.Entry<String, ExchangeTransaction> entry : exchangeTransMgr.getItemsMap().entrySet()) {

                // If this current transaction is a forSale transaction, or has already been matched, then skip
                if (entry.getValue().isMatched()) {
                    continue;
                }

                // Get info of other (unmatched, forExchange) transaction
                ExchangeTransaction existingUnmatchedTransaction = entry.getValue();
                String otherUser = existingUnmatchedTransaction.getUsername1();
                String otherLikedBook = existingUnmatchedTransaction.getUser1LikedBookID();

                if (!BookManager.getBookManager().doesItemExist(otherLikedBook)) {
                    Log.d(TAG, "Found a non-existent book while iterating through unmatched transactions: " + otherLikedBook);
                    continue;
                }

                Log.d(TAG,"Iterating over unmatched transactions. otherLikedBook: " + otherLikedBook);
                Log.d(TAG, "otherLikedBook name: " + BookManager.getBookManager().getItemByID(otherLikedBook).getBookName());
                String otherLikedBookOwner = BookManager.getBookManager().getBookOwner(otherLikedBook);

                // Other book's owner has liked a book that is owned by current user --> match!
                if (otherLikedBookOwner.equals(username) && !username.equals(otherUser)) {
                    existingUnmatchedTransaction.matchExchangeTransaction(username, bookID);
                    transactionID = existingUnmatchedTransaction.getTransactionID();
                    foundMatch = true;
                    break;
                }
            }

            // If this can't complete any existing partial transaction, call addNewPotentialTransaction
            if (!foundMatch) {
                transactionID = this.exchangeTransMgr.addNewUnmatchedExchangeTransaction(username, bookID);
            }

            this.exchangeTransMgr.saveToFirebase();
        }


        return transactionID;
    }

    public ArrayList<Transaction> getAllMatchedTransactionsForUser(String userName) {
        ArrayList<Transaction> exchangeTransactions = this.exchangeTransMgr.getAllMatchedTransactionsForUser(userName);
        ArrayList<Transaction> forSaleTransactions = this.forSaleTransMgr.getAllMatchedTransactionsForUser(userName);
        exchangeTransactions.addAll(forSaleTransactions);
        return exchangeTransactions;
    }
}

package com.example.team19.kindlr;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TransactionManager {

    private Map<String, Transaction> transactionsMap;
    FirebaseDatabase database;
    DatabaseReference transactionsRef;

    private static TransactionManager transactionManagerSingleton;
    public static TransactionManager getTransactionManager() {
        if (transactionManagerSingleton == null)
            transactionManagerSingleton = new TransactionManager();
        return transactionManagerSingleton;
    }

    public TransactionManager()
    {
        database = FirebaseDatabase.getInstance();
        transactionsRef = database.getReference("transactions");
        transactionsMap = new HashMap<String, Transaction>();
//        refresh();

        // On data change, re-read usersMap from the database
        transactionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d("TESTINFO", "Transactions being updated");
                transactionsMap = new HashMap<String, Transaction>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Transaction t = snapshot.getValue(Transaction.class);
                    if (t.isSale())
                        transactionsMap.put(snapshot.getKey(), (ForSaleTransaction) t);
                    else
                        transactionsMap.put(snapshot.getKey(), (ExchangeTransaction) t);
                }
                Log.d("TESTINFO", "Refreshed transactionsMap to be " + transactionsMap.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("WARN", "Failed to read value.", error.toException());
            }
        });
    }

    // Save usersMap to Firebase (write to DB)
    public void saveToFirebase() {
        transactionsRef.setValue(transactionsMap);
    }

    public boolean makeUserLikeBook(String username, String bookID) {
        boolean success = UserManager.getUserManager().makeUserLikeBook(username, bookID);
        if (!success) {
            return false;
        }
        processLikedBook(username, bookID);
        return true;
    }

    public boolean makeUserDislikeBook(String username, String bookID) {
        boolean success = UserManager.getUserManager().makeUserDislikeBook(username, bookID);
        if (!success)
            return false;
        return true;
    }

    public void processLikedBook(String username, String bookID) {
        // If this is a forSale book and has not been completed, then create a new transaction
        // Otherwise (book is for exchange -- not for sale), Look at all other existing partial transactions:
            // If this could complete a transaction, then make it do so, and notify both participants
            // Otherwise, call addNewUnMatchedTransaction so future 'likes' can potentially make a 'match'

        // Case 1: Liked book is for sale
        if (BookManager.getBookManager().getBookByID(bookID).getForSale()) {
            addNewForSaleTransaction(BookManager.getBookManager().getBookByID(bookID).getOwner(), bookID,username);
        }

        // Case 2: Liked book is for exchange
        else {
            // See if this 'like' could complete a 'match' with any existing transactions
            for (Map.Entry<String, Transaction> entry : transactionsMap.entrySet()) {

                // If this current transaction is a forSale transaction, or has already been matched, then skip
                if (entry.getValue().forSaleTransaction || ((ExchangeTransaction) entry.getValue()).isMatched()) {
                    continue;
                }

                // Get info of other (unmatched, forExchange) transaction
                ExchangeTransaction existingUnmatchedTransaction = (ExchangeTransaction) entry.getValue();
                String otherUser = existingUnmatchedTransaction.getUsername1();
                String otherLikedBook = existingUnmatchedTransaction.getUser1LikedBookID();
                String otherLikedBookOwner = BookManager.getBookManager().getBookOwner(otherLikedBook);

                // Other book's owner has liked a book that is owned by current user --> match!
                if (otherLikedBookOwner.equals(username) && !username.equals(otherUser)) {
                    existingUnmatchedTransaction.matchExchangeTransaction(username, bookID);
                    return;
                }
            }

            // If this can't complete any existing partial transaction, call addNewPotentialTransaction
            addNewUnmatchedExchangedTransaction(username, bookID);
        }

        saveToFirebase();
    }

    // Add new partial exchange transaction using given fields (mo match found yet)
    public void addNewUnmatchedExchangedTransaction(String username1, String user1LikedBookID) {
        String transactionID = transactionsRef.push().getKey();
        Transaction t = new ExchangeTransaction(transactionID, username1, user1LikedBookID); // create new exchange transaction
        transactionsMap.put(transactionID, t);
        Log.d("INFO", "Created new unmatched exchange transaction");
        saveToFirebase();
    }

    // Add new forSale transaction using given fields
    public void addNewForSaleTransaction(String userThatLikedBook, String forSaleBookID, String forSaleBookOwner) {
        String transactionID = transactionsRef.push().getKey();
        Transaction t = new ForSaleTransaction(transactionID, userThatLikedBook, forSaleBookID, forSaleBookOwner); // create new forSale transaction
        transactionsMap.put(transactionID, t);
        Log.d("INFO", "Created new forSale transaction");
        saveToFirebase();
    }

    //deletes a certain specified transaction. Returns true if it deletes and exists, false if
    //it does not exist
    public boolean deleteTransaction(Transaction t)
    {
        String transactionID = null; // ID of transaction to remove
        for(Map.Entry<String, Transaction> entry : transactionsMap.entrySet())
        {
            Transaction currTransaction = entry.getValue();
            if(currTransaction == t)
            {
                transactionID = entry.getKey();
                break;
            }
        }
        if (transactionID != null) {
            transactionsMap.remove(transactionID);
            saveToFirebase();
            return true;
        }

        return false;
    }

    //gets a list of all transactions of books
    public HashMap<String, Transaction> getTransactionsMap()
    {
        return (HashMap<String, Transaction>) transactionsMap;
    }

//    public void refresh()
//    {
//
//    }

    //Gets all the transactions, sales or not, for a specified user
    public ArrayList<Transaction> getAllMatchedTransactionsForUser(String userName)
    {
        ArrayList<Transaction> result = new ArrayList<Transaction>();
        for(Map.Entry<String, Transaction> entry : transactionsMap.entrySet())
        {
            Transaction transaction = entry.getValue();
            if((transaction.getUsername1().equals(userName) || transaction.getUsername2().equals(userName)) && transaction.isMatched())
            {
                result.add(transaction);

            }
        }
        return result;
    }
}

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
    public static TransactionManager getUserManager() {
        if (transactionManagerSingleton == null)
            transactionManagerSingleton = new TransactionManager();
        return transactionManagerSingleton;
    }

    public TransactionManager()
    {
        database = FirebaseDatabase.getInstance();
        transactionsRef = database.getReference("transactions");
        transactionsMap = new HashMap<String, Transaction>();
        refresh();

        // On data change, read read usersMap from the database
        transactionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                transactionsMap = (HashMap<String, Transaction>) dataSnapshot.getValue(HashMap.class);
                Log.d("INFO", "Refreshed usersMap");
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

    // TODO: Add logic for making users like books, finding matches
    public boolean makeUserLikeBook(String username, String bookID) {
        boolean success = UserManager.getUserManager().makeUserLikeBook(username, bookID);
        if (!success)
            return false;
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
        // TODO: process a "liked book" action, from makeUserLikeBook
        // If this is a forSale book and has not been completed, then notify
        // Otherwise (not a forSale book), Look at all other existing partial transactions:
            // If this could complete a transaction, then make it do so, and notify both participants
            // Otherwise, call addNewPotentialTransaction

        if (BookManager.getBookManager().getBookByID(bookID).getForSale()) {
            // TODO: notify book owner?
        }

        for (Map.Entry<String, Transaction> entry : transactionsMap.entrySet()) {
            Transaction t = entry.getValue();
            if (!t.isFullTransaction() && !t.isCompleted()) {
                String otherUser = t.getUsername1();
                String otherLikedBook = t.getUser1LikedBookID();
                String likedBookOwner = BookManager.getBookManager().getBookOwner(otherLikedBook);
                ArrayList<String> otherUsersLikedBooks = UserManager.getUserManager().getUserByUsername(otherUser).getLikedBooks();

                // If this completes an existing transaction, then notify owners
                    // I.e. Other use has liked one if this current user (username)'s books
                    // And the book that 'username' liked (i.e. bookID) belongs to that other user
                // TODO: check for match, and process match
            }
        }

        // TODO: if this can't complete any existing partial transaction, call addNewPotentialTransaction
    }

    // Add new transaction using given fields
    public void addNewPotentialTransaction(String username1, String book2ID, boolean forSaleTransaction) {
        String transactionID = transactionsRef.push().getKey();
        Transaction t = new Transaction(transactionID, username1, book2ID, forSaleTransaction);
        transactionsMap.put(transactionID, t);
        saveToFirebase();
    }

//    // Add new transaction using given fields
    // THIS SHOULD NEVER HAVE TO BE CALLED
    // use makeUserLikeBook(), which will call processLikedBook(), which will either
    // call addNewPotentialTransaction, or fill the existing transaction.
//    public void addNewTransaction(String username1, String username2,
//                                     String book1ID, String book2ID,
//                                     boolean forSaleTransaction, boolean wasAccepted) {
//        String transactionID = transactionsRef.push().getKey();
//        Transaction t = new Transaction(transactionID, username1, username2, book1ID, book2ID, forSaleTransaction, wasAccepted);
//        transactionsMap.put(transactionID, t);
//        saveToFirebase();
//    }

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
    public HashMap<String, Transaction> getAllTransactions()
    {
        return (HashMap<String, Transaction>) transactionsMap;
    }

    //TODO: refreshes the database
    public void refresh()
    {

    }

    //Gets all the transactions, sales or not, for a specified user
    public ArrayList<Transaction> getAllTransactionsForUser(String userName)
    {
        ArrayList<Transaction> result = new ArrayList<Transaction>();
        for(Map.Entry<String, Transaction> entry : transactionsMap.entrySet())
        {
            Transaction transaction = entry.getValue();
            if(transaction.getUsername1().equals(userName) || transaction.getUsername2().equals(userName))
            {
                result.add(transaction);
            }
        }
        return result;
    }
}

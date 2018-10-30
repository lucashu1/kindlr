package com.example.team19.kindlr;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TransactionManager {

    private Map<String, ExchangeTransaction> exchangeTransactionsMap;
    private Map<String, ForSaleTransaction> forSaleTransactionsMap;
    private FirebaseDatabase database;
    private DatabaseReference exchangeTransactionsRef;
    private DatabaseReference forSaleTransactionsRef;

    private static TransactionManager transactionManagerSingleton;
    public static TransactionManager getTransactionManager() {
        if (transactionManagerSingleton == null)
            transactionManagerSingleton = new TransactionManager();
        return transactionManagerSingleton;
    }

    public TransactionManager() {
        exchangeTransactionsMap = new HashMap<String, ExchangeTransaction>();
        forSaleTransactionsMap = new HashMap<String, ForSaleTransaction>();
    }

    public void initialize()
    {
        database = FirebaseDatabase.getInstance();
        exchangeTransactionsRef = database.getReference("exchangeTransactions");
        forSaleTransactionsRef = database.getReference("forSaleTransactions");

//        exchangeTransactionsMap = new HashMap<String, ExchangeTransaction>();
//        forSaleTransactionsMap = new HashMap<String, ForSaleTransaction>();

        // On data change, re-read usersMap from the database
        exchangeTransactionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d("TESTINFO", "exchangeTransactions being updated");
                exchangeTransactionsMap = new HashMap<String, ExchangeTransaction>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ExchangeTransaction t = snapshot.getValue(ExchangeTransaction.class);
                    exchangeTransactionsMap.put(snapshot.getKey(), t);
                }
                Log.d("TESTINFO", "Refreshed exchangeTransactionsMap to be " + exchangeTransactionsMap.toString());
                Log.d("TESTINFO", "Length of exchangeTransactionsMap: " + exchangeTransactionsMap.size());
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("WARN", "Failed to read value.", error.toException());
            }
        });

        // On data change, re-read usersMap from the database
        forSaleTransactionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d("TESTINFO", "forSaleTransactions being updated");
                forSaleTransactionsMap = new HashMap<String, ForSaleTransaction>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ForSaleTransaction t = snapshot.getValue(ForSaleTransaction.class);
                    forSaleTransactionsMap.put(snapshot.getKey(), t);
                }
                Log.d("TESTINFO", "Refreshed forSaleTransactionsMap to be " + forSaleTransactionsMap.toString());
                Log.d("TESTINFO", "Length of forSaleTransactionsMap: " + forSaleTransactionsMap.size());
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
        exchangeTransactionsRef.setValue(exchangeTransactionsMap);
        forSaleTransactionsRef.setValue(forSaleTransactionsMap);
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

    // Process liked book, return corresponding transactionID
    public String processLikedBook(String username, String bookID) {
        // If this is a forSale book and has not been completed, then create a new transaction
        // Otherwise (book is for exchange -- not for sale), Look at all other existing partial transactions:
            // If this could complete a transaction, then make it do so, and notify both participants
            // Otherwise, call addNewUnMatchedTransaction so future 'likes' can potentially make a 'match'

        String transactionID = null;

        // Case 1: Liked book is for sale
        if (BookManager.getBookManager().getBookByID(bookID).getForSale()) {
            transactionID = addNewForSaleTransaction(username, bookID, BookManager.getBookManager().getBookByID(bookID).getOwner());
        }

        // Case 2: Liked book is for exchange
        else {
            boolean foundMatch = false;
            // See if this 'like' could complete a 'match' with any existing transactions
            for (Map.Entry<String, ExchangeTransaction> entry : exchangeTransactionsMap.entrySet()) {

                // If this current transaction is a forSale transaction, or has already been matched, then skip
                if (entry.getValue().isMatched()) {
                    continue;
                }

                // Get info of other (unmatched, forExchange) transaction
                ExchangeTransaction existingUnmatchedTransaction = entry.getValue();
                String otherUser = existingUnmatchedTransaction.getUsername1();
                String otherLikedBook = existingUnmatchedTransaction.getUser1LikedBookID();
                Log.d("INFO","Iterating over unmatched transactions. otherLikedBook: " + otherLikedBook);
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
                transactionID = addNewUnmatchedExchangeTransaction(username, bookID);
            }
        }

        saveToFirebase();
        return transactionID;
    }

    public boolean forSaleTransactionExists(String transactionID) {
        return forSaleTransactionsMap.containsKey(transactionID);
    }

    public boolean exchangeTransactionExists(String transactionID) {
        return exchangeTransactionsMap.containsKey(transactionID);
    }

    public ForSaleTransaction getForSaleTransactionByID(String transactionID) {
        Log.i("TESTINFO", "For sale transaction map when getting by sale transaction id " +
                forSaleTransactionsMap.toString());

        Log.i("TESTINFO", "When getting tid of " + transactionID);

        if (!forSaleTransactionExists(transactionID)) {
            Log.i("TESTINFO", "TID does not exist");
            return null;
        }

        return forSaleTransactionsMap.get(transactionID);
    }

    public ExchangeTransaction getExchangeTransactionByID(String transactionID) {
        if (!exchangeTransactionExists(transactionID))
            return null;

        return exchangeTransactionsMap.get(transactionID);
    }

    public void deleteForSaleTransaction(String transactionID) {
        if (!forSaleTransactionExists(transactionID))
            return;

        forSaleTransactionsMap.remove(transactionID);
        DatabaseReference transactionRef = forSaleTransactionsRef.child(transactionID);
        transactionRef.removeValue();
    }

    public void deleteExchangeTransaction(String transactionID) {
        if (!exchangeTransactionExists(transactionID))
            return;

        exchangeTransactionsMap.remove(transactionID);
        DatabaseReference transactionRef = exchangeTransactionsRef.child(transactionID);
        transactionRef.removeValue();
    }

    // Add new partial exchange transaction using given fields (mo match found yet)
    public String addNewUnmatchedExchangeTransaction(String username1, String user1LikedBookID) {
        String transactionID = exchangeTransactionsRef.push().getKey();
        ExchangeTransaction t = new ExchangeTransaction(transactionID, username1, user1LikedBookID); // create new exchange transaction
        exchangeTransactionsMap.put(transactionID, t);
        Log.d("INFO", "Created new unmatched exchange transaction");
        saveToFirebase();
        return transactionID;
    }

    // Add new forSale transaction using given fields
    public String addNewForSaleTransaction(String userThatLikedBook, String forSaleBookID, String forSaleBookOwner) {
        String transactionID = forSaleTransactionsRef.push().getKey();
        ForSaleTransaction t = new ForSaleTransaction(transactionID, userThatLikedBook, forSaleBookID, forSaleBookOwner); // create new forSale transaction
        forSaleTransactionsMap.put(transactionID, t);
        Log.d("TESTINFO", "Created new forSale transaction with ID " + transactionID);
        saveToFirebase();
        return transactionID;
    }

    //deletes a certain specified transaction. Returns true if it deletes and exists, false if
    //it does not exist
//    public boolean deleteTransaction(Transaction t)
//    {
//        String transactionID = null; // ID of transaction to remove
//        for(Map.Entry<String, Transaction> entry : transactionsMap.entrySet())
//        {
//            Transaction currTransaction = entry.getValue();
//            if(currTransaction == t)
//            {
//                transactionID = entry.getKey();
//                break;
//            }
//        }
//        if (transactionID != null) {
//            transactionsMap.remove(transactionID);
//            saveToFirebase();
//            return true;
//        }
//
//        return false;
//    }

    //gets a list of all exchangeTransactions of books
    public HashMap<String, ExchangeTransaction> getExchangeTransactionsMap()
    {
        return (HashMap<String, ExchangeTransaction>) exchangeTransactionsMap;
    }

//    public void refresh()
//    {
//
//    }

    //Gets all the transactions, sales or not, for a specified user
    public ArrayList<Transaction> getAllMatchedTransactionsForUser(String userName)
    {
        ArrayList<Transaction> result = new ArrayList<Transaction>();

        // exchange transactions
        for(Map.Entry<String, ExchangeTransaction> entry : exchangeTransactionsMap.entrySet())
        {
            Transaction transaction = entry.getValue();
            if((transaction.getUsername1().equals(userName) || transaction.getUsername2().equals(userName)) && transaction.isMatched())
            {
                result.add(transaction);

            }
        }

        // sale transactions
        for(Map.Entry<String, ForSaleTransaction> entry : forSaleTransactionsMap.entrySet())
        {
            Transaction transaction = entry.getValue();
            if((transaction.getUsername1().equals(userName) || transaction.getUsername2().equals(userName)) && transaction.isMatched())
            {
                result.add(transaction);

            }
        }
        return result;
    }

    // Clear all transactions from Firebase. Can't undo!
    public void clearAllTransactions() {
//        exchangeTransactionsMap = new HashMap<String, ExchangeTransaction>();
//        forSaleTransactionsMap = new HashMap<String, ForSaleTransaction>();
//        exchangeTransactionsRef.setValue(exchangeTransactionsMap);
//        forSaleTransactionsRef.setValue(forSaleTransactionsMap);
        exchangeTransactionsRef.removeValue();
        forSaleTransactionsRef.removeValue();
    }
}

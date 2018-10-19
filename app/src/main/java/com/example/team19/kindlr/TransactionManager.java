package com.example.team19.kindlr;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

    // Add new transaction using given fields
    public void addNewTransaction(String username1, String username2,
                                     String book1ID, String book2ID,
                                     boolean forSaleTransaction, boolean wasAccepted, Date timestamp) {
        String transactionID = transactionsRef.push().getKey();
        Transaction t = new Transaction(transactionID, username1, username2, book1ID, book2ID, forSaleTransaction, wasAccepted, timestamp);
        transactionsMap.put(transactionID, t);
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
            if(transaction.getCurUser().equals(userName) || transaction.getOtherUser().equals(userName))
            {
                result.add(transaction);
            }
        }
        return result;
    }
}

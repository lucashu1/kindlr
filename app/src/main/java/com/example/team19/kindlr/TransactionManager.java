package com.example.team19.kindlr;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TransactionManager {

    private ArrayList<Transaction> transactions;
    FirebaseDatabase database;
    DatabaseReference ref;
    DatabaseReference usersRef;

    private static TransactionManager transactionManagerSingleton;
    public static TransactionManager getUserManager() {
        if (transactionManagerSingleton == null)
            transactionManagerSingleton = new TransactionManager();
        return transactionManagerSingleton;
    }

    public TransactionManager()
    {
        transactions = new ArrayList<Transaction>();
    }

    //deletes a certain specified transaction. Returns true if it deletes and exists, false if
    //it does not exist
    public boolean deleteTransaction(Transaction t)
    {
        for(int i = 0; i < transactions.size(); i++)
        {
            if(transactions.get(i) == t)
            {
                transactions.remove(i);
                return true;
            }
        }
        return false;
    }

    //adds a transaction to the list of transactions
    public void acceptTransaction(Transaction t)
    {
        transactions.add(t);
    }

    //gets a list of all transactions of books
    public List<Transaction> getAllTransactions()
    {
        return transactions;
    }

    //TODO: refreshes the database
    public void refresh()
    {

    }

    //Gets all the transactions, sales or not, for a specified user
    public List<Transaction> getAllTransactionsForUser(String userName)
    {
        List<Transaction> result = new ArrayList<Transaction>();
        for(int i = 0; i < transactions.size(); i++)
        {
            if(transactions.get(i).getCurUser().equals(userName) || transactions.get(i).getOtherUser().equals(userName))
            {
                result.add(transactions.get(i));
            }
        }
        return result;
    }
}

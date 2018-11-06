package com.example.team19.kindlr;

import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

public class ExchangeTransMgr extends FirebaseAccessor<ExchangeTransaction> {

    public ExchangeTransMgr() {
        super(ExchangeTransaction.class);
    }

    public String getFirebaseRefName() {
        return "exchangeTransactions";
    }

    // Add new partial exchange transaction using given fields (mo match found yet)
    public String addNewUnmatchedExchangeTransaction(String username1, String user1LikedBookID) {
        String transactionID = this.getInsertKey();
        ExchangeTransaction t = new ExchangeTransaction(transactionID, username1, user1LikedBookID); // create new exchange transaction
        this.getItemsMap().put(transactionID, t);
        Log.d("INFO", "Created new unmatched exchange transaction");
        saveToFirebase();
        return transactionID;
    }

    //Gets all the transactions, sales or not, for a specified user
    public ArrayList<Transaction> getAllMatchedTransactionsForUser(String userName)
    {
        ArrayList<Transaction> result = new ArrayList<Transaction>();

        // exchange transactions
        for(Map.Entry<String, ExchangeTransaction> entry : this.getItemsMap().entrySet())
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


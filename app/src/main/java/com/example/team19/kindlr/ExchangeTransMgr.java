package com.example.team19.kindlr;

import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

public class ExchangeTransMgr extends FirestoreAccessor<ExchangeTransaction> {

    private static final String TAG = "ExchangeTransMgr";

    public ExchangeTransMgr() {
        super(ExchangeTransaction.class);
    }

    public String getFirestoreCollectionName() {
        return "exchangeTransactions";
    }

    // Add new partial exchange transaction using given fields (mo match found yet)
    public String addNewUnmatchedExchangeTransaction(String username1, String user1LikedBookID) {
        String transactionID = this.getInsertKey();
        ExchangeTransaction t = new ExchangeTransaction(transactionID, username1, user1LikedBookID); // create new exchange transaction
        this.putItem(transactionID, t);
        Log.d(TAG, "Created new unmatched exchange transaction: " + transactionID);
        return transactionID;
    }

    //Gets all the transactions, sales or not, for a specified user
    public ArrayList<Transaction> getAllMatchedTransactionsForUser(String userName)
    {
        ArrayList<Transaction> result = new ArrayList<Transaction>();

        // exchange transactions
        for(Map.Entry<String, ExchangeTransaction> entry : this.getItemsMap().entrySet())
        {
            ExchangeTransaction transaction = entry.getValue();
            if (transaction.getUsername1() == null || transaction.getUsername2() == null)
                continue;

            if((transaction.getUsername1().equals(userName) || transaction.getUsername2().equals(userName)) && transaction.isMatched())
            {
                result.add(transaction);

            }
        }
        Log.d(TAG, "Getting matched transactions for user: " + userName + "; found " + result.size() + " matched transactions.");

        return result;
    }
}


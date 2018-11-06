package com.example.team19.kindlr;

import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

public class ForSaleTransMgr extends FirebaseAccessor<ForSaleTransaction> {
    public ForSaleTransMgr() {
        super(ForSaleTransaction.class);
    }

    public String getFirebaseRefName() {
        return "forSaleTransactions";
    }

    // Add new forSale transaction using given fields
    public String addNewForSaleTransaction(String userThatLikedBook, String forSaleBookID, String forSaleBookOwner) {
        String transactionID = this.getInsertKey();
        ForSaleTransaction t = new ForSaleTransaction(transactionID, userThatLikedBook, forSaleBookID, forSaleBookOwner); // create new forSale transaction
        this.getItemsMap().put(transactionID, t);
        Log.d("TESTINFO", "Created new forSale transaction with ID " + transactionID);
        saveToFirebase();
        return transactionID;
    }

    //Gets all the transactions, sales or not, for a specified user
    public ArrayList<Transaction> getAllMatchedTransactionsForUser(String userName)
    {
        ArrayList<Transaction> result = new ArrayList<Transaction>();

        // sale transactions
        for(Map.Entry<String, ForSaleTransaction> entry : this.getItemsMap().entrySet())
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

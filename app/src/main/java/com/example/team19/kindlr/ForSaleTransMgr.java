package com.example.team19.kindlr;

import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

public class ForSaleTransMgr extends FirestoreAccessor<ForSaleTransaction> {

    private static final String TAG = "ForSaleTransMgr";

    public ForSaleTransMgr() {
        super(ForSaleTransaction.class);
    }

    public String getFirestoreCollectionName() {
        return "forSaleTransactions";
    }

    // Add new forSale transaction using given fields
    public String addNewForSaleTransaction(String userThatLikedBook, String forSaleBookID, String forSaleBookOwner) {
        String transactionID = this.getInsertKey();
        ForSaleTransaction t = new ForSaleTransaction(transactionID, userThatLikedBook, forSaleBookID, forSaleBookOwner); // create new forSale transaction
        this.putItem(transactionID, t);

        Log.d(TAG, "Created new forSale transaction with ID " + transactionID);
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

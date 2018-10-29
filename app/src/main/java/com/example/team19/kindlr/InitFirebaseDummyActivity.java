package com.example.team19.kindlr;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;

public class InitFirebaseDummyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_firebase_dummy);

        UserManager.getUserManager().initialize();
        BookManager.getBookManager().initialize();
        TransactionManager.getTransactionManager().initialize();

        UserManager um = UserManager.getUserManager();
        BookManager bm = BookManager.getBookManager();
        TransactionManager tm = TransactionManager.getTransactionManager();

        // Clear database
        um.clearAllUsers();
        bm.clearAllBooks();
        tm.clearAllTransactions();

        // Create users
        um.addUser("andy", "asdf", "Andrew", "Szot", "Los Angeles", "California", "123-456-7890", "szot@usc.edu");
        um.addUser("bhahn", "benhahn", "Ben", "Hahn", "Los Angeles", "California", "314-616-3599", "bhahn@usc.edu");
        um.addUser("lucashu1", "helloworld", "Lucas", "Hu", "Los Angeles", "California", "999-999-9999", "lucashu@usc.edu");

        // Create books
        ArrayList<String> harryPotterTags = new ArrayList<String>();
        harryPotterTags.add("Good for kids");
        harryPotterTags.add("Magic");
        String harryPotterID = bm.postBookForExchange("Harry Potter and the Philosopher's Stone", "9789604533084", "J.K. Rowling", "Fantasy", 300, harryPotterTags, "andy");
        String goDogGoID = bm.postBookForExchange("Go Dog, Go!", "9781299086180", "P.D. Eastman", "Non-Fiction", 10, null, "lucashu1");
        ArrayList<String> sweTags = new ArrayList<String>();
        sweTags.add("Textbook");
        String sweID = bm.postBookForSale("Software Engineering", "978-0133943030", "Ian Sommerville", "Comedy", 500, sweTags, "bhahn");

        // Create transactions (make users like books)
        tm.makeUserLikeBook("andy", goDogGoID);
//        tm.makeUserLikeBook("lucashu1", harryPotterID);
//        tm.makeUserLikeBook("andy", sweID);

    }
}

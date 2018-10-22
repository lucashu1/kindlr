package com.example.team19.kindlr;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TestFirebaseDummyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_firebase_dummy);

        UserManager um = UserManager.getUserManager();
        BookManager bm = BookManager.getBookManager();
        TransactionManager tm = TransactionManager.getTransactionManager();
    }
}

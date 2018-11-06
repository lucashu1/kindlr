package com.example.team19.kindlr;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import android.view.View;
import java.util.ArrayList;

public class ViewExchangeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exchange);
        Intent intent = getIntent();
        final String name = intent.getStringExtra("Name");
        String title = intent.getStringExtra("Title");
        String author = intent.getStringExtra("Author");
        String genre = intent.getStringExtra("Genre");
        String tags = intent.getStringExtra("Tags");
        String rating = intent.getStringExtra("Rating");
        String image = intent.getStringExtra("Image");
        final String username = intent.getStringExtra("Username");
        final String transactionKey = intent.getStringExtra("TransactionKey");

        TextView nameTV = (TextView) findViewById(R.id.name);
        TextView titleTV = (TextView) findViewById(R.id.title);
        TextView authorTV = (TextView) findViewById(R.id.author);
        TextView usernameTV = (TextView) findViewById(R.id.username);
        TextView genreTV = (TextView) findViewById(R.id.genre);
        TextView tagsTV = (TextView) findViewById(R.id.tags);
        TextView ratingTV = (TextView) findViewById(R.id.rating);
        ImageView imageIV = (ImageView) findViewById(R.id.book_image);
        final Button confirm = (Button) findViewById(R.id.confirm);
        final Button deny = (Button) findViewById(R.id.deny);
        final TextView message = (TextView) findViewById(R.id.message);

        nameTV.setText("Name: " + name);
        titleTV.setText("Title: " + title);
        authorTV.setText("Author: " + author);
        usernameTV.setText("Username: " + username);
        genreTV.setText("Genre: " + genre);
        tagsTV.setText("Tags: " + tags);
        ratingTV.setText("Rating: " + rating + "/5");

        final TransactionManager tm = TransactionManager.getTransactionManager();
        final ExchangeTransaction et = (ExchangeTransaction) tm.exchangeTransMgr.getItemByID(transactionKey);
        final UserManager um = UserManager.getUserManager();

        if (et.wasRejected()) {
            confirm.setVisibility(View.INVISIBLE);
            deny.setVisibility(View.INVISIBLE);
            message.setText("THIS TRANSACTION WAS DENIED");
        }

        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (et.wasAccepted()) { //both parties accepted; display contact info
                    confirm.setVisibility(View.INVISIBLE);
                    deny.setVisibility(View.INVISIBLE);
                    String phone = um.getUserByUsername(username).getPhoneNum();
                    String email = um.getUserByUsername(username).getEmail();
                    message.setText("BOTH USERS ACCEPTED THIS TRANSACTION! GET IN CONTACT " +
                            "WITH " + name + ": " + phone + " or " + email);
                } else { //just current user accepted, waiting on other user's response
                    et.acceptTransaction();
                    confirm.setVisibility(View.INVISIBLE);
                    deny.setVisibility(View.INVISIBLE);
                    message.setText("YOU HAVE ACCEPTED THIS TRANSACTION! PLEASE WAIT FOR OTHER USER" +
                            " TO RESPOND, THEY WILL CONTACT YOU IF THEY ACCEPT.");
                }
            }
        });

        deny.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirm.setVisibility(View.INVISIBLE);
                deny.setVisibility(View.INVISIBLE);
                message.setText("THIS TRANSACTION WAS DENIED");
                et.rejectTransaction();
            }
        });
    }
}

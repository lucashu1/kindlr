package com.example.team19.kindlr;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;
import android.view.ViewGroup.LayoutParams;
import android.view.View;
import com.squareup.picasso.Picasso;
import android.content.Intent;
import java.util.ArrayList;
import android.util.Log;

public class ViewNotificationsActivity extends Activity {

    private final static String TAG = "NotificationsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notifications);
        displayNotifications();
    }

    /*
    Displays all notification blocks for this user
     */
    public void displayNotifications() {
        UserManager um = UserManager.getUserManager();
        TransactionManager tm = TransactionManager.getTransactionManager();
        User currentUser = um.getCurrentUser();

        ArrayList<Transaction> matches = tm.getAllMatchedTransactionsForUser(currentUser.getUsername());

        Log.d(TAG, "The matches are " + matches.toString());

        for (int i = 0; i < matches.size(); i++) {
            Transaction tx = matches.get(i);

            Log.d(TAG, "Found transaction " + tx.toString());

            Book otherUsersBook = tx.getOtherUsersBook();
            User otherUser = tx.getOtherUserInTransaction();

            Log.d(TAG, "Other user's book " + otherUsersBook.toString());
            Log.d(TAG, "Other user " + otherUser.toString());

            TableLayout table = (TableLayout)findViewById(R.id.notifications_table_layout);
            TableRow row = new TableRow(this);

            ImageView img = new ImageView(this);
            final String imageURL = "https://png.pngtree.com/element_pic/17/07/27/bd157c7c747dc708790aa64b43c3da35.jpg";
            Picasso.get().load(imageURL).into(img);


            TextView t = new TextView(this);
            final String name = otherUser.getFirstName() + " " + otherUser.getLastName();
            final String rating = Double.toString(otherUser.getRating());
            final String title = otherUsersBook.getBookName();
            final String author = otherUsersBook.getAuthor();
            final String genre = otherUsersBook.getGenre();
            final String username = otherUser.getUsername();
            String tagsTemp = "";
            ArrayList<String> tagsList = (ArrayList<String>) otherUsersBook.getTags();
            for (int j = 0; j < tagsList.size(); j++) {
                tagsTemp += tagsList.get(j) + " ";
            }
            final String tags = tagsTemp.trim();
            final String transactionKey = tx.getTransactionID();


            String textToDisplay =  "Name: " + name + "\n" +
                                    "Title: " + title + "\n" +
                                    "Author: " + author + "\n" +
                                    "Genre: " + genre + "\n" +
                                    "Tags: " + tags + "\n" +
                                    "Rating: " + rating + "/5\n";
            t.setText(textToDisplay);
            t.setWidth(500);

            Button view = new Button(this);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ViewExchangeActivity.class);
                    intent.putExtra("Name", name);
                    intent.putExtra("Title", title);
                    intent.putExtra("Author", author);
                    intent.putExtra("Genre", genre);
                    intent.putExtra("Tags", tags);
                    intent.putExtra("Rating", rating);
                    intent.putExtra("Image", imageURL);
                    intent.putExtra("Username", username);
                    intent.putExtra("TransactionKey", transactionKey);
                    startActivity(intent);
                }
            });
            view.setText("VIEW");

            //TODO: Generalize
            if (i == 0) view.setId(R.id.view_notification_1);
            else if (i == 1) view.setId(R.id.view_notification_2);
            else if (i == 2) view.setId(R.id.view_notification_3);

            row.addView(img);
            row.addView(t);
            row.addView(view);

            table.addView(row, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
    }
}

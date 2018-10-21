package com.example.team19.kindlr;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;
import android.view.ViewGroup.LayoutParams;
import com.squareup.picasso.Picasso;

public class ViewNotificationsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notifications);

        User u; //TODO: set equal to current user and pass into displayNotifications()
        displayNotifications();
    }

    /*
    Displays all notification blocks for this user
     */
    public void displayNotifications() {
        int num_matches = 4; //TODO: set equal to this user's number of matches
        for (int i = 0; i < num_matches; i++) {
            TableLayout table = (TableLayout)findViewById(R.id.notifications_table_layout);
            TableRow row = new TableRow(this);

            ImageView img = new ImageView(this);
            String imageURL = "https://images-na.ssl-images-amazon.com/images/I/815egbJMs1L._AC_UL320_SR256,320_.jpg";
            //TODO: Set imageURL to the image needed (Picasso also allows you to load image files instead of use URL's)
            Picasso.get().load(imageURL).into(img);


            TextView t = new TextView(this);
            String name = "Ben";
            String username="bhahn";
            String rating = "4";
            String title = "Software Engineering";
            String author = "I forget";
            String genre = "Computer Science";
            String tags = "long, boring, expensive";
            String price = "";

            String textToDisplay =  "Name: " + name + "\n" +
                                    "Username: " + username + "\n" +
                                    "Rating: " + rating + "\n" +
                                    "Title: " + title + "\n" +
                                    "Author: " + author + "\n" +
                                    "Genre: " + genre + "\n" +
                                    "Tags: " + tags + "\n" +
                                    "Price: " + price;
            t.setText(textToDisplay);
            t.setWidth(360);

            Button confirm = new Button(this);
            Button deny = new Button(this);
            confirm.setText("CONFIRM");
            deny.setText("DENY");

            row.addView(img);
            row.addView(t);
            row.addView(confirm);
            row.addView(deny);

            table.addView(row, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
    }
}

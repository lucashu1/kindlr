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

public class ViewNotificationsActivity extends Activity {

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
        int num_matches = 4; //TODO: set equal to current user's number of matches
        for (int i = 0; i < num_matches; i++) {
            TableLayout table = (TableLayout)findViewById(R.id.notifications_table_layout);
            TableRow row = new TableRow(this);

            ImageView img = new ImageView(this);
            final String imageURL = "https://images-na.ssl-images-amazon.com/images/I/815egbJMs1L._AC_UL320_SR256,320_.jpg";
            //TODO: Set imageURL to the image needed (Picasso also allows you to load image files instead of use URL's)
            Picasso.get().load(imageURL).into(img);


            TextView t = new TextView(this);
            final String name = "Ben" + i;
            final String rating = "4";
            final String title = "Software Engineering";
            final String author = "Ian Sommerville";
            final String genre = "Computer Science";
            final String tags = "long, boring, expensive";
            final String username = "bhahn16";

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
                    startActivity(intent);
                }
            });
            view.setText("VIEW");

            row.addView(img);
            row.addView(t);
            row.addView(view);

            table.addView(row, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
    }
}

package com.example.team19.kindlr;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;
import android.widget.ImageView;
import android.util.Log;

public class ViewExchangeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exchange);
        Intent intent = getIntent();
        String name = intent.getStringExtra("Name");
        String title = intent.getStringExtra("Title");
        String author = intent.getStringExtra("Author");
        String genre = intent.getStringExtra("Genre");
        String tags = intent.getStringExtra("Tags");
        String rating = intent.getStringExtra("Rating");
        String image = intent.getStringExtra("Image");
        String username = intent.getStringExtra("Username");

        TextView nameTV = (TextView) findViewById(R.id.name);
        TextView titleTV = (TextView) findViewById(R.id.title);
        TextView authorTV = (TextView) findViewById(R.id.author);
        TextView usernameTV = (TextView) findViewById(R.id.username);
        TextView genreTV = (TextView) findViewById(R.id.genre);
        TextView tagsTV = (TextView) findViewById(R.id.tags);
        TextView ratingTV = (TextView) findViewById(R.id.rating);
        ImageView imageIV = (ImageView) findViewById(R.id.image);

        nameTV.setText("Name: " + name);
        titleTV.setText("Title: " + title);
        authorTV.setText("Author: " + author);
        usernameTV.setText("Username: " + username);
        genreTV.setText("Genre: " + genre);
        tagsTV.setText("Tags: " + tags);
        ratingTV.setText("Rating: " + rating + "/5");
    }

}

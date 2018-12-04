package com.example.team19.kindlr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewProfileActivity extends Activity {
    private boolean isCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

//        isCurrentUser = (UserManager.getUserManager().getCurrentUser().getUsername().equals(displayUser.getUsername()));

//        TextView profileNameView = (TextView)findViewById(R.id.profile_name);
//        profileNameView.setText(displayUser.getUsername());

        populateFields();

        Button postBookBtn = (Button) findViewById(R.id.post_book_button);
        postBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToPostBook(false);
            }
        });

        Button sellBookBtn = (Button) findViewById(R.id.sell_book_button);
        sellBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToPostBook(true);
            }
        });

        Button signoutBtn = (Button) findViewById(R.id.signout);
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        Button editProfBtn = (Button) findViewById(R.id.edit_profile_button);
        editProfBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

//        if (!isCurrentUser) {
//            postBookBtn.setVisibility(View.GONE);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    private void populateFields() {
        User currentUser = UserManager.getUserManager().getCurrentUser();
        TextView firstNameView = (TextView)findViewById(R.id.first_name_view);
        firstNameView.setText("Name: " + currentUser.getFirstName() + " " + currentUser.getLastName());

        TextView usernameView = (TextView)findViewById(R.id.username_view);
        usernameView.setText("Username: "+ UserManager.getUserManager().getCurrentUser().getUsername());

        TextView cityView = (TextView)findViewById(R.id.city_view);
        cityView.setText("Location: " + currentUser.getCity() + ", " + currentUser.getState());

        TextView phoneView = (TextView)findViewById(R.id.phone_view);
        phoneView.setText("Phone: " + UserManager.getUserManager().getCurrentUser().getPhoneNum());

        TextView emailView = (TextView)findViewById(R.id.email_view);
        emailView.setText("Email: " + UserManager.getUserManager().getCurrentUser().getEmail());

        TextView ratingView = (TextView)findViewById(R.id.rating_view);
        ratingView.setText("Rating: " + UserManager.getUserManager().getCurrentUser().getRating());


        TextView likedBooksView = (TextView)findViewById(R.id.liked_books_view);
        likedBooksView.setText("Liked Books: " + currentUser.getLikedBooksString());


        TextView postedBooksView = (TextView)findViewById(R.id.posted_books_view);
        postedBooksView.setText("Posted Books: " + currentUser.getPostedBooksString());
    }

    public void navigateToPostBook(boolean isForSale) {
        Intent intent = new Intent(ViewProfileActivity.this, PostBookActivity.class);
        intent.putExtra("isForSale", isForSale);
        startActivity(intent);
    }

}

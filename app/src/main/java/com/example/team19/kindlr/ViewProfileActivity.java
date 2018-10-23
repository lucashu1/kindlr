package com.example.team19.kindlr;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewProfileActivity extends Activity {
    private User displayUser;
    private boolean isCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        displayUser = (User)getIntent().getSerializableExtra("DISPLAY_USER");
        isCurrentUser = (UserManager.getUserManager().getCurrentUser().getUsername().equals(displayUser.getUsername()));

        TextView profileNameView = (TextView)findViewById(R.id.profile_name);
        profileNameView.setText(displayUser.getUsername());

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

        if (!isCurrentUser) {
            postBookBtn.setVisibility(View.GONE);
        }
    }

    public void navigateToPostBook(boolean isForSale) {
        Intent intent = new Intent(ViewProfileActivity.this, PostBookActivity.class);
        intent.putExtra("isForSale", isForSale);
        startActivity(intent);
    }

}

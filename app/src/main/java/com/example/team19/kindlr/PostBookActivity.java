package com.example.team19.kindlr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


public class PostBookActivity extends AppCompatActivity {

    private EditText title;
    private EditText isbn;
    private EditText author;
    private EditText genre;
    private EditText pagecount;
    private boolean isForSale;
    private TextView errorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_book);

        title = (EditText)findViewById(R.id.title);
        isbn = (EditText) findViewById(R.id.isbn);
        author = (EditText) findViewById(R.id.author);
        genre = (EditText) findViewById(R.id.genre);
        pagecount = (EditText) findViewById(R.id.pagecount);
        errorView = (TextView)findViewById(R.id.error_msg);


        isForSale = getIntent().getBooleanExtra("isForSale", false);

        Button postBtn = (Button) findViewById(R.id.post);
        postBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean success = postBook();
                if (success) {
                    navigateBack();
                }
                else {
                    ErrorHelper.displayError("Error", "Invalid input", PostBookActivity.this);
                }
            }
        });

        Button backBtn = (Button) findViewById(R.id.back);
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateBack();
            }
        });
    }

    private boolean postBook() {
        String titleStr = title.getText().toString();
        String isbnStr = isbn.getText().toString();
        String authorStr = author.getText().toString();
        String genreStr = genre.getText().toString();

        int pageCountInt = 0;
        try {
            pageCountInt = Integer.parseInt(pagecount.getText().toString());
        } catch (NumberFormatException e) {
            pageCountInt = -1;
        }

        if (pageCountInt < 0) {
            return false;
        }

        if (titleStr.isEmpty() || isbnStr.isEmpty() || authorStr.isEmpty() || genreStr.isEmpty()) {
            return false;
        }

        User currentUser = UserManager.getUserManager().getCurrentUser();

        if (!isForSale) {
            BookManager.getBookManager().postBookForExchange(titleStr, isbnStr, authorStr, genreStr,
                    pageCountInt, new ArrayList<String>(), currentUser.getUsername());
        }
        else {
            BookManager.getBookManager().postBookForSale(titleStr, isbnStr, authorStr, genreStr,
                    pageCountInt, new ArrayList<String>(), currentUser.getUsername());
        }

        return true;
    }

    private void navigateBack() {
        this.finish();
    }



}

package com.example.team19.kindlr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
    private EditText imageurl;
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
        imageurl = (EditText) findViewById(R.id.image_url);
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
//                else {
//                    ErrorHelper.displayError("Error", "Invalid input", PostBookActivity.this);
//                }
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
        String imageURL = imageurl.getText().toString();
        String numPages = pagecount.getText().toString();

        View focusView = null;

        //Set to default image if user does not provide link
        if (imageURL.equals("") || imageURL == null) imageURL = "https://png.pngtree.com/element_pic/17/07/27/bd157c7c747dc708790aa64b43c3da35.jpg";

        boolean isInvalid = false;
        if(titleStr.isEmpty())
        {
            title.setError(getString(R.string.error_field_required));
            focusView = title;
            isInvalid = true;
        }

        if(isbnStr.isEmpty())
        {
            isbn.setError(getString(R.string.error_field_required));
            focusView = isbn;
            isInvalid = true;
        }

        if(authorStr.isEmpty())
        {
            author.setError(getString(R.string.error_field_required));
            focusView = author;
            isInvalid = true;
        }
        else
        {
            if(authorStr.matches(".*\\d+.*"))
            {
                author.setError("Not a valid name");
                focusView = author;
                isInvalid = true;
            }
        }

        if(genreStr.isEmpty())
        {
            genre.setError(getString(R.string.error_field_required));
            focusView = genre;
            isInvalid = true;
        }

        int pageCountInt = 0;
        if(numPages.isEmpty())
        {
            pagecount.setError(getString(R.string.error_field_required));
            focusView = pagecount;
            isInvalid = true;
        }
        else
        {
            try {
                pageCountInt = Integer.parseInt(pagecount.getText().toString());
            } catch (NumberFormatException e) {
                pageCountInt = -1;
            }

            if (pageCountInt < 0) {
                return false;
            }
        }

        if(isInvalid)
        {
            focusView.requestFocus();
            return false;
        }

        User currentUser = UserManager.getUserManager().getCurrentUser();

        if (!isForSale) {
            BookManager.getBookManager().postBookForExchange(titleStr, isbnStr, authorStr, genreStr,
                    pageCountInt, new ArrayList<String>(), currentUser.getUsername(), imageURL);
        }
        else {
            BookManager.getBookManager().postBookForSale(titleStr, isbnStr, authorStr, genreStr,
                    pageCountInt, new ArrayList<String>(), currentUser.getUsername(), imageURL);
        }

        return true;
    }

    private void navigateBack() {
        this.finish();
    }



}

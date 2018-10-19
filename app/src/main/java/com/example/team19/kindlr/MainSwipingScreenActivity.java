package com.example.team19.kindlr;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainSwipingScreenActivity extends Activity {

    private final Book testBook = new Book(0, "Harry Potter", "1234", "JK Rowling", "magic", 500, new ArrayList());
    private Book currentBook;
    private TextView bookTitle;
    private TextView bookAuthor;
    List<Book> curBooks = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_swiping_screen);

        Button likeBtn = (Button) findViewById(R.id.like_button);
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Button dislikeBtn = (Button) findViewById(R.id.dislike_button);
        dislikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void refreshBook() {
        curBooks =
    }

    private void updateDisplay(Book book) {
        bookTitle.setText(book.getBookName());
        bookAuthor.setText(book.getAuthor());
    }

}

package com.example.team19.kindlr;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainSwipingScreenActivity extends Activity {
    private TextView bookTitle;
    private TextView bookAuthor;
    private BookManager bookMgr = BookManager.getBookManager();
    private BookFilter bookFilter = new BookFilter();
    List<Book> curBooks = null;
    private int curIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_swiping_screen);

        Button likeBtn = (Button) findViewById(R.id.like_button);
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementIndex();
            }
        });

        Button dislikeBtn = (Button) findViewById(R.id.dislike_button);
        dislikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementIndex();
            }
        });

        refreshBook();
    }

    private void incrementIndex() {
        curIndex += 1;
        if (curIndex >= curBooks.size()) {
            curIndex = 0;
        }
    }

    private void refreshBook() {
        curBooks = bookMgr.getFilteredBooks(bookFilter);
        Log.i("InfoMsg", "" + curBooks.size());

        if (curIndex > curBooks.size()) {
            curIndex = 0;
        }
        updateDisplay();
    }

    private Book getCurrentBook() {
        if (curBooks.size() == 0)
            return null;
        return curBooks.get(curIndex);
    }

    private void updateDisplay() {
        Book book = getCurrentBook();
        bookTitle.setText(book.getBookName());
        bookAuthor.setText(book.getAuthor());
    }

}

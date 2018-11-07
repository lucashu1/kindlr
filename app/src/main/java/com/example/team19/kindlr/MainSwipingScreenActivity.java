package com.example.team19.kindlr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class MainSwipingScreenActivity extends Activity {
    private TextView bookTitle;
    private TextView bookAuthor;
    private BookManager bookMgr = BookManager.getBookManager();
    private BookFilter bookFilter = new BookFilter();
    List<Book> curBooks = null;
    private int curIndex = 0;
    private User currentUser;
    private EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_swiping_screen);

        bookTitle = (TextView)findViewById(R.id.title_text);
        bookAuthor = (TextView)findViewById(R.id.author_text);

        this.currentUser = UserManager.getUserManager().getCurrentUser();

        Button likeBtn = (Button) findViewById(R.id.like_button);
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeBook();
                incrementIndex();
            }
        });

        Button dislikeBtn = (Button) findViewById(R.id.dislike_button);
        dislikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dislikeBook();
                incrementIndex();
            }
        });

        Button profileBtn = (Button)findViewById(R.id.profile_button);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToProfile();
            }
        });

        Button notificationsBtn = (Button) findViewById(R.id.notifications);
        notificationsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewNotificationsActivity.class);
                startActivity(intent);
            }
        });

        Button filterBtn = (Button)findViewById(R.id.filter_button);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText = findViewById(R.id.searchFilter);
                String stringSearchText = searchText.getText().toString();
                Log.i("FilterText",stringSearchText);
                bookFilter.setSearchText(stringSearchText);
//                filterBooks(stringSearchText);
                refreshBook();


            }
        });

        refreshBook();
    }

    private void dislikeBook() {
        Book book = this.getCurrentBook();
        if (book == null) {
            ErrorHelper.displayError("Invalid", "No book to dislike!", this);
        }
        else {
            TransactionManager.getTransactionManager().makeUserDislikeBook(this.currentUser.getUsername(), book.getBookID());
        }
    }

    private void likeBook() {
        Book book = this.getCurrentBook();
        if (book == null) {
            ErrorHelper.displayError("Invalid", "No book to like!", this);
        }
        else {
            TransactionManager.getTransactionManager().makeUserLikeBook(this.currentUser.getUsername(), book.getBookID());
        }
    }

    private void navigateToProfile() {
        Intent intent = new Intent(MainSwipingScreenActivity.this, ViewProfileActivity.class);
        intent.putExtra("DISPLAY_USER", UserManager.getUserManager().getCurrentUser());
        startActivity(intent);
    }

    private void incrementIndex() {
        if (this.curBooks.size() == 0) {
            curIndex = 0;
            return;
        }

        this.curBooks.remove(curIndex);

        curIndex += 1;
        if (curIndex >= curBooks.size()) {
            curIndex = 0;
        }

        updateDisplay();
    }

    private void refreshBook() {
        curBooks = bookMgr.getFilteredBooks(bookFilter, currentUser);
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
        if (book == null) {
            bookTitle.setText("No books!");
            bookAuthor.setText("Surprising!");
        }
        else {
            bookTitle.setText(book.getBookName());
            bookAuthor.setText(book.getAuthor());
        }
    }

}

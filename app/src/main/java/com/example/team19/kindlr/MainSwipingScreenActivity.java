package com.example.team19.kindlr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainSwipingScreenActivity extends Activity {
    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView forSaleText;
    private BookManager bookMgr = BookManager.getBookManager();
    private BookFilter bookFilter = new BookFilter();
    List<Book> curBooks = null;
    private int curIndex = 0;
    private User currentUser;
    private EditText searchText;
    private ImageView iv;

    public static Boolean usersRefreshed;
    public static Boolean booksRefreshed;
    public static Boolean forSaleTransactionsRefreshed;
    public static Boolean exchangeTransactionsRefreshed;

    private final static String LOG_TAG = "MainSwipingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_swiping_screen);

        bookTitle = (TextView)findViewById(R.id.title_text);
        bookAuthor = (TextView)findViewById(R.id.author_text);
        forSaleText = (TextView)findViewById(R.id.for_sale_text);

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

        iv = (ImageView) findViewById(R.id.image);

        refreshBook();
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentUser = UserManager.getUserManager().getCurrentUser() != null ? UserManager.getUserManager().getCurrentUser() : currentUser;
        Log.d(LOG_TAG, "Resumed MainSwipingScreenActivity. CurrentUser: " + currentUser.getUsername());
        refreshBook();
    }

    private void dislikeBook() {
        Book book = this.getCurrentBook();
        if (book == null) {
            ErrorHelper.displayError("Invalid", "No book to dislike!", this);
        }
        else {
            TransactionManager.getTransactionManager().makeUserDislikeBook(UserManager.getUserManager().getCurrentUser().getUsername(), book.getBookID());
        }
    }

    private void likeBook() {
        Book book = this.getCurrentBook();
        if (book == null) {
            ErrorHelper.displayError("Invalid", "No book to like!", this);
        }
        else {
            TransactionManager.getTransactionManager().makeUserLikeBook(UserManager.getUserManager().getCurrentUser().getUsername(), book.getBookID());
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
        currentUser = UserManager.getUserManager().getCurrentUser() != null ? UserManager.getUserManager().getCurrentUser() : currentUser;
        curBooks = BookManager.getBookManager().getFilteredBooks(bookFilter, currentUser);
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
            String noBooksImageURL = "https://banner2.kisspng.com/20180407/yhw/kisspng-empty-set-null-set-symbol-mathematics-forbidden-5ac859ad09c119.24223671152307959704.jpg";
            Picasso.get().load(noBooksImageURL).resize(500,500).centerCrop().into(iv);
            forSaleText.setVisibility(View.GONE);
        }
        else {
            String imageURL = book.getImageURL();
            Picasso.get().load(imageURL).resize(500,500).centerCrop().into(iv);
            bookTitle.setText("Title: " + book.getBookName());
            bookAuthor.setText("Author: " + book.getAuthor());
            int visibilityStatus = book.getForSale() ? View.VISIBLE : View.GONE;
            forSaleText.setVisibility(visibilityStatus);
        }
    }

}

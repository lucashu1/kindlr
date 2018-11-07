package com.example.team19.kindlr;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookManager extends FirebaseAccessor<Book> {
    // Singleton logic
    private static BookManager bookManagerSingleton;
    public synchronized static BookManager getBookManager() {
        if (bookManagerSingleton == null)
            bookManagerSingleton = new BookManager();
        return bookManagerSingleton;
    }

    // BookManager constructor
    public BookManager() {
        super(Book.class);
    }

    public String getFirebaseRefName() {
        return "books";
    }

    // Return username of book owner for a given bookID. Return null if book not found
    public String getBookOwner(String bookID) {
        if (!this.doesItemExist(bookID)) {
            Log.d("WARN", "called getBookOwner() on a bookID that wasn't found: " + bookID);
            return null;
        }

        return getItemByID(bookID).getOwner();
    }

    public List<Book> getFilteredBooks(BookFilter bookFilter, User forUser) {
        Log.i("TESTINFO", "Getting filtered books");
        List<Book> filteredBooks = new ArrayList();

        // Filter out current user's already liked/disliked books
        List<String> currentUserDislikedBooks = forUser.getDislikedBooks();
        List<String> currentUserLikedBooks = forUser.getLikedBooks();

        for (Map.Entry<String, Book> entry : getItemsMap().entrySet()) {
            Book b = entry.getValue();
            String bookID = b.getBookID();

            boolean matchesFilter = bookFilter.isMatch(entry.getValue());
            boolean isNotDisliked = !currentUserDislikedBooks.contains(bookID);
            boolean isNotLiked = !currentUserLikedBooks.contains(bookID);
            boolean doesNotOwn = !b.getOwner().equals(forUser.getUsername());
            boolean isVisible = b.isVisible();

            // Get books that match the filter, have not already been liked/disliked, do not belong to current user, and are not invisible
            if (matchesFilter && isNotDisliked && isNotLiked && doesNotOwn && isVisible) {
                filteredBooks.add(entry.getValue());
            }
        }

        return filteredBooks;
    }

    public String postBookForExchange(String bookName, String isbn, String author,
                                                   String genre, int pageCount, List<String> tags, String owner) {

        return postBook(bookName, isbn, author, genre, pageCount, tags, owner, false);
    }


    public String postBookForSale(String bookName, String isbn, String author, String genre,
                                               int pageCount, List<String> tags, String owner){
        return postBook(bookName, isbn, author, genre, pageCount, tags, owner, true);
    }

    public String postBook(String bookName, String isbn, String author, String genre,
                         int pageCount, List<String> tags, String owner, boolean forSale) {

        String bookKey = this.getInsertKey();
        Book book = new Book(bookKey, bookName, isbn, author, genre, pageCount, tags, forSale, owner);

        // Add book to map, save to firebase
        this.getItemsMap().put(bookKey, book);
        saveToFirebase();

        return bookKey;
    }

}

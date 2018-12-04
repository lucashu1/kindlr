package com.example.team19.kindlr;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookManager extends FirestoreAccessor<Book> {
    // Singleton logic
    private static BookManager bookManagerSingleton;
    public synchronized static BookManager getBookManager() {
        if (bookManagerSingleton == null)
            bookManagerSingleton = new BookManager();
        return bookManagerSingleton;
    }

    private final static String TAG = "BookManager";

    // BookManager constructor
    public BookManager() {
        super(Book.class);
    }

    public String getFirestoreCollectionName() {
        return "books";
    }

    // Return username of book owner for a given bookID. Return null if book not found
    public String getBookOwner(String bookID) {
        if (!this.doesItemExist(bookID)) {
            Log.d(TAG, "WARNING: called getBookOwner() on a bookID that wasn't found: " + bookID);
            return null;
        }

        return getItemByID(bookID).getOwner();
    }

    // Return arraylist of books owned by a certain username
    public ArrayList<Book> getBooksOwnedByUser(String username) {
        ArrayList<Book> ownedBooks = new ArrayList<Book>();
        for (Book b : this.getItemsMap().values()) {
            if (b.getOwner().equals(username)) {
                ownedBooks.add(b);
            }
        }
        return ownedBooks;
    }

    public List<Book> getFilteredBooks(BookFilter bookFilter, User forUser) {
        Log.d(TAG, "Getting filtered books");
        List<Book> filteredBooks = new ArrayList();

        // Filter out current user's already liked/disliked books
        List<String> currentUserDislikedBooks = forUser.getDislikedBooks();
        List<String> currentUserLikedBooks = forUser.getLikedBooks();

        Log.d(TAG, "" + currentUserDislikedBooks.toString());
        Log.d(TAG, "" + currentUserLikedBooks.toString());

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

    // With image
    public String postBookForExchange(String bookName, String isbn, String author,
                                                   String genre, int pageCount, List<String> tags, String owner, String imageURL) {

        return postBook(bookName, isbn, author, genre, pageCount, tags, owner, imageURL, false);
    }

    // No image
    public String postBookForExchange(String bookName, String isbn, String author,
                                      String genre, int pageCount, List<String> tags, String owner) {

        return postBook(bookName, isbn, author, genre, pageCount, tags, owner, null, false);
    }


    // With image
    public String postBookForSale(String bookName, String isbn, String author, String genre,
                                               int pageCount, List<String> tags, String owner, String imageURL){
        return postBook(bookName, isbn, author, genre, pageCount, tags, owner, imageURL, true);
    }

    // No image
    public String postBookForSale(String bookName, String isbn, String author, String genre,
                                  int pageCount, List<String> tags, String owner){
        return postBook(bookName, isbn, author, genre, pageCount, tags, owner, null, true);
    }

    public String postBook(String bookName, String isbn, String author, String genre,
                         int pageCount, List<String> tags, String owner, String imageURL, boolean forSale) {

        String bookKey = this.getInsertKey();
        Book book = new Book(bookKey, bookName, isbn, author, genre, pageCount, tags, forSale, owner, imageURL);

        // Add book to map, save to firebase
        this.putItem(bookKey, book);

        return bookKey;
    }

}

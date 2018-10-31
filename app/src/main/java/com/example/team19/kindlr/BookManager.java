package com.example.team19.kindlr;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookManager {

    // Book ID to Book object
    private Map<String, Book> booksMap;
    private FirebaseDatabase database;
    private DatabaseReference booksRef;
//    private DatabaseReference booksRef;
    private boolean initialized;

    // Singleton logic
    private static BookManager bookManagerSingleton;
    public synchronized static BookManager getBookManager() {
        if (bookManagerSingleton == null)
            bookManagerSingleton = new BookManager();
        return bookManagerSingleton;
    }

    // BookManager constructor
    public BookManager() {
        booksMap = new HashMap<String, Book>();
        initialized = false;
    }

    public void initialize() {
        if (initialized)
            return;

        database  = FirebaseDatabase.getInstance();
        booksRef = database.getReference("books");

        // On data change, re-read booksMap from the database
        booksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d("TESTINFO", "Books being updated");
                booksMap = new HashMap<String, Book>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Book book = snapshot.getValue(Book.class);
                    booksMap.put(snapshot.getKey(), book);
                }
                Log.d("TESTINFO", "Refreshed booksMap to be " + booksMap.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("WARN", "Failed to read value.", error.toException());
            }
        });

        initialized = true;
    }

    public HashMap<String, Book> getAllBooks() {
        return (HashMap<String, Book>) booksMap;
    }

    // Save usersMap to Firebase (write to DB)
    public synchronized void saveToFirebase() {
        booksRef.setValue(booksMap);
    }

    public boolean doesBookExist(String bookID) {
        return (booksMap.containsKey(bookID));
    }

    public void deleteBook(String bookID) {
        if (!doesBookExist(bookID))
            return;

        booksMap.remove(bookID);
        DatabaseReference bookRef = booksRef.child(bookID);
        bookRef.removeValue();
    }

    // Get book with given ID
    public Book getBookByID(String bookID) {
        if (!booksMap.containsKey(bookID))
            return null;
        return booksMap.get(bookID);
    }

    // Return true if a given bookID exists
    public boolean bookExists(String bookID) {
        return doesBookExist(bookID);
    }

    // Remove a book from booksMap (e.g. after transaction done)
        // Return true if successful
//    public boolean removeBook(String bookID) {
//        if (!booksMap.containsKey(bookID))
//            return false;
//        booksMap.remove(bookID);
//        saveToFirebase();
//        return true;
//    }

    // Return username of book owner for a given bookID. Return null if book not found
    public String getBookOwner(String bookID) {
        if (!bookExists(bookID)) {
            Log.d("WARN", "called getBookOwner() on a bookID that wasn't found: " + bookID);
            return null;
        }

        return getBookByID(bookID).getOwner();
    }

    public List<Book> getFilteredBooks(BookFilter bookFilter, User forUser) {
        Log.i("TESTINFO", "Getting filtered books");
        List<Book> filteredBooks = new ArrayList();

        // Filter out current user's already liked/disliked books
        List<String> currentUserDislikedBooks = forUser.getDislikedBooks();
        List<String> currentUserLikedBooks = forUser.getLikedBooks();

        for (Map.Entry<String, Book> entry : booksMap.entrySet()) {
            Book b = entry.getValue();
            String bookID = b.getBookID();

            boolean matchesFilter = bookFilter.isMatch(entry.getValue());
            boolean isNotDisliked = !currentUserDislikedBooks.contains(bookID);
            boolean isNotLiked = !currentUserLikedBooks.contains(bookID);
            boolean doesNotOwn = !b.getOwner().equals(forUser.getUsername());
            boolean isVisible = b.isVisible();

            Log.i("TESTINFO", "Truth values: " + matchesFilter + ", " + isNotDisliked +
                    ", " + isNotLiked + ", " + doesNotOwn + ", " + isVisible);

            // Get books that match the filter, have not already been liked/disliked, do not belong to current user, and are not invisible
            if (matchesFilter && isNotDisliked && isNotLiked && doesNotOwn && isVisible) {
                filteredBooks.add(entry.getValue());
            }
        }

        Log.i("TESTINFO", "Final filtered books " + filteredBooks.toString());

        return filteredBooks;
    }

    public synchronized String postBookForExchange(String bookName, String isbn, String author, String genre, int pageCount, List<String> tags, String owner) {
        String bookKey = booksRef.push().getKey();
        Book book = new Book(bookKey, bookName, isbn, author, genre, pageCount, tags, false, owner);

        // Add book to map, save to firebase
        booksMap.put(bookKey, book);
        saveToFirebase();
        return bookKey;
    }


    public synchronized String postBookForSale(String bookName, String isbn, String author, String genre, int pageCount, List<String> tags, String owner){
        String bookKey = booksRef.push().getKey();
        Book book = new Book(bookKey, bookName, isbn, author, genre, pageCount, tags, true, owner);

        // Add book, update the firebase database
        booksMap.put(bookKey, book);
        saveToFirebase();
        return bookKey;
    }

    // Clear all books from Firebase. Can't undo!
    public void clearAllBooks() {
//        booksMap = new HashMap<String, Book>();
//        booksRef.setValue(booksMap);
        booksRef.removeValue();
    }





}

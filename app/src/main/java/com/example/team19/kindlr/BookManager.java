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
    public Map<String, Book> booksMap;
    private FirebaseDatabase database;
    private DatabaseReference booksRef;
//    private DatabaseReference booksRef;

    // Singleton logic
    private static BookManager bookManagerSingleton;
    public static BookManager getBookManager() {
        if (bookManagerSingleton == null)
            bookManagerSingleton = new BookManager();
        return bookManagerSingleton;
    }

    // BookManager constructor
    public BookManager() {
        booksMap = new HashMap<String, Book>();
        database  = FirebaseDatabase.getInstance();
        booksRef = database.getReference("books");
//        booksRef = ref.child("books");
//        refreshBooks(); // pull from DB

        // On data change, re-read booksMap from the database
        booksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                booksMap = (HashMap<String, Book>) dataSnapshot.getValue();
                Log.d("INFO", "Refreshed booksMap");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("WARN", "Failed to read value.", error.toException());
            }
        });
    }

    public HashMap<String, Book> getAllBooks() {
        return (HashMap<String, Book>) booksMap;
    }

    // Save usersMap to Firebase (write to DB)
    public void saveToFirebase() {
        booksRef.setValue(booksMap);
    }

    // Get book with given ID
    public Book getBookByID(String bookID) {
        if (!booksMap.containsKey(bookID))
            return null;
        return booksMap.get(bookID);
    }

    // Return true if a given bookID exists
    public boolean bookExists(String bookID) {
        return (getBookByID(bookID) != null);
    }

    // Remove a book from booksMap (e.g. after transaction done)
        // Return true if successful
    public boolean removeBook(String bookID) {
        if (!booksMap.containsKey(bookID))
            return false;
        booksMap.remove(bookID);
        saveToFirebase();
        return true;
    }

    // Return username of book owner for a given bookID. Return null if book not found
    public String getBookOwner(String bookID) {
        Book b;
        for (Map.Entry<String, Book> entry : booksMap.entrySet()) {
            b = entry.getValue();
            if (b.getBookID().equals(bookID)) {
                return b.getOwner();
            }
        }
        return null;
    }

//    // Refresh books from DB/Firebase
//    public void refreshBooks() {
//        // TODO: pull from DB
//    }

    public List<Book> getFilteredBooks(BookFilter bookFilter){
        List<Book> filteredBooks = new ArrayList();

        // Filter out current user's already liked/disliked books
        List<String> currentUserDislikedBooks = UserManager.getUserManager().getCurrentUser().getDislikedBooks();
        List<String> currentUserLikedBooks = UserManager.getUserManager().getCurrentUser().getLikedBooks();

        for (Map.Entry<String, Book> entry : booksMap.entrySet()) {
            Book b = entry.getValue();
            String bookID = b.getBookID();
            // Get books that match the filter, have not already been liked/disliked, do not belong to current user, and are not invisible
            if (bookFilter.isMatch(entry.getValue())
                    && !currentUserDislikedBooks.contains(bookID)
                    && !currentUserLikedBooks.contains(bookID)
                    && !b.getOwner().equals(UserManager.getUserManager().getCurrentUser().getUsername())
                    && b.isVisible()) {
                filteredBooks.add(entry.getValue());
            }
        }

        return filteredBooks;
    }

    public void postBookForExchange(String bookName, String isbn, String author, String genre, int pageCount, List<String> tags, String owner){

        String bookKey = booksRef.push().getKey();
        Book book = new Book(bookKey, bookName, isbn, author, genre, pageCount, tags, false, owner);

        // Add book to map, save to firebase
        booksMap.put(bookKey, book);
        saveToFirebase();
    }


    public void postBookForSale(String bookName, String isbn, String author, String genre, int pageCount, List<String> tags, String owner){
        String bookKey = booksRef.push().getKey();
        Book book = new Book(bookKey, bookName, isbn, author, genre, pageCount, tags, true, owner);

        // Add book, update the firebase database
        booksMap.put(bookKey, book);
        saveToFirebase();
    }





}

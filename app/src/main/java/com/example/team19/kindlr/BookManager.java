package com.example.team19.kindlr;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class BookManager {

    // Book ID to Book object
    public Map<Integer, Book> booksMap;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private DatabaseReference booksRef;

    // Singleton logic
    private static BookManager bookManagerSingleton;
    public static BookManager getBookManager() {
        if (bookManagerSingleton == null)
            bookManagerSingleton = new BookManager;
        return bookManagerSingleton;
    }

    // BookManager constructor
    public BookManager() {
        booksMap = new HashMap<Integer, Book>();
        database  = FirebaseDatabase.getInstance();
        ref = database.getReference("books");
        booksRef = ref.child("books");
    }

    // Get book with given ID
    public Book getBookByID(int bookID) {
        if (!booksMap.containsKey(bookID))
            return null;
        return booksMap.get(bookID);
    }

    // TODO:
    // Logic for adding books (how to generate bookID?) - postBookExchange/Sale()
    // Searching for books given a certain BookFilter - getFilteredBooks()
    // refreshBooks() - read from DB
    // see design doc

}

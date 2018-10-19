package com.example.team19.kindlr;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            bookManagerSingleton = new BookManager();
        return bookManagerSingleton;
    }

    // BookManager constructor
    public BookManager() {
        booksMap = new HashMap<Integer, Book>();
        database  = FirebaseDatabase.getInstance();
        ref = database.getReference("books");
        booksRef = ref.child("books");
        refreshBooks(); // pull from DB
    }

    // Get book with given ID
    public Book getBookByID(int bookID) {
        if (!booksMap.containsKey(bookID))
            return null;
        return booksMap.get(bookID);
    }

    // Refresh books from DB/Firebase
    public void refreshBooks() {
        // TODO: pull from DB
    }

    // TODO:
    // Logic for adding books (how to generate bookID?) - postBookExchange/Sale()
    // Searching for books given a certain BookFilter - getFilteredBooks()
    // refreshBooks() - read from DB
    // see design doc

    public List<Book> getFilteredBooks(String searchString){
        List<Book> filteredBooks = new ArrayList();

        for (Map.Entry<Integer, Book> entry : booksMap.entrySet()) {
            Integer key = entry.getKey();
            Book book = entry.getValue();

            if(book.getAuthor().equals(searchString)){
                filteredBooks.add(book);
            }
            else if(book.getBookName().equals(searchString)){
                filteredBooks.add(book);
            }

            else if(book.getGenre().equals(searchString)){
                filteredBooks.add(book);
            }
            else if(book.getIsbn().equals(searchString)){
                filteredBooks.add(book);
            }
            for(int i =0; i < book.getTags().size();i++){
                if(book.getTags().get(i).equals(searchString)){
                    filteredBooks.add(book);
                    break;
                }
            }

        }
        return filteredBooks;
    }



}

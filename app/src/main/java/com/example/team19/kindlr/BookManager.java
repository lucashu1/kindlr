package com.example.team19.kindlr;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        refreshBooks(); // pull from DB
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

    // Refresh books from DB/Firebase
    public void refreshBooks() {
        // TODO: pull from DB
    }

    // TODO:
    // refreshBooks() - read from DB
    // see design doc

    public List<Book> getFilteredBooks(BookFilter bookFilter){
        List<Book> filteredBooks = new ArrayList();

        for (Map.Entry<String, Book> entry : booksMap.entrySet()) {
            if (bookFilter.isMatch(entry.getValue())) {
                filteredBooks.add(entry.getValue());
            }
        }

        return filteredBooks;
    }

    public void postBookForExchange(String bookName, String isbn, String author, String genre, int pageCount, List<String> tags, String owner){
//        Integer maxKey = 0;
        //iterate through keys and find max key, that will be key for new book
//        for (Integer key : booksMap.keySet()) {
//            if(key > maxKey){
//                maxKey = key;
//            }
//            // ...
//        }

        String bookKey = booksRef.push().getKey();
        Book book = new Book(bookKey, bookName, isbn, author, genre, pageCount, tags, false, owner);

        // Add book to map, save to firebase
        booksMap.put(bookKey, book);
        saveToFirebase();
    }


    public void postBookForSale(String bookName, String isbn, String author, String genre, int pageCount, List<String> tags, String owner){

//        //find the book in the map
//        for (Map.Entry<String, Book> entry : booksMap.entrySet()) {
//            Book currBook = entry.getValue();
//            String bookKey = entry.getKey();
//            if(currBook.getBookId().equals(book.getBookId())){
//                currBook.setForSale(true);
//                booksMap.put(bookKey, currBook);
//                break;
//            }
//
//        }

        String bookKey = booksRef.push().getKey();
        Book book = new Book(bookKey, bookName, isbn, author, genre, pageCount, tags, true, owner);

        // Add book, update the firebase database
        booksMap.put(bookKey, book);
        saveToFirebase();
    }





}

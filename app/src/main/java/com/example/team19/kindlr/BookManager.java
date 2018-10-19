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
    private DatabaseReference ref;
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
        ref = database.getReference("books");
//        booksRef = ref.child("books");
        refreshBooks(); // pull from DB
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
    // Logic for adding books (how to generate bookID?) - postBookExchange/Sale()
    // Searching for books given a certain BookFilter - getFilteredBooks()
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

    public void postBookExchange(Book book){
//        Integer maxKey = 0;
        //iterate through keys and find max key, that will be key for new book
//        for (Integer key : booksMap.keySet()) {
//            if(key > maxKey){
//                maxKey = key;
//            }
//            // ...
//        }

        String bookKey = ref.push().getKey();

        booksMap.put(bookKey, book);

        //update the firebase database now
        Map<String, Map<String, Book>> dataUpdates = new HashMap<>();
        dataUpdates.put("books", booksMap);
        ref.setValue(dataUpdates);


    }


    public void postBookSale(Book book){

        //find the book in the map
        for (Map.Entry<String, Book> entry : booksMap.entrySet()) {
            Book currBook = entry.getValue();
            String bookKey = entry.getKey();
            if(currBook.getBookId().equals(book.getBookId())){
                currBook.setForSale(true);
                booksMap.put(bookKey, currBook);
                break;
            }

        }

        //update the firebase database
        Map<String, Map<String, Book>>  dataUpdates= new HashMap<>();
        dataUpdates.put("books", booksMap);
        ref.setValue(dataUpdates);







    }





}

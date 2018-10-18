package com.example.team19.kindlr;

import java.util.List;

public class Book {
    private int bookId;
    private String bookName;
    private String isbn;
    private String author;
    private String genre;
    private int pageCount;
    private List<String> tags;

    public Book(int bookId, String bookName, String isbn, String author, String genre, int pageCount, List<String> tags){
        this.author = author;
        this.bookId = bookId;
        this.isbn = isbn;
        this.genre = genre;
        this.pageCount = pageCount;
        this.tags = tags;
    }

    public int getBookId(){
        return bookId;
    }

    public String getBookName(){
        return bookName;
    }

    public String getIsbn(){
        return isbn;
    }

    public String getAuthor(){
        return author;
    }

    public String getGenre(){
        return genre;
    }

    public int getPageCount(){
        return pageCount;
    }

    public List<String> getTags(){
        return tags;
    }
}

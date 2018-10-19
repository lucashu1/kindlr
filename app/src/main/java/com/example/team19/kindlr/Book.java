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
    private boolean forSale;
    private String owner;

    public Book(int bookId, String bookName, String isbn, String author, String genre, int pageCount, List<String> tags, boolean forSale, String owner){
        this.author = author;
        this.bookId = bookId;
        this.isbn = isbn;
        this.genre = genre;
        this.pageCount = pageCount;
        this.tags = tags;
        this.forSale = forSale;
        this.owner = owner;
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

    public boolean getForSale(){
        return forSale;
    }

    public void setForSale(sale){
        forSale = sale;
    }

    public String getOwner(){
        return owner;
    }
}

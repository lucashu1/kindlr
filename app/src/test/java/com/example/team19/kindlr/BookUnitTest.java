package com.example.team19.kindlr;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class BookUnitTest {

    List<String> tags = new ArrayList<>();

    Book testBook = new Book("","TestBook", "978-3-16-148410-0", "TestAuthor","TestGenre",100, tags,true, "TestOwner");

    @Test
    public void testBookInitialization(){
        assertEquals(testBook.getAuthor(), "TestAuthor");
        assertEquals(testBook.getBookName(), "TestBook");
        assertEquals(testBook.getIsbn(), "978-3-16-148410-0");
        assertEquals(testBook.getGenre(), "TestGenre");
        assertEquals(testBook.getOwner(),"TestOwner");
        assertEquals(testBook.getPageCount(),100);
        assertEquals(testBook.getForSale(),true);
    }
}

package com.example.team19.kindlr;

public class BookFilter {
    private String searchText;
    private String author;
    private String genre;
    private String tag;

    public BookFilter(String searchText)
    {
        this.searchText = searchText;
    }

    public String getSearchText()
    {
        return searchText;
    }

    public void setSearchText(String s)
    {
        searchText = s;
    }

    public String getAuthor()
    {
        return author
    }

    public void setAuthor(String a)
    {
        author = a;
    }

    public String getGenres()
    {
        return genre;
    }

    public void setGenre(String g)
    {
        genre = g;
    }

    public String getTag()
    {
        return tag;
    }

    public void setTag(String t)
    {
        tag = t;
    }
}

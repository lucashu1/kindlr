package com.example.team19.kindlr;

public class BookFilter {
    private String searchText;
    private String author;
    private String genre;
    private String tag;

    public BookFilter() {
        this(null);
    }

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
        return author;
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

    public boolean isMatch(Book book) {
        if (getSearchText() != null) {
            if (book.getAuthor().contains(getSearchText())) {
                return true;
            } else if (book.getBookName().contains(getSearchText())) {
                return true;
            } else if (book.getGenre().contains(getSearchText())) {
                return true;
            } else if (book.getIsbn().contains(getSearchText())) {
                return true;
            }

            for (int i = 0; i < book.getTags().size(); i++) {
                if (book.getTags().get(i).contains(getSearchText())) {
                    return true;
                }
            }
        }

        return false;
    }
}

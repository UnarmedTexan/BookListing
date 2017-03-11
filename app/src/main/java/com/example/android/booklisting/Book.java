package com.example.android.booklisting;

/**
 * Created by Mark on 3/3/2017.
 */

public class Book {

    // String for book title
    private String mBookTitle;

    // String for book author
    private String mAuthors;

    // String for book description
    private String mDescription;

    // Website for book details
    private String mUrl;

    // Website for book cover
    private String mBookCover;

    public Book(String bookTitle, String authors, String bookDescription, String url, String bookCover){
        mBookTitle = bookTitle;
        mAuthors = authors;
        mDescription = bookDescription;
        mUrl = url;
        mBookCover = bookCover;
    }

    public String getBookTitle() {
        return mBookTitle;
    }

    public String getAuthor() {
        return mAuthors;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getBookCover(){
        return mBookCover;
    }
}

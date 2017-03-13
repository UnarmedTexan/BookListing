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

    /**
     * Build a new {@link Book} object
     *
     * @param bookTitle       is the book title
     * @param authors         is author(s) for a given book
     * @param bookDescription is the description for a given book
     * @param url             is the link to website with detailed information for a given book
     * @param bookCover       link to thumbnail of a book cover for a given book
     */
    public Book(String bookTitle, String authors, String bookDescription, String url, String bookCover){
        mBookTitle = bookTitle;
        mAuthors = authors;
        mDescription = bookDescription;
        mUrl = url;
        mBookCover = bookCover;
    }

    // Get the title of a particular book.
    public String getBookTitle() {
        return mBookTitle;
    }

    // Get the author(s) of a particular book.
    public String getAuthor() {
        return mAuthors;
    }

    // Get the description of a particular book.
    public String getDescription() {
        return mDescription;
    }

    // Get the link to website containing more information on a particular book.
    public String getUrl() {
        return mUrl;
    }

    // Get the thumbnail image used as the book cover for a particular book.
    public String getBookCover(){
        return mBookCover;
    }
}

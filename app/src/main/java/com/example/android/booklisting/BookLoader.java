package com.example.android.booklisting;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Mark on 3/5/2017.
 */


// Loads a list of book objects by using an AsyncTaskLoader to perform a network request to a
// specified Url, which includes user supplied search criteria
public class BookLoader extends AsyncTaskLoader<List<Book>> {

    //Tag for log messages
    private static final String LOG_TAG = Book.class.getName();

    // Search URL
    private String mUrl;

    /**
     * Constructs a new {@link BookLoader}.
     * @param context of the activity
     * @param url to load data from
     */
    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Book> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of books.
        List<Book> books = SearchUtils.fetchBookData(mUrl);
        return books;
    }
}

package com.example.android.booklisting;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final String LOG_TAG = BookActivity.class.getName();
    // Constant value for the book loader ID. This is required if multiple loaders are being used.
    private static final int BOOK_LOADER_ID = 1;
    // Main portion of the Url for the book data requested from the Google Books API
    private static String googleBooks =
            "https://www.googleapis.com/books/v1/volumes?maxResults=10&q=";
    // Adapter for the list of books
    private BookAdapter mAdapter;

    // When the list is empty, this is the TextView to be displayed.
    private TextView mEmptyStateView;

    // While waiting for the app to retrieve data, this is the Progress spinner to be displayed.
    private ProgressBar loadingData;

    // Initialize the ImageView used as the clickable icon to start the search.
    private ImageView termSearch;

    // Initialize the String object used to hold the user entered search criteria.
    private String searchTerm;

    // Initialize the ListView used to display a list of books associated with the search criteria.
    private ListView bookListView;

    // Get a reference to the LoaderManager, in order to interact with loaders.
    private LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books_activity);

        // Find the item touched by the user to initiate a book search
        termSearch = (ImageView) findViewById(R.id.subject_search);
        // Find the reference to the {@link ListView} in the layout
        bookListView = (ListView) findViewById(R.id.list);

        // Set the progress spinner to the Progress Bar view in books_activity.xml
        loadingData = (ProgressBar) findViewById(R.id.loading_progress);
        // Make the progress spinner invisible
        loadingData.setVisibility(View.INVISIBLE);

        // Initialize the LoaderManager.
        loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(BOOK_LOADER_ID, null, BookActivity.this).forceLoad();

        // Find the EditText view where the user enters search criteria
        final EditText subjectEntered = (EditText) findViewById(R.id.subject_text);

        //Create a new book adapter which take an empty list of books as input
        mAdapter = new BookAdapter(BookActivity.this, new ArrayList<Book>());
        // Set the adapter on the {@link ListView}, so the list can be filled with books
        // in the user interface.
        bookListView.setAdapter(mAdapter);

        // View to be displayed when no list items are available.
        mEmptyStateView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateView);

        termSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                // Convert EditText input to the String searchTerm
                searchTerm = subjectEntered.getText().toString();

                // Verify a search term has been entered
                if (searchTerm == null || searchTerm.equals("")) {
                    // Inform user no search term has been entered
                    Toast.makeText(BookActivity.this, getString(R.string.no_search_term),
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Hide keyboard once the search icon is touched
                    InputMethodManager inputMethodManager = (InputMethodManager)
                            getSystemService(BookActivity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(subjectEntered.getWindowToken(), 0);

                    // Find the Progress spinner & make it visible to the app user
                    loadingData.setVisibility(View.VISIBLE);

                    // Create a reference to ConnectivityManager & check for network connectivity
                    ConnectivityManager cm =
                            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                    // Check for a network connection and begin the process of fetching data
                    if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                        // Create a reference to the LoaderManager to interact with the Loader(s).
                        LoaderManager loaderManager = getSupportLoaderManager();

                        // Loader reset - this is to clear out any existing book data.
                        loaderManager.restartLoader(BOOK_LOADER_ID, null, BookActivity.this).
                                forceLoad();
                    } else {
                        // Hide the progress spinner
                        loadingData.setVisibility(View.GONE);
                        // Inform the user there is "No network connection"
                        mEmptyStateView.setText(R.string.no_connection);
                    }
                }
            }
        });

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // Locate the current book being clicked on by the user
                Book currentBook = mAdapter.getItem(position);

                // Prepare the the String URL to be passed into the intent constructor by first
                // converting it into a URL object.
                Uri bookUri = Uri.parse(currentBook.getUrl());

                // Generate a new intent to view the book URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // Use the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for a user provided search request
        if (searchTerm == null) {
            return new Loader<List<Book>>(this);
        } else {
            // remove spaces from user search entry
            searchTerm = searchTerm.replace(" ", "%20");
        }
        // Generate a new loader for a particular URL
        return new BookLoader(this, googleBooks + searchTerm);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        // Set TextView ID empty_view to display "No books found."
        mEmptyStateView.setText(R.string.no_books);

        // Set Progress spinner to invisible
        loadingData.setVisibility(View.INVISIBLE);

        //Clear the adapter of any previous book search results
        mAdapter.clear();

        // If there is a valid list of {@link Books}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset - this is to clear out any existing book data.
        mAdapter.clear();
    }
}

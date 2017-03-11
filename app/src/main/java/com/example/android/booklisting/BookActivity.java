package com.example.android.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

    // Main portion of the Url for the book data requested from the Google Books API
    private static final String googleBooks =
            "https://www.googleapis.com/books/v1/volumes?maxResults=10&q=";

    // Constant value for the book loader ID. This is required if multiple loaders are being used.
    private static final int BOOK_LOADER_ID = 1;

    // Adapter for the list of books
    private BookAdapter mAdapter;

    // When the list is empty, this is the TextView to be displayed.
    private TextView mEmptyStateView;

    // While waiting for the app to retrieve data, this is the Progress spinner to be displayed.
    private ProgressBar loadingData;


    private ImageView termSearch;

    private String searchTerm;

    private ListView bookListView;

    // Get a reference to the LoaderManager, in order to interact with loaders.
    private LoaderManager loaderManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books_activity);

        termSearch = (ImageView) findViewById(R.id.subject_search);
        // Find the reference to the {@link ListView} in the layout
        bookListView = (ListView) findViewById(R.id.list);
        loadingData = (ProgressBar) findViewById(R.id.loading_progress);
        loadingData.setVisibility(View.INVISIBLE);

        // Initialize the loader. Pass in the int ID constant defined above and pass
        // in null for the bundle. Pass in this activity for the LoaderCallbacks
        // parameter (which is valid because this activity implements the
        // LoaderCallbacks interface).
        loaderManager = getLoaderManager();
        loaderManager.initLoader(BOOK_LOADER_ID, null, BookActivity.this);
        final EditText subjectEntered = (EditText) findViewById(R.id.subject_text);
        mEmptyStateView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateView);

        //Create a new book adapter which take an empty list of books as input
        mAdapter = new BookAdapter(BookActivity.this, new ArrayList<Book>());

        termSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // Loader reset - this is to clear out any existing book data.
                searchTerm = subjectEntered.getText().toString();

                if (searchTerm == null || searchTerm.equals("")) {
                    Toast.makeText(BookActivity.this, getString(R.string.no_search_term),
                            Toast.LENGTH_SHORT).show();
                }else{
                    // Find the Progress spinner & make it visible to the app user
                    loadingData.setVisibility(View.VISIBLE);

                    // Set the adapter on the {@link ListView}, so the list can be filled with books
                    // in the user interface.
                    bookListView.setAdapter(mAdapter);


                    ConnectivityManager cm =
                            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                    if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
                        LoaderManager loaderManager = getLoaderManager();

                        // Restarts the loader.
                        loaderManager.restartLoader(BOOK_LOADER_ID, null, BookActivity.this);
                    }
                    else{
                        loadingData.setVisibility(View.GONE);
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
    public Loader<List<Book>>onCreateLoader(int i, Bundle bundle){

        Log.v(LOG_TAG, "Our current URL is : " + googleBooks);

        // Generate a new loader for a particular URL
        return new BookLoader(this, googleBooks + searchTerm);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books){

        Log.v(LOG_TAG, "On load finished Books is :" + books);

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

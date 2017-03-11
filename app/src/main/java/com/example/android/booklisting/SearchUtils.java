package com.example.android.booklisting;

/**
 * Created by Mark on 3/4/2017.
 */


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods needed for requesting and receiving book information from GoogleBooks API.
 */
public class SearchUtils {

    //Tag for the log messages
    public static final String LOG_TAG = SearchUtils.class.getSimpleName();

    /**
     * Search the GoogleBooksAPI and return an {@link Book} object to represent a book
     */
    public static List<Book> fetchBookData(String requestUrl) {
        // Genereate URL object
        URL url = createURL(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        //Retrieve relevant fields from the JSON response and create an {@link Book} object
        List<Book> books = extractBookInfoFromJson(jsonResponse);

        // return the {@link Book}
        return books;
    }

    private static URL createURL (String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Book} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<Book> extractBookInfoFromJson(String booksJSON) {

        // Create an empty ArrayList for the purpose of adding books
        List<Book> books = new ArrayList<>();

        // Try to parse the JSON response. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create JSONObject from booksJSON string.
            JSONObject baseJsonResponse = new JSONObject(booksJSON);

            // Extract the JSONArray associated with the key called "items",
            // which represents a list of books.
            JSONArray booksArray = baseJsonResponse.getJSONArray("items");

            // Create an {@link Book} object in the booksArray for each book.
            for (int i = 0; i < booksArray.length(); i++){

                // Get the book at position i within the list of books
                JSONObject currentBook = booksArray.getJSONObject(i);

                // For a given book, extract the JSONObject associated with the
                // key called "volumeInfo", which represents a information associated with the
                // links for a the given book.
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                // Extract the value for the key called "title"
                String bookTitle = volumeInfo.optString("title");

                // Extract the value for the key called "infoLink", which is the GoogleBooks API url
                // for the given book
                String url = volumeInfo.optString("canonicalVolumeLink");

                // Extract the value for the key called "description"
                String bookDescription = volumeInfo.optString("description");
                // Trim the bookDescription if it's length is over 150 characters
                    if (bookDescription.length() > 150){
                        bookDescription = bookDescription.substring(0, 150) + "...";
                    }

                // Extract the value for the key called "authors", which is the author(s)
                // for the given book
                String authors;
                if (volumeInfo.has("authors")){
                    JSONArray authorsJsonArray = volumeInfo.getJSONArray("authors");

                    // Set the first author in the JSONarray to the String authors object.
                    authors = authorsJsonArray.optString(0);

                    // Set String authors object based on the number of authors for a given book
                    for (int j = 0; j < authorsJsonArray.length(); j++){
                        if (authorsJsonArray.length() == 1) {
                            // if only one book author
                            authors = authorsJsonArray.optString(j) + " ";
                        } else if (authorsJsonArray.length() > 1) {
                            // if more than one author
                            authors = authors + ", " + authorsJsonArray.optString(j);
                        }
                    }
                } else {
                    // if no author is listed.
                    authors = "No authors listed.";
                }

                // For a given book, extract the JSONObject associated with the
                // key called "imageLinks", which represents the url for a small thumbnail of the
                // book cover.
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");

                // Save the thumbnail url as a String object
                String smallThumbnail = imageLinks.optString("smallThumbnail");

                // Create a new {@link Book} object with the book title, author(s), description of
                // the book, url linking to further book details found at Google Books, and a small
                // image of the book cover.  Add the new {@link Book} to the list of books.
                books.add(new Book(bookTitle, authors, bookDescription, url, smallThumbnail));
            }
        } catch (JSONException e){
            Log.e ("SearchUtils", "Problem parsing the book JSON results", e);
        }
        // return the list of books
        return books;
    }
}

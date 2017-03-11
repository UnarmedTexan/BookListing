package com.example.android.booklisting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mark on 3/4/2017.
 */

public class BookAdapter extends ArrayAdapter<Book>{

    private static final String LOG_TAG = BookAdapter.class.getSimpleName();

    public BookAdapter(Context context, ArrayList<Book> booksInfo){
        super(context, 0, booksInfo);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_books, parent, false);
        }
        //get the {@link Book} object for this list position
        Book currentBook = getItem(position);

        //Find the ImageView for the list_books.xml layout with the ID book_cover.
        ImageView bookCover = (ImageView) listItemView.findViewById(R.id.book_cover);

        // Create String object of the URL for the book cover thumbnail.
        String bookCoverUrl = currentBook.getBookCover();

        // Load the book cover url string object to the Book ImageView for ID book_title.
        Picasso.with(getContext()).load(bookCoverUrl).fit().centerCrop().
                placeholder(R.drawable.beach).error(R.drawable.beach).into(bookCover);

        //Find the TextView for the list_books.xml layout with the ID book_title.
        TextView bookTitleView = (TextView) listItemView.findViewById(R.id.book_title);

        // Get the book title from the currentBook object and set this text on
        // the Book TextView for ID book_title.
        bookTitleView.setText(currentBook.getBookTitle());

        //Find the TextView for the list_books.xml layout with the ID book_author.
        TextView bookAuthorView = (TextView) listItemView.findViewById(R.id.book_author);

        // Get the book author from the currentBook object and set this text on
        // the Book TextView for ID book_author.
        bookAuthorView.setText(currentBook.getAuthor());

        //Find the TextView for the list_books.xml layout with the ID book_description.
        TextView bookDescriptionView = (TextView) listItemView.findViewById(R.id.book_description);

        // Get the book author from the currentBook object and set this text on
        // the Book TextView for ID book_description.
        bookDescriptionView.setText(currentBook.getDescription());

        return listItemView;
    }
}

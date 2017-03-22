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

// ArrayAdapter used to to provide the layout for list of Book objects based on user entered
// search criteria.
public class BookAdapter extends ArrayAdapter<Book> {

    private static final String LOG_TAG = BookAdapter.class.getSimpleName();


    public BookAdapter(Context context, ArrayList<Book> booksInfo) {
        super(context, 0, booksInfo);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // initialize viewHolder
        ViewHolder viewHolder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_books, parent, false);

            //Find the ImageView for the list_books.xml layout with the ID book_cover.
            viewHolder.bookCover = (ImageView) convertView.findViewById(R.id.book_cover);

            //Find the TextView for the list_books.xml layout with the ID book_title.
            viewHolder.bookTitleView = (TextView) convertView.findViewById(R.id.book_title);

            //Find the TextView for the list_books.xml layout with the ID book_author.
            viewHolder.bookAuthorView = (TextView) convertView.findViewById(R.id.book_author);

            //Find the TextView for the list_books.xml layout with the ID book_description.
            viewHolder.bookDescriptionView = (TextView)
                    convertView.findViewById(R.id.book_description);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //get the {@link Book} object for this list position
        Book currentBook = getItem(position);

        // Get the ListView and set as ID list_bucket
        viewHolder.textContainer = (View) convertView.findViewById(R.id.list_bucket);

        // Get the book title from the currentBook object and set this text on
        // the Book TextView for ID book_title.
        viewHolder.bookTitleView.setText(currentBook.getBookTitle());

        // Get the book author from the currentBook object and set this text on
        // the Book TextView for ID book_author.
        viewHolder.bookAuthorView.setText(currentBook.getAuthor());

        // Get the book author from the currentBook object and set this text on
        // the Book TextView for ID book_description.
        viewHolder.bookDescriptionView.setText(currentBook.getDescription());

        // Create String object of the URL for the book cover thumbnail.
        String bookCoverUrl = currentBook.getBookCover();

        // Load the book cover url string object to the Book ImageView for ID book_title.
        Picasso.with(getContext()).load(bookCoverUrl).fit().centerCrop().placeholder
                (R.drawable.no_cover).error(R.drawable.no_cover).into(viewHolder.bookCover);

        return convertView;
    }

    private static class ViewHolder {
        ImageView bookCover;
        TextView bookTitleView;
        TextView bookAuthorView;
        TextView bookDescriptionView;
        View textContainer;
    }
}

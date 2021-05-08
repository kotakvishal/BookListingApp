package com.example.booklistingapp;

public class Book {
    private String mAuthors;
    private String mTitle;

    public Book(String writers, String book_name) {
        mAuthors = writers;
        mTitle = book_name;
    }

    public String getAuthor() {
        return mAuthors;
    }

    public String getTitle() {
        return mTitle;
    }
}

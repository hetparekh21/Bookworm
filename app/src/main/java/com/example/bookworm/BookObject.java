package com.example.bookworm;

import android.graphics.Bitmap;

public class BookObject {

    private String title;
    private String publisher;
    private String publishedDate;
    private String description;
    private String authors;
    private String subtitle ;
    private Bitmap BookThumbnail;

    public BookObject(Bitmap BookThumbnail ,String title , String publisher , String publishedDate , String description , String authors , String Subtitle){

        this.authors = authors ;
        this.BookThumbnail = BookThumbnail ;
        this.description = description ;
        this.publisher = publisher ;
        this.title = title ;
        this.publishedDate = publishedDate ;
        this.subtitle = Subtitle ;

    }

    public BookObject(Bitmap BookThumbnail ,String title , String publisher , String publishedDate , String description , String authors){

        this.authors = authors ;
        this.BookThumbnail = BookThumbnail ;
        this.description = description ;
        this.publisher = publisher ;
        this.title = title ;
        this.publishedDate = publishedDate ;

    }

    public Bitmap getBookThumbnail() {
        return BookThumbnail;
    }

    public String getAuthors() {
        return authors;
    }

    public String getDescription() {
        return description;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getTitle() {
        return title;
    }

}

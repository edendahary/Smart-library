package com.example.edenproject;

import android.graphics.Bitmap;

import java.io.Serializable;

public class BookItem implements Serializable {
    private String Name,AuthorName,Category,PublicationDate,Description,Uri;
    private int quantity;

    public BookItem(){

    }
    public BookItem(String name, int quantity) {
        Name = name;
        this.quantity = quantity;
    }

    public BookItem(String name, String authorName, String category, String publicationDate, String description, int pages,String uri) {
        Name = name;
        AuthorName = authorName;
        Category = category;
        PublicationDate = publicationDate;
        Description = description;
        Pages = pages;
        quantity = 1;
        this.Uri = uri;
    }

    public String getUri() {
        return Uri;
    }

    public void setUri(String uri) {
        Uri = uri;
    }

    public String getAuthorName() {
        return AuthorName;
    }

    public void setAuthorName(String authorName) {
        AuthorName = authorName;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getPublicationDate() {
        return PublicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        PublicationDate = publicationDate;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getPages() {
        return Pages;
    }

    public void setPages(int pages) {
        Pages = pages;
    }

    private int Pages;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    @Override
    public String toString() {
        return "BookItem{" +
                "Name='" + Name + '\'' +
                ", AuthorName='" + AuthorName + '\'' +
                ", Category='" + Category + '\'' +
                ", PublicationDate='" + PublicationDate + '\'' +
                ", Description='" + Description + '\'' +
                ", Uri='" + Uri + '\'' +
                ", quantity=" + quantity +
                ", Pages=" + Pages +
                '}';
    }
}

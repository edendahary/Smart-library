package com.example.edenproject;

import android.graphics.Bitmap;

public class BookItem {
    private String Name;
    private int quantity;

    public BookItem(){

    }
    public BookItem(String name, int quantity) {
        Name = name;
        this.quantity = quantity;
    }



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
}

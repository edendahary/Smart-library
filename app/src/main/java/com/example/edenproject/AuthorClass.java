package com.example.edenproject;

import java.util.ArrayList;

public class AuthorClass extends UserClass{
    private int Wallet;
    private String UidAuthor;


    public AuthorClass(String full_name, String email,String dob, String phone, String gender,String UID) {
        super(full_name,email,dob,phone,gender);
        this.Wallet = 0;
        this.UidAuthor = UID;
    }

    public AuthorClass(String full_name, String email,String dob, String phone, String gender,int wallet) {
        super(full_name,email,dob,phone,gender);
        this.Wallet = wallet;
    }

    public AuthorClass(){

    }

    public int getWallet() {
        return Wallet;
    }

    public void setWallet(int wallet) {
        Wallet = wallet;
    }
}

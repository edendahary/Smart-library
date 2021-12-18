package com.example.edenproject;

import java.util.ArrayList;

public class UserClass {
    private String full_name;
    private String email;
    private String dob;
    private String phone;
    private String gender;

    public UserClass(){

    }
    public UserClass(String full_name, String email,String dob, String phone, String gender) {
        this.full_name = full_name;
        this.email = email;
        this.dob = dob;
        this.phone = phone;
        this.gender=gender;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}

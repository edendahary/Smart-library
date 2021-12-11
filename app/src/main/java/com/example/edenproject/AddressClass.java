package com.example.edenproject;

public class AddressClass {
    private String County,City,Street,Postal_Code;

    public AddressClass(){

    }
    public AddressClass(String county, String city, String street, String postal_Code) {
        County = county;
        City = city;
        Street = street;
        Postal_Code = postal_Code;
    }

    public String getCounty() {
        return County;
    }

    public void setCounty(String county) {
        County = county;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getPostal_Code() {
        return Postal_Code;
    }

    public void setPostal_Code(String postal_Code) {
        Postal_Code = postal_Code;
    }
}

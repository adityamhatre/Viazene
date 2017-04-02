package com.thelogicalcoder.viazene.Application;

import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * Created by Aditya on 015, 15 July 2015.
 */
public class UserInfo {
    private String email, dob, address, contact, pincode, user, dpURL;
    private List<String> cartProducts;
    private Drawable dp;

    public List<String> getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(List<String> cartProducts) {
        this.cartProducts = cartProducts;
    }

    public Drawable getDp() {
        return dp;
    }

    public void setDp(Drawable dp) {
        this.dp = dp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDpURL() {
        return dpURL;
    }

    public void setDpURL(String dpURL) {
        this.dpURL = dpURL;
    }
}

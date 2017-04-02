package com.thelogicalcoder.viazene.Application;

import java.util.List;

/**
 * Created by Aditya on 027, 27 July 2015.
 */
public class UserProfile {
    private List<UserMatch> userMatch;
    private List<String> userMatchNameList;
    private List<String> userCart;
    private List<String> userService;
    private List<UserProductBought> userBought;

    public List<String> getUserMatchNameList() {
        return userMatchNameList;
    }

    public void setUserMatchNameList(List<String> userMatchNameList) {
        this.userMatchNameList = userMatchNameList;
    }

    public List<UserMatch> getUserMatch() {
        return userMatch;
    }

    public void setUserMatch(List<UserMatch> userMatch) {
        this.userMatch = userMatch;
    }


    public List<String> getUserCart() {
        return userCart;
    }

    public void setUserCart(List<String> userCart) {
        this.userCart = userCart;
    }

    public List<UserProductBought> getUserBought() {
        return userBought;
    }

    public void setUserBought(List<UserProductBought> userBought) {
        this.userBought = userBought;
    }

    public List<String> getUserService() {
        return userService;
    }

    public void setUserService(List<String> userService) {
        this.userService = userService;
    }
}

package com.thelogicalcoder.viazene.Data;

/**
 * Created by Aditya on 010, 10 July 2015.
 */
public class WallCommentData {
    String user, wallID, comment, email, id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getWallID() {
        return wallID;
    }

    public void setWallID(String productID) {
        this.wallID = productID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

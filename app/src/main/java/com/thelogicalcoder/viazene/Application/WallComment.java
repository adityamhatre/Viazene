package com.thelogicalcoder.viazene.Application;

/**
 * Created by Aditya on 007, 07 August 2015.
 */
public class WallComment {
    private String wallID, comment, user;

    public WallComment(String wallID, String comment, String user) {
        this.wallID = wallID;
        this.comment = comment;
        this.user = user;
    }

    public String getWallID() {
        return wallID;
    }

    public String getComment() {
        return comment;
    }

    public String getUser() {
        return user;
    }
}

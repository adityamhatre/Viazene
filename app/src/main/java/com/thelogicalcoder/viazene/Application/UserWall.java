package com.thelogicalcoder.viazene.Application;


import java.util.List;

/**
 * Created by Aditya on 027, 27 July 2015.
 */
public class UserWall {
    private List<String> likeList;
    private List<WallComment> commentList;

    public List<String> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<String> likeList) {
        this.likeList = likeList;
    }

    public List<WallComment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<WallComment> commentList) {
        this.commentList = commentList;
    }
}

package com.thelogicalcoder.viazene.JSONParsers;

import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Application.UserWall;
import com.thelogicalcoder.viazene.Application.WallComment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aditya on 019, 19 July 2015.
 */
public class UserWallParser {
    public UserWallParser(String response) {
        parseResponse(response);
    }

    void parseResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray likesArray = jsonObject.getJSONArray("likes");
            JSONArray commentsArray = jsonObject.getJSONArray("comments");


            List<String> likes = new ArrayList<>();
            {
                for (int i = 0; i < likesArray.length(); i++) {
                    JSONObject userLikeOBJ = (JSONObject) likesArray.get(i);
                    likes.add(userLikeOBJ.getString("wallID"));
                }
            }

            List<WallComment> comments = new ArrayList<>();
            {
                for (int i = 0; i < commentsArray.length(); i++) {
                    JSONObject userCommentOBJ = (JSONObject) commentsArray.get(i);
                    comments.add(new WallComment(userCommentOBJ.getString("wallID"), userCommentOBJ.getString("comment"),userCommentOBJ.getString("user")));
                }
            }


            UserWall userWall = new UserWall();
            userWall.setLikeList(likes);
            userWall.setCommentList(comments);

            AppController.getInstance().setUserWall(userWall);
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("UWP");
            AppController.getInstance().setUserWall(null);
        }

    }
}

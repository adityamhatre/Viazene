package com.thelogicalcoder.viazene.JSONParsers;

import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Data.WallData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Aditya on 012, 12 July 2015.
 */
public class WallParser {

    public WallParser(String response) {
        parseResponse(response);
    }

    void parseResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray productArray = jsonObject.getJSONArray("wall");
            List<WallData> wallDataList = new ArrayList<>();
            for (int i = 0; i < productArray.length(); i++) {
                JSONObject wallOBJ = (JSONObject) productArray.get(i);
                WallData wallData = new WallData();
                wallData.setWallID(wallOBJ.getString("id"));
                wallData.setUser(wallOBJ.getString("user"));
                wallData.setEmail(wallOBJ.getString("email"));
                wallData.setID(wallOBJ.getString("psID"));
                wallData.setPostType(wallOBJ.getString("postType"));
                wallData.setPostTitle(wallOBJ.getString("postTitle"));
                wallData.setLikeCount(wallOBJ.getString("likeCount"));
                wallData.setCommentCount(wallOBJ.getString("commentCount"));
                wallDataList.add(wallData);
            }
            //Collections.shuffle(productDataList);
            AppController.getInstance().setWallDataList(wallDataList);

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("WPE");
            AppController.getInstance().setWallDataList(null);
        }

    }
}

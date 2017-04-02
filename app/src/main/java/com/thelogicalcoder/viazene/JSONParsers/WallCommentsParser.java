package com.thelogicalcoder.viazene.JSONParsers;

import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Data.WallCommentData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Aditya on 012, 12 July 2015.
 */
public class WallCommentsParser {

    public WallCommentsParser(String response) {
        parseResponse(response);
    }

    void parseResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray wallComments = jsonObject.getJSONArray("wallComments");
            List<WallCommentData> wallCommentDataList = new ArrayList<>();
            for (int i = 0; i < wallComments.length(); i++) {
                JSONObject productOBJ = (JSONObject) wallComments.get(i);
                WallCommentData wallCommentData = new WallCommentData();
                wallCommentData.setUser(productOBJ.getString("user"));
                wallCommentData.setId(productOBJ.getString("id"));
                wallCommentData.setComment(productOBJ.getString("comment"));
                wallCommentData.setWallID(productOBJ.getString("wallID"));
                wallCommentData.setEmail(productOBJ.getString("email"));
                wallCommentDataList.add(wallCommentData);
            }
            //Collections.shuffle(productDataList);
            AppController.getInstance().setWallCommentDataList(wallCommentDataList);

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("WCPE");
            AppController.getInstance().setWallCommentDataList(null);
        }

    }
}

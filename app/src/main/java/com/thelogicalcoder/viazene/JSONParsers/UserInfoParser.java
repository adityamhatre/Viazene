package com.thelogicalcoder.viazene.JSONParsers;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Application.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aditya on 012, 12 July 2015.
 */
public class UserInfoParser {

    Context context;
    Drawable DP;

    public UserInfoParser(Context context, String response) {
        this.context = context;
        parseResponse(response);
    }

    void parseResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            UserInfo userInfo = new UserInfo();
            userInfo.setUser(jsonObject.getString("user"));
            userInfo.setEmail(jsonObject.getString("email"));
            userInfo.setAddress(jsonObject.getString("address"));
            userInfo.setContact(jsonObject.getString("contact"));
            userInfo.setDob(jsonObject.getString("dob"));
            userInfo.setPincode(jsonObject.getString("pincode"));
            userInfo.setDpURL(jsonObject.getString("dpURL"));
            AppController.getInstance().setUserInfo(userInfo);


        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("UIP");
            AppController.getInstance().setUserInfo(null);
        }

    }


}

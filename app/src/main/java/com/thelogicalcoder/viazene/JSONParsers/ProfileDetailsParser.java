package com.thelogicalcoder.viazene.JSONParsers;

import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Application.UserMatch;
import com.thelogicalcoder.viazene.Application.UserProductBought;
import com.thelogicalcoder.viazene.Application.UserProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aditya on 012, 12 July 2015.
 */
public class ProfileDetailsParser {

    public ProfileDetailsParser(String response) {
        parseResponse(response);
    }

    void parseResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray userCartArray = jsonObject.getJSONArray("userCart");
            JSONArray userProductBoughtArray = jsonObject.getJSONArray("userProductBought");
            JSONArray userMatchesArray = jsonObject.getJSONArray("userMatches");
            JSONArray userServiceArray = jsonObject.getJSONArray("userService");


            List<String> userCart = new ArrayList<>();
            {
                for (int i = 0; i < userCartArray.length(); i++) {
                    JSONObject userCartOBJ = (JSONObject) userCartArray.get(i);
                    userCart.add(userCartOBJ.getString("productID"));
                }
            }

            List<String> userService = new ArrayList<>();
            {
                for (int i = 0; i < userServiceArray.length(); i++) {
                    JSONObject userServiceOBJ = (JSONObject) userServiceArray.get(i);
                    userService.add(userServiceOBJ.getString("serviceID"));
                }
            }

            List<UserProductBought> userProductBoughtList = new ArrayList<>();
            {
                for (int i = 0; i < userProductBoughtArray.length(); i++) {
                    JSONObject userProductBoughtOBJ = (JSONObject) userProductBoughtArray.get(i);
                    UserProductBought userProductBought = new UserProductBought();
                    userProductBought.setProductID(userProductBoughtOBJ.getString("productID"));
                    userProductBought.setOrderID(userProductBoughtOBJ.getString("orderID"));
                    userProductBoughtList.add(userProductBought);
                }
            }

            List<UserMatch> userMatchList = new ArrayList<>();
            List<String> userMatchNameList = new ArrayList<>();
            {
                for (int i = 0; i < userMatchesArray.length(); i++) {
                    JSONObject userMatchOBJ = (JSONObject) userMatchesArray.get(i);

                    UserMatch userMatch = new UserMatch();
                    userMatch.setMatchName(userMatchOBJ.getString("matchName"));
                    userMatch.setProduct1(userMatchOBJ.getString("product1"));
                    userMatch.setProduct2(userMatchOBJ.getString("product2"));
                    userMatch.setProduct3(userMatchOBJ.getString("product3"));
                    userMatch.setProduct4(userMatchOBJ.getString("product4"));
                    userMatchNameList.add(userMatchOBJ.getString("matchName"));
                    userMatchList.add(userMatch);
                }
            }


            UserProfile userProfile = new UserProfile();
            userProfile.setUserCart(userCart);
            userProfile.setUserBought(userProductBoughtList);
            userProfile.setUserMatch(userMatchList);
            userProfile.setUserMatchNameList(userMatchNameList);
            userProfile.setUserService(userService);

            AppController.getInstance().setUserProfile(userProfile);
        } catch (JSONException e) {
            e.printStackTrace();
            AppController.getInstance().setUserProfile(null);
            System.out.println("PDP");
        }

    }
}

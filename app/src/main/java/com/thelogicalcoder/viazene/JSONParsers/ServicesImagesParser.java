package com.thelogicalcoder.viazene.JSONParsers;

import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Data.ServiceImagesData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aditya on 012, 12 July 2015.
 */
public class ServicesImagesParser {

    public ServicesImagesParser(String response) {
        parseResponse(response);
    }

    void parseResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray servicesURLS = jsonObject.getJSONArray("servicesURLS");

            List<ServiceImagesData> serviceImagesDataArrayList = new ArrayList<>();


            for (int i = 0; i < servicesURLS.length(); i++) {
                JSONObject serviceImagesOBJ = (JSONObject) servicesURLS.get(i);
                JSONArray urls = serviceImagesOBJ.getJSONArray("URLS");
                String ID = "";
                List<String> URLs = new ArrayList<>();

                ID = serviceImagesOBJ.getString("ServiceID");
                for (int j = 0; j < urls.length(); j++) {
                    URLs.add(urls.getString(j));
                }

                ServiceImagesData serviceImagesData = new ServiceImagesData();
                serviceImagesData.setServiceID(ID);
                serviceImagesData.setImageURLs(URLs);

                serviceImagesDataArrayList.add(serviceImagesData);
            }

            //Collections.shuffle(productDataList);
            AppController.getInstance().setServiceImagesDataList(serviceImagesDataArrayList);

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("SIPE");
            AppController.getInstance().setServiceImagesDataList(null);
        }

    }
}

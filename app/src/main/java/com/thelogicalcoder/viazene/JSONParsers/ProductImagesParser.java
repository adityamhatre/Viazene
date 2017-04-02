package com.thelogicalcoder.viazene.JSONParsers;

import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Data.ProductImagesData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aditya on 012, 12 July 2015.
 */
public class ProductImagesParser {

    public ProductImagesParser(String response) {
        parseResponse(response);
    }

    void parseResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray productsURLS = jsonObject.getJSONArray("productsURLS");

            List<ProductImagesData> productImagesDataArrayList = new ArrayList<>();


            for (int i = 0; i < productsURLS.length(); i++) {
                JSONObject productImageOBJ = (JSONObject) productsURLS.get(i);
                JSONArray urls = productImageOBJ.getJSONArray("URLS");
                String ID = "";
                List<String> URLs = new ArrayList<>();

                ID = productImageOBJ.getString("ProductID");
                for (int j = 0; j < urls.length(); j++) {
                    URLs.add(urls.getString(j));
                }

                ProductImagesData productImagesData = new ProductImagesData();
                productImagesData.setProductID(ID);
                productImagesData.setImageURLs(URLs);

                productImagesDataArrayList.add(productImagesData);
            }

            //Collections.shuffle(productDataList);
            AppController.getInstance().setProductImagesDataList(productImagesDataArrayList);

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("PIPE");
            AppController.getInstance().setProductImagesDataList(null);
        }

    }
}

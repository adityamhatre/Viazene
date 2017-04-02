package com.thelogicalcoder.viazene.JSONParsers;

import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Data.ProductData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aditya on 012, 12 July 2015.
 */
public class ProductsParser {

    public ProductsParser(String response) {
        parseResponse(response);
    }

    void parseResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray productArray = jsonObject.getJSONArray("products");
            List<ProductData> productDataList = new ArrayList<>();
            for (int i = 0; i < productArray.length(); i++) {
                JSONObject productOBJ = (JSONObject) productArray.get(i);
                ProductData productData = new ProductData();
                productData.setProductID(productOBJ.getString("Product ID"));
                productData.setProductName(productOBJ.getString("Name"));
                productData.setProductCategory(productOBJ.getString("Category"));
                productData.setProductSubCategory(productOBJ.getString("Subcategory"));
                productData.setProductPrice(productOBJ.getString("Price"));
                productData.setProductPriceBeforeDiscount(productOBJ.getString("PriceBeforeDiscount"));
                productData.setProductURL(productOBJ.getString("URL"));
                productData.setProductImages(productOBJ.getString("Images"));
                productData.setProductPoints(productOBJ.getString("Points"));
                productData.setProductDiscount(productOBJ.getString("Discount"));
                productData.setProductDetails(productOBJ.getString("Details"));
                productData.setProductSize(productOBJ.getString("Size"));
                productData.setProductColor(productOBJ.getString("Color"));
                productData.setProductTags(productOBJ.getString("Tags"));
                productData.setProductCount(productOBJ.getString("Count"));
                productData.setProductPriceSaved(productOBJ.getString("PriceSaved"));
                productData.setBoughtCount(productOBJ.getString("BoughtCount"));
                productData.setTimeStamp(productOBJ.getString("AddedDateTime"));
                productDataList.add(productData);
            }
            //Collections.shuffle(productDataList);
            AppController.getInstance().setProductDataList(productDataList);

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("PPE");
            AppController.getInstance().setProductDataList(null);
        }

    }
}

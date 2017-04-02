package com.thelogicalcoder.viazene.Data;

import java.util.List;

/**
 * Created by Aditya on 015, 15, Aug, 2015.
 */
public class ProductImagesData {
    private String productID;
    private List<String> imageURLs;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public List<String> getImageURLs() {
        return imageURLs;
    }

    public void setImageURLs(List<String> imageURLs) {
        this.imageURLs = imageURLs;
    }
}

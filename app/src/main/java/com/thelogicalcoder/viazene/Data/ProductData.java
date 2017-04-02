package com.thelogicalcoder.viazene.Data;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Aditya on 010, 10 July 2015.
 */
public class ProductData implements Serializable {
    String productID, productName, productCategory, productSubCategory, productPrice, productPriceBeforeDiscount, productURL, productImages, productPoints, productDiscount, productDetails, productSize, productColor, productTags, productCount, productPriceSaved, boughtCount, timeStamp;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductSubCategory() {
        return productSubCategory;
    }

    public void setProductSubCategory(String productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductPriceBeforeDiscount() {
        return productPriceBeforeDiscount;
    }

    public void setProductPriceBeforeDiscount(String productPriceBeforeDiscount) {
        this.productPriceBeforeDiscount = productPriceBeforeDiscount;
    }

    public String getProductURL() {
        return productURL;
    }

    public void setProductURL(String productURL) {
        this.productURL = productURL;
    }

    public String getProductImages() {
        return productImages;
    }

    public void setProductImages(String productImages) {
        this.productImages = productImages;
    }

    public String getProductPoints() {
        return productPoints;
    }

    public void setProductPoints(String productPoints) {
        this.productPoints = productPoints;
    }

    public String getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(String productDiscount) {
        this.productDiscount = productDiscount;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public String getProductTags() {
        return productTags;
    }

    public void setProductTags(String productTags) {
        this.productTags = productTags;
    }

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    public String getProductPriceSaved() {
        return productPriceSaved;
    }

    public void setProductPriceSaved(String productPriceSaved) {
        this.productPriceSaved = productPriceSaved;
    }


    public void setBoughtCount(String boughtCount) {
        this.boughtCount = boughtCount;
    }

    public String getBoughtCount() {
        return boughtCount;
    }

    public Date getTimeStamp() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date(System.currentTimeMillis());

    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}

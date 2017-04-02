package com.thelogicalcoder.viazene.Data;

import java.util.List;

/**
 * Created by Aditya on 015, 15, Aug, 2015.
 */
public class ServiceImagesData {
    private String serviceID;
    private List<String> imageURLs;

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public List<String> getImageURLs() {
        return imageURLs;
    }

    public void setImageURLs(List<String> imageURLs) {
        this.imageURLs = imageURLs;
    }
}

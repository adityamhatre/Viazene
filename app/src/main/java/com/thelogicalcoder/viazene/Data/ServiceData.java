package com.thelogicalcoder.viazene.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Aditya on 010, 10 July 2015.
 */
public class ServiceData implements Serializable {
    private String id, serviceID, category, name, noOfServices, pinCodeAvailable, details, discount, tags,fromTime,toTime;
    private List<String> pinCodes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNoOfServices() {
        return noOfServices;
    }

    public void setNoOfServices(String noOfServices) {
        this.noOfServices = noOfServices;
    }

    public String getPinCodeAvailable() {
        return pinCodeAvailable;
    }

    public List<String> getPinCodeAvailableList() {
        return this.pinCodes;
    }

    public void setPinCodeAvailable(String pinCodeAvailable) {
        this.pinCodeAvailable = pinCodeAvailable;
        pinCodes = new ArrayList<>();
        pinCodes.addAll(Arrays.asList(pinCodeAvailable.trim().split(",")));
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getFromTime() {
        return Integer.parseInt(fromTime);
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public int getToTime() {
        return Integer.parseInt(toTime);
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }
}

package com.thelogicalcoder.viazene.Data;

import java.io.Serializable;

/**
 * Created by Aditya on 010, 10 July 2015.
 */
public class ServiceHelperData implements Serializable {
    private String id;
    private String serviceID;
    private String SubService;
    private String Price;

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

    public String getSubService() {
        return SubService;
    }

    public void setSubService(String subService) {
        SubService = subService;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}

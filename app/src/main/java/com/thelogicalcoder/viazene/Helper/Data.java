package com.thelogicalcoder.viazene.Helper;

import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Data.ProductData;
import com.thelogicalcoder.viazene.Data.ServiceData;

/**
 * Created by Aditya on 013, 13 July 2015.
 */
public class Data {
    public ProductData getProductFromID(String productID) {
        for (int i = 0; i < AppController.getInstance().getProductDataList().size(); i++) {
            if (AppController.getInstance().getProductDataList().get(i).getProductID().trim().equals(productID.trim())) {
                return AppController.getInstance().getProductDataList().get(i);
            }
        }
        return null;
    }

    public ServiceData getServiceFromID(String serviceID) {
        for (int i = 0; i < AppController.getInstance().getServices().getServices().size(); i++) {
            if (AppController.getInstance().getServices().getServices().get(i).getServiceID().trim().equals(serviceID.trim())) {
                return AppController.getInstance().getServices().getServices().get(i);
            }
        }
        return null;
    }

    public ServiceData getServiceFromName(String serviceName) {
        for (int i = 0; i < AppController.getInstance().getServices().getServices().size(); i++) {
            if (AppController.getInstance().getServices().getServices().get(i).getName().equalsIgnoreCase(serviceName)) {
                return AppController.getInstance().getServices().getServices().get(i);
            }
        }
        return null;
    }
}

package com.thelogicalcoder.viazene.JSONParsers;

import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Application.Services;
import com.thelogicalcoder.viazene.Data.ServiceData;
import com.thelogicalcoder.viazene.Data.ServiceHelperData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aditya on 012, 12 July 2015.
 */
public class ServicesParser {

    public ServicesParser(String response) {
        parseResponse(response);
    }

    void parseResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray servicesArray = jsonObject.getJSONArray("services");
            JSONArray servicesHelperArray = jsonObject.getJSONArray("servicesHelper");


            List<ServiceData> servicesList = new ArrayList<>();
            {
                for (int i = 0; i < servicesArray.length(); i++) {
                    JSONObject serviceOBJ = (JSONObject) servicesArray.get(i);
                    ServiceData servicesData = new ServiceData();
                    servicesData.setServiceID(serviceOBJ.getString("SERVICE ID"));
                    servicesData.setCategory(serviceOBJ.getString("CATEGORY"));
                    servicesData.setName(serviceOBJ.getString("NAME"));
                    servicesData.setNoOfServices(serviceOBJ.getString("No. of Services"));
                    servicesData.setPinCodeAvailable(serviceOBJ.getString("PINCODE AVAILABLE"));
                    servicesData.setDetails(serviceOBJ.getString("DETAILS"));
                    servicesData.setDiscount(serviceOBJ.getString("DISCOUNT"));
                    servicesData.setTags(serviceOBJ.getString("TAGS"));
                    servicesData.setFromTime(serviceOBJ.getString("FROMTIME"));
                    servicesData.setToTime(serviceOBJ.getString("TOTIME"));

                    servicesList.add(servicesData);
                }
            }

            List<ServiceHelperData> servicesHelperList = new ArrayList<>();
            {
                for (int i = 0; i < servicesHelperArray.length(); i++) {
                    JSONObject servicesHelperOBJ = (JSONObject) servicesHelperArray.get(i);
                    ServiceHelperData serviceHelperData = new ServiceHelperData();
                    serviceHelperData.setServiceID(servicesHelperOBJ.getString("SERVICE ID"));
                    serviceHelperData.setSubService(servicesHelperOBJ.getString("SubService"));
                    serviceHelperData.setPrice(servicesHelperOBJ.getString("Price"));

                    servicesHelperList.add(serviceHelperData);
                }
            }


            Services services = new Services();
            services.setServices(servicesList);
            services.setServicesHelper(servicesHelperList);

            AppController.getInstance().setServices(services);

        } catch (JSONException e) {
            e.printStackTrace();
            AppController.getInstance().setServices(null);
            System.out.println("SPE");
        }

    }
}

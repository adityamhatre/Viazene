package com.thelogicalcoder.viazene.Application;

import com.thelogicalcoder.viazene.Data.ServiceHelperData;

/**
 * Created by Aditya on 025, 25, Aug, 2015.
 */
public class SelectedService {
    private ServiceHelperData serviceHelperData;
    private String date, time;

    public ServiceHelperData getServiceHelperData() {
        return serviceHelperData;
    }

    public void setServiceHelperData(ServiceHelperData serviceHelperData) {
        this.serviceHelperData = serviceHelperData;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

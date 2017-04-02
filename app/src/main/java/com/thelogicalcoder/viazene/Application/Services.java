package com.thelogicalcoder.viazene.Application;

import com.thelogicalcoder.viazene.Data.ServiceData;
import com.thelogicalcoder.viazene.Data.ServiceHelperData;

import java.util.List;

/**
 * Created by Aditya on 014, 14, Aug, 2015.
 */
public class Services {
    List<ServiceData> services;
    List<ServiceHelperData> servicesHelper;

    public List<ServiceData> getServices() {
        return services;
    }

    public void setServices(List<ServiceData> services) {
        this.services = services;
    }

    public List<ServiceHelperData> getServicesHelper() {
        return servicesHelper;
    }

    public void setServicesHelper(List<ServiceHelperData> servicesHelper) {
        this.servicesHelper = servicesHelper;
    }
}

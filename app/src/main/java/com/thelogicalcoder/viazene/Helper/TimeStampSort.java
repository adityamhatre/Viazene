package com.thelogicalcoder.viazene.Helper;

import com.thelogicalcoder.viazene.Data.ProductData;

import java.util.Comparator;

/**
 * Created by Aditya on 028, 28 July 2015.
 */
public class TimeStampSort implements Comparator<ProductData> {
    @Override
    public int compare(ProductData item1, ProductData item2) {
        return item1.getTimeStamp().compareTo(item2.getTimeStamp());
    }
}

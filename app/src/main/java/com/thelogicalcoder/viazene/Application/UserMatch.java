package com.thelogicalcoder.viazene.Application;

import java.util.ArrayList;

/**
 * Created by Aditya on 027, 27 July 2015.
 */
public class UserMatch {
    String matchName, product1, product2, product3, product4;

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getProduct1() {
        return product1;
    }

    public void setProduct1(String product1) {
        this.product1 = product1;
    }

    public String getProduct2() {
        return product2;
    }

    public void setProduct2(String product2) {
        this.product2 = product2;
    }

    public String getProduct3() {
        return product3;
    }

    public void setProduct3(String product3) {
        this.product3 = product3;
    }

    public String getProduct4() {
        return product4;
    }

    public void setProduct4(String product4) {
        this.product4 = product4;
    }

    public ArrayList<String> getAllProductsOfMatch() {
        ArrayList<String> productIDs = new ArrayList<>();
        try {
            productIDs.add(product1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            productIDs.add(product2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            productIDs.add(product3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            productIDs.add(product4);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productIDs;
    }
}

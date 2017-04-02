package com.thelogicalcoder.viazene.Application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.digits.sdk.android.AuthCallback;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.thelogicalcoder.viazene.Activities.CrashActivity;
import com.thelogicalcoder.viazene.Data.ProductData;
import com.thelogicalcoder.viazene.Data.ProductImagesData;
import com.thelogicalcoder.viazene.Data.ServiceData;
import com.thelogicalcoder.viazene.Data.ServiceImagesData;
import com.thelogicalcoder.viazene.Data.WallCommentData;
import com.thelogicalcoder.viazene.Data.WallData;
import com.thelogicalcoder.viazene.Fragment.WallFragment;
import com.thelogicalcoder.viazene.Helper.Data;
import com.thelogicalcoder.viazene.R;
import com.thelogicalcoder.viazene.Volley.LruBitmapCache;

import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.util.ArrayList;
import java.util.List;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

/**
 * Created by Aditya on 011, 11 July 2015.
 */

@ReportsCrashes(formKey = "", // will not be used
        mailTo = "aditya.r.mhatre@gmail.com",
        customReportContent = {ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT},
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.error_report)
public class AppController extends Application {
    private static AppController appController;
    Class mainActivityWhichClass;
    LruBitmapCache mLruBitmapCache;
    private List<ProductData> productDataList;
    private List<WallData> wallDataList;
    private List<WallCommentData> wallCommentDataList;
    private List<String> productCategories, productSubcategories, serviceCategories;
    private List<Boolean> likeMap;
    private List<ServiceImagesData> serviceImagesDataList;
    private List<ProductImagesData> productImagesDataList;
    private List<String> tempUserInfo;
    private Boolean loggedIn = false;
    private Boolean registrationComplete = false;
    private UserProfile userProfile;
    private AuthCallback authCallback;
    private Boolean autoLoginTry = true;
    //<volley>
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    //</volley>
    private UserInfo userInfo;
    private UserWall userWall;
    private AlertDialog profileLoadingDialog;
    private Services services;
    private Boolean isWallReversed = false;
    private WallFragment wallFragment;

    public static synchronized AppController getInstance() {
        return appController;
    }


    public List<Boolean> getLikeMap() {
        return likeMap;
    }

    public void setLikeMap(List<Boolean> likeMap) {
        this.likeMap = likeMap;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CustomActivityOnCrash.install(this);
        CustomActivityOnCrash.setErrorActivityClass(CrashActivity.class);
        CustomActivityOnCrash.setShowErrorDetails(true);
        //ACRA.init(this);
        appController = this;
        productCategories = new ArrayList<>();
        serviceCategories = new ArrayList<>();
        tempUserInfo = new ArrayList<>();
        likeMap = new ArrayList<>();

    }

    public Boolean getAutoLoginTry() {
        return autoLoginTry;
    }

    public void setAutoLoginTry(Boolean autoLoginTry) {
        this.autoLoginTry = autoLoginTry;
    }

    public AuthCallback getAuthCallback() {
        return authCallback;
    }

    public void setAuthCallback(AuthCallback authCallback) {
        this.authCallback = authCallback;
    }

    public List<String> getTempUserInfo() {
        return tempUserInfo;
    }

    public void setTempUserInfo(List<String> tempUserInfo) {
        this.tempUserInfo = tempUserInfo;
    }

    public Boolean isRegistrationComplete() {
        return registrationComplete;
    }

    public void setRegistrationComplete(Boolean registrationComplete) {
        this.registrationComplete = registrationComplete;
    }

    public List<String> getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(String category) {
        this.productCategories.add(category);
    }

    public List<String> getProductSubcategories() {
        return productSubcategories;
    }

    public void setProductSubcategories(List<String> productSubcategories) {
        this.productSubcategories = productSubcategories;
    }

    public List<ProductData> getProductDataList() {
        return productDataList;
    }

    public void setProductDataList(List<ProductData> productDataList) {
        this.productDataList = productDataList;
    }

    public List<String> getSubcategoriesOfCategory(String category) {
        List<String> subcategoryProducts = new ArrayList<>();
        for (int i = 0; i < this.getProductDataList().size(); i++) {
            if (this.getProductDataList().get(i).getProductCategory().trim().equalsIgnoreCase(category.trim())) {
                if (!subcategoryProducts.contains(this.getProductDataList().get(i).getProductSubCategory())) {
                    subcategoryProducts.add(this.getProductDataList().get(i).getProductSubCategory());
                }
            }
        }

        return subcategoryProducts;
    }

    public List<ServiceData> filterServicesByCategory(final String category) {
        return FluentIterable.from(this.getServices().getServices()).filter(new Predicate<ServiceData>() {
            @Override
            public boolean apply(ServiceData input) {
                return input.getCategory().trim().equalsIgnoreCase(category.trim());
            }
        }).toList();
    }

    public List<ProductData> filterProductsByCategory(final String category) {
        return FluentIterable.from(this.getProductDataList()).filter(new Predicate<ProductData>() {
            @Override
            public boolean apply(ProductData input) {
                return input.getProductCategory().trim().equalsIgnoreCase(category.trim());
            }
        }).toList();
    }

    public List<ProductData> filterProductsBySubcategory(final String subcategory) {
        return FluentIterable.from(this.getProductDataList()).filter(new Predicate<ProductData>() {
            @Override
            public boolean apply(ProductData input) {
                return input.getProductSubCategory().trim().equalsIgnoreCase(subcategory.trim());
            }
        }).toList();
    }

    public List<ProductData> filterProductsByPrice(List<ProductData> currentList, final float min, final float max) {
        return FluentIterable.from(currentList).filter(new Predicate<ProductData>() {
            @Override
            public boolean apply(ProductData input) {
                return Float.parseFloat(input.getProductPrice()) > min && Float.parseFloat(input.getProductPrice()) < max;
            }
        }).toList();
    }

    public List<ProductData> filterProductsByColors(List<ProductData> currentList, final List<String> colorList) {

        final List<ProductData> returnList = new ArrayList<>();
        for (int i = 0; i < currentList.size(); i++) {
            for (int j = 0; j < colorList.size(); j++) {
                if (currentList.get(i).getProductColor().contains(colorList.get(j))) {
                    if (!returnList.contains(currentList.get(i)))
                        returnList.add(currentList.get(i));
                }
            }
        }
        return returnList;
    }


    public List<String> getColorList() {
        List<String> colorList = new ArrayList<>();
        for (int i = 0; i < productDataList.size(); i++) {
            String temp[] = productDataList.get(i).getProductColor().trim().split(",");
            if (temp.length != 0) {
                for (int j = 0; j < temp.length; j++) {
                    if (!colorList.contains(temp[j].trim())) {
                        colorList.add(temp[j].trim());
                    }
                }
            } else {
                if (!colorList.contains(productDataList.get(i).getProductColor().trim())) {
                    colorList.add(productDataList.get(i).getProductColor().trim());
                }
            }
        }

        return colorList;

    }

    public List<WallData> getWallDataList() {
        return wallDataList;
    }

    public void setWallDataList(List<WallData> wallDataList) {
        this.wallDataList = wallDataList;
        for (int i = 0; i < wallDataList.size(); i++) {
            likeMap.add(false);
        }
    }

    public List<WallCommentData> getWallCommentDataList() {
        return wallCommentDataList;
    }

    public void setWallCommentDataList(List<WallCommentData> wallCommentDataList) {
        this.wallCommentDataList = wallCommentDataList;
    }

    //<volley>
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            getLruBitmapCache();
            mImageLoader = new ImageLoader(this.mRequestQueue, mLruBitmapCache);
        }

        return this.mImageLoader;
    }

    public LruBitmapCache getLruBitmapCache() {
        if (mLruBitmapCache == null)
            mLruBitmapCache = new LruBitmapCache(this);
        return this.mLruBitmapCache;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
    //</volley>


    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getProductNameByID(String productID) {
        for (int i = 0; i < productDataList.size(); i++) {
            if (productDataList.get(i).getProductID().equalsIgnoreCase(productID)) {
                return productDataList.get(i).getProductName();
            }
        }
        return null;
    }

    public Class getMainActivityWhichClass() {
        return mainActivityWhichClass;
    }

    public void setMainActivityWhichClass(Class mainActivityWhichClass) {
        this.mainActivityWhichClass = mainActivityWhichClass;
    }

    public UserWall getUserWall() {
        return userWall;
    }

    public void setUserWall(UserWall userWall) {
        this.userWall = userWall;
        likeMap = new ArrayList<>();
        for (int i = 0; i < wallDataList.size(); i++) {
            if (userWall.getLikeList().contains(wallDataList.get(i).getWallID().trim())) {
                likeMap.add(true);
            } else {
                likeMap.add(false);
            }
        }
        System.out.println("PUBLIC WALL");
        for (int i = 0; i < wallDataList.size(); i++) {
            System.out.println(wallDataList.get(i).getWallID());
        }
        System.out.println("USER WALL");
        for (int i = 0; i < userWall.getLikeList().size(); i++) {
            System.out.println(userWall.getLikeList().get(i));
        }
        System.out.println("LIKE MAP");
        for (int i = 0; i < likeMap.size(); i++) {
            System.out.println(likeMap.get(i));
        }
    }

    public void showMaterialProgress(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(View.inflate(activity, R.layout.profile_loading_dialog, null));
        builder.setCancelable(false);
        profileLoadingDialog = builder.create();
        profileLoadingDialog.show();
    }

    public void dismissMaterialProgress() {
        try {
            profileLoadingDialog.dismiss();
        } catch (Exception e) {
            System.out.println("LEAKED PROGRESSBAR");
            e.printStackTrace();
        }
    }

    public String getViazeneTotalPoints() {
        float points = 0;
        for (int i = 0; i < getUserProfile().getUserBought().size(); i++) {
            points = points + Float.parseFloat((new Data().getProductFromID(getUserProfile().getUserBought().get(i).getProductID())).getProductPoints());
        }
        return "" + points;
    }

    public void logOut() {
        likeMap = new ArrayList<>();
        for (int i = 0; i < wallDataList.size(); i++) {
            likeMap.add(false);
        }
        tempUserInfo = new ArrayList<>();
        loggedIn = false;
        registrationComplete = false;
        userProfile = null;
        authCallback = null;
        userInfo = null;
        userWall = null;
        autoLoginTry = true;
        getSharedPreferences("savedSession", Context.MODE_PRIVATE).edit().remove("email").remove("password").commit();
    }

    public List<String> getServiceCategories() {
        return serviceCategories;
    }

    public void setServiceCategories(String serviceCategory) {
        this.serviceCategories.add(serviceCategory);
    }

    /*List<String> servicesURLs;
    public void setServicesURLs()
    {
        servicesURLs=new ArrayList<>();
        for (int i = 0; i < getServiceCategories().size(); i++) {
            for (int j = 0; j < filterServicesByCategory(getServiceCategories().get(i)).size(); j++) {
                servicesURLs.add(("http://www.viazene.com/viazeneApp/services/Categories/"+
                        getServiceCategories().get(i)+"/"+
                        filterServicesByCategory(getServiceCategories().get(i)).get(j).getName()).replace(" ","%20").trim()
                );
            }
        }
        System.out.println("URLS");
        for (int i = 0; i < servicesURLs.size(); i++) {
            System.out.println(servicesURLs.get(i));
        }
    }*/

    public Services getServices() {
        return services;
    }

    public void setServices(Services services) {
        this.services = services;

    }

    public List<ServiceImagesData> getServiceImagesDataList() {
        return serviceImagesDataList;
    }

    public void setServiceImagesDataList(List<ServiceImagesData> serviceImagesDataList) {
        this.serviceImagesDataList = serviceImagesDataList;
    }

    public List<String> getServiceImageURLList(ServiceData serviceData) {
        List<String> urlList = new ArrayList<>();

        for (int i = 0; i < AppController.getInstance().getServiceImagesDataList().size(); i++) {
            if (AppController.getInstance().getServiceImagesDataList().get(i).getServiceID().trim().equalsIgnoreCase(serviceData.getServiceID().trim())) {
                urlList = AppController.getInstance().getServiceImagesDataList().get(i).getImageURLs();
            }
        }
        return urlList;
    }

    public List<String> getProductImageURLList(ProductData productData) {
        List<String> urlList = new ArrayList<>();

        for (int i = 0; i < AppController.getInstance().getProductImagesDataList().size(); i++) {
            if (AppController.getInstance().getProductImagesDataList().get(i).getProductID().trim().equalsIgnoreCase(productData.getProductID().trim())) {
                urlList = AppController.getInstance().getProductImagesDataList().get(i).getImageURLs();
            }
        }

        return urlList;
    }


    public void setWallInstance(WallFragment wallFragment) {
        this.wallFragment = wallFragment;
    }

    public WallFragment getWallFragment() {
        return wallFragment;
    }

    public List<ProductImagesData> getProductImagesDataList() {
        return productImagesDataList;
    }

    public void setProductImagesDataList(List<ProductImagesData> productImagesDataList) {
        this.productImagesDataList = productImagesDataList;
    }
}

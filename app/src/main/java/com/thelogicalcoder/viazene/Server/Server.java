package com.thelogicalcoder.viazene.Server;

import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.R;

/**
 * Created by Aditya on 011, 11 July 2015.
 */
public class Server {
    //private static String VIAZENE_APP_DOMAIN = "http://ladysona.2fh.co/ladysona/";
    public static final String TWITTER_KEY = AppController.getInstance().getApplicationContext().getString(R.string.twitter_key);
    public static final String TWITTER_SECRET = AppController.getInstance().getApplicationContext().getString(R.string.twitter_secret);
    private static String VIAZENE_DOMAIN = "http://www.thelegacycoder.com/Viazene/";
    //private static String VIAZENE_DOMAIN = "http://192.168.1.71/";
    private static String VIAZENE_APP_DOMAIN = VIAZENE_DOMAIN + "viazeneApp/";
    public static String SERVICES_DOMAIN = VIAZENE_APP_DOMAIN + "services/Categories/";
    public static String PRODUCTS_DOMAIN = VIAZENE_APP_DOMAIN + "products/Categories/";
    public static String GET_PRODUCTS = VIAZENE_APP_DOMAIN + "products.php";
    public static String GET_SERVICES = VIAZENE_APP_DOMAIN + "services.php";
    public static String CHECK_LOGIN = VIAZENE_APP_DOMAIN + "checkLogin.php";
    public static String CHECK_EMAIL = VIAZENE_APP_DOMAIN + "checkEmail.php";
    public static String REGISTER = VIAZENE_APP_DOMAIN + "register.php";
    public static String GET_USER_INFO = VIAZENE_APP_DOMAIN + "getUserInfo.php";
    public static String GET_WALL = VIAZENE_APP_DOMAIN + "wall.php";
    public static String GET_WALL_COMMENTS = VIAZENE_APP_DOMAIN + "viewWallComment.php";
    public static String GET_WALL_OF_USER = VIAZENE_APP_DOMAIN + "getUserWall.php";
    public static String UPDATE_USER_INFO = VIAZENE_APP_DOMAIN + "updateInfo.php";
    public static String DELETE_COMMENT = VIAZENE_APP_DOMAIN + "deleteComment.php";
    public static String GET_PROFILE = VIAZENE_APP_DOMAIN + "getProfile.php";
    public static String SAVE_MATCH = VIAZENE_APP_DOMAIN + "saveMatch.php";
    public static String EDIT_MATCH = VIAZENE_APP_DOMAIN + "editMatch.php";
    public static String PROFILE_PIC_UPLOAD = VIAZENE_APP_DOMAIN + "uploadPic.php";
    public static String WALL_LIKE = VIAZENE_APP_DOMAIN + "incLike.php";
    public static String SEND_QUOTATION_EMAIL = VIAZENE_APP_DOMAIN + "sendQuotationEmail.php";
    public static String WALL_UNLIKE = VIAZENE_APP_DOMAIN + "decLike.php";
    public static String ADD_COMMENT_TO_WALL = VIAZENE_APP_DOMAIN + "addComment.php";
    public static String ADD_TO_CART = VIAZENE_APP_DOMAIN + "addToCart.php";
    public static String REMOVE_FROM_CART = VIAZENE_APP_DOMAIN + "removeFromCart.php";
    public static String GET_PICS_OF_SERVICES = VIAZENE_APP_DOMAIN + "getPicsOfServices.php";
    public static String GET_PICS_OF_PRODUCTS = VIAZENE_APP_DOMAIN + "getPicsOfProducts.php";
    public static String BUY_PRODUCT = VIAZENE_APP_DOMAIN + "buyProduct.php";
    public static String BUY_PRODUCT_MULTIPLE = VIAZENE_APP_DOMAIN + "buyProductMultiple.php";
    public static String GET_ORDER_ID = VIAZENE_APP_DOMAIN + "getLatestOrderID.php";
    public static String ADD_TO_WALL = VIAZENE_APP_DOMAIN + "addToWall.php";
    public static String ADD_TO_WALL_MULTIPLE = VIAZENE_APP_DOMAIN + "addToWallMultiple.php";
    public static String SEND_REGISTRATION_EMAIL = VIAZENE_DOMAIN + "regMail.php";
    public static String SEND_SINGLE_BOUGHT_EMAIL = VIAZENE_DOMAIN + "singleBoughtMail.php";
    public static String SEND_MULTIPLE_BOUGHT_EMAIL = VIAZENE_DOMAIN + "multipleBoughtMail.php";
    public static String APP_DOMAIN_PASSWORD = AppController.getInstance().getApplicationContext().getString(R.string.app_domain_password);
    public static String APP_DOMAIN_USERNAME = AppController.getInstance().getApplicationContext().getString(R.string.app_domain_username);
    public static String BUY_SERVICE = VIAZENE_APP_DOMAIN + "buyService.php";

}

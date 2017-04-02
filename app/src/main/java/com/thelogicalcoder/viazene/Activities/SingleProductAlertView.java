package com.thelogicalcoder.viazene.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.AsyncTasks.CartOperation;
import com.thelogicalcoder.viazene.AsyncTasks.GetProfile;
import com.thelogicalcoder.viazene.Data.ProductData;
import com.thelogicalcoder.viazene.Helper.Data;
import com.thelogicalcoder.viazene.Interfaces.onCartCallBackListener;
import com.thelogicalcoder.viazene.Interfaces.onProfileLoadedListener;
import com.thelogicalcoder.viazene.JSONParsers.ProfileDetailsParser;
import com.thelogicalcoder.viazene.R;
import com.thelogicalcoder.viazene.Server.Server;

import babushkatext.BabushkaText;

public class SingleProductAlertView extends Activity {

    ProductData productData;
    SliderLayout sliderShow;
    BabushkaText productName, productPrice, productDescription;
    Button makeMyMatch, buyNow;
    ImageView shareButton, cartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productData = new Data().getProductFromID(getIntent().getStringExtra("productID"));
        setContentView(R.layout.activity_single_product_view);

        findViewById(R.id.bottomBar).setVisibility(View.GONE);
        sliderShow = (SliderLayout) findViewById(R.id.slider);
        for (int i = 0; i < AppController.getInstance().getProductImageURLList(productData).size(); i++) {
            final DefaultSliderView imageSlide = new DefaultSliderView(this);
            imageSlide.empty(R.drawable.logo_loading);
            imageSlide
                    .image((Server.PRODUCTS_DOMAIN + productData.getProductCategory().trim() + "/" + productData.getProductSubCategory() + "/" + productData.getProductName().trim() + "/" + AppController.getInstance().getProductImageURLList(productData).get(i)).replace(" ", "%20"))
                    .setScaleType(BaseSliderView.ScaleType.CenterInside);
            imageSlide.error(R.drawable.error);
            final int pos = i;
            imageSlide.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView baseSliderView) {
                    View view = View.inflate(SingleProductAlertView.this, R.layout.zoom_image_view, null);
                    WebView webView = (WebView) view.findViewById(R.id.webView);
                    final ImageView imageView = (ImageView) view.findViewById(R.id.cover);
                    webView.loadUrl((Server.PRODUCTS_DOMAIN + productData.getProductCategory().trim() + "/" + productData.getProductSubCategory() + "/" + productData.getProductName().trim() + "/" + AppController.getInstance().getProductImageURLList(productData).get(pos)).replace(" ", "%20"));
                    System.out.println((Server.PRODUCTS_DOMAIN + productData.getProductCategory().trim() + "/" + productData.getProductSubCategory() + "/" + productData.getProductName().trim() + "/" + AppController.getInstance().getProductImageURLList(productData).get(pos)).replace(" ", "%20"));
                    WebSettings settings = webView.getSettings();
                    settings.setJavaScriptEnabled(true);
                    webView.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            imageView.setVisibility(View.GONE);
                            super.onPageFinished(view, url);
                        }
                    });
                    settings.setSupportZoom(true);
                    settings.setBuiltInZoomControls(true);
                    new AlertDialog.Builder(SingleProductAlertView.this).setView(view).create().show();
                }
            });
            sliderShow.addSlider(imageSlide);
        }
        sliderShow.setDuration(4000);
        productName = (BabushkaText) findViewById(R.id.productName);
        productPrice = (BabushkaText) findViewById(R.id.productPrice);
        productDescription = (BabushkaText) findViewById(R.id.descriptionText);
        shareButton = (ImageView) findViewById(R.id.shareButton);
        cartButton = (ImageView) findViewById(R.id.cartButton);
        productName.addPiece(new BabushkaText.Piece.Builder(productData.getProductName()).textColor(Color.parseColor("#000000")).textSize(70).build());
        productName.display();

        productPrice.addPiece(new BabushkaText.Piece.Builder("Rs. " + productData.getProductPrice() + "\n").textSize(80).build());
        productPrice.addPiece(new BabushkaText.Piece.Builder("MRP: ").textSize(50).build());
        productPrice.addPiece(new BabushkaText.Piece.Builder("Rs. " + productData.getProductPriceBeforeDiscount()).strike().textSize(50).build());
        productPrice.addPiece(new BabushkaText.Piece.Builder("   " + productData.getProductDiscount() + "% OFF\n").textColor(Color.parseColor("#29A6A6")).textSize(50).build());
        productPrice.addPiece(new BabushkaText.Piece.Builder(("You save: Rs. " + productData.getProductPriceSaved())).textSize(50).build());
        productPrice.addPiece(new BabushkaText.Piece.Builder(("\n" + productData.getProductPoints() + " VZ Points")).textSize(50).build());
        productPrice.display();


        productDescription.addPiece(new BabushkaText.Piece.Builder((productData.getProductDetails())).textSize(50).build());
        productDescription.display();

        sliderShow.startAutoCycle();

        //getSupportActionBar().setTitle(productData.getProductName());
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        makeMyMatch = (Button) findViewById(R.id.makeMyMatchButton);
        makeMyMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SingleProductAlertView.this, MatchMakerActivity.class).putExtra("productID", productData.getProductID()));
            }
        });

        buyNow = (Button) findViewById(R.id.buyNow);
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SingleProductAlertView.this, BuyProductCOD.class).putExtra("productID", productData.getProductID()).putExtra("mode", "single"));
            }
        });


        if (AppController.getInstance().isLoggedIn()) {
            if (AppController.getInstance().getUserProfile().getUserCart().contains(productData.getProductID())) {
                cartButton.setImageResource(R.drawable.remove_from_cart);
            } else cartButton.setImageResource(R.drawable.add_to_cart);
        }

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppController.getInstance().isLoggedIn()) {
                    if (!AppController.getInstance().getUserProfile().getUserCart().contains(productData.getProductID())) {
                        AppController.getInstance().showMaterialProgress(SingleProductAlertView.this);
                        new CartOperation(SingleProductAlertView.this, true, productData.getProductID(), new onCartCallBackListener() {
                            @Override
                            public void onCartCallBack(String response) {
                                if (response.equalsIgnoreCase("errorOccurred")) {
                                    Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    new GetProfile(SingleProductAlertView.this, AppController.getInstance().getUserInfo().getEmail(), new onProfileLoadedListener() {
                                        @Override
                                        public void onProfileLoaded(String response) {
                                            if (!response.equalsIgnoreCase("errorOccurred")) {
                                                new ProfileDetailsParser(response);
                                                cartButton.setImageResource(R.drawable.remove_from_cart);
                                            } else {
                                                Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).execute();

                                }
                                AppController.getInstance().dismissMaterialProgress();
                            }
                        }).execute();
                    } else {
                        AppController.getInstance().showMaterialProgress(SingleProductAlertView.this);
                        new CartOperation(SingleProductAlertView.this, false, productData.getProductID(), new onCartCallBackListener() {
                            @Override
                            public void onCartCallBack(String response) {
                                if (response.equalsIgnoreCase("errorOccurred")) {
                                    Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    new GetProfile(SingleProductAlertView.this, AppController.getInstance().getUserInfo().getEmail(), new onProfileLoadedListener() {
                                        @Override
                                        public void onProfileLoaded(String response) {
                                            if (!response.equalsIgnoreCase("errorOccurred")) {
                                                new ProfileDetailsParser(response);
                                                cartButton.setImageResource(R.drawable.add_to_cart);
                                            } else {
                                                Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).execute();
                                }
                                AppController.getInstance().dismissMaterialProgress();
                            }
                        }).execute();
                    }
                } else {
                    Snackbar.make(findViewById(R.id.root), "Login to add product to cart", Snackbar.LENGTH_LONG).setAction("LOGIN", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(SingleProductAlertView.this, LoginRegisterActivity.class).putExtra("source", "spv"));
                        }
                    }).show();
                }
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppController.getInstance().isLoggedIn()) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Product: " + productData.getProductName() + "\n" + productData.getProductURL().replace(" ", "%20") + "/image1.jpg\n" + "\n\nShared via VIAZENE App\nDownload the app now - Hurry Up !!!");
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, "Share with"));
                } else
                    Snackbar.make(v, "Login to share product", Snackbar.LENGTH_SHORT).setAction("LOGIN", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(SingleProductAlertView.this, LoginRegisterActivity.class).putExtra("source", "spv"));
                        }
                    }).show();

            }
        });

    }


    @Override
    protected void onStop() {
        super.onStop();
        sliderShow.stopAutoCycle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*if (AppController.getInstance().isLoggedIn()) {
            menu.add(Menu.NONE, 0, Menu.NONE, "Add to cart").setIcon(R.drawable.ic_cart).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    return false;
                }
            }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        } else {
            menu.add(Menu.NONE, 0, Menu.NONE, "Log in").setIcon(R.drawable.user_icon_not_logged_in).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    startActivity(new Intent(SingleProductView.this, LoginRegisterActivity.class).putExtra("source","spv"));
                   // finish();
                    return false;
                }
            }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        if (AppController.getInstance().isLoggedIn()) {
            if (AppController.getInstance().getUserProfile().getUserCart().contains(productData.getProductID())) {
                cartButton.setImageResource(R.drawable.remove_from_cart);
            } else cartButton.setImageResource(R.drawable.add_to_cart);
        }
        super.onResume();
    }
}
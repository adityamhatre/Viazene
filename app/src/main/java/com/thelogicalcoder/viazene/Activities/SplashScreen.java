package com.thelogicalcoder.viazene.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;

import com.thelogicalcoder.viazene.AsyncTasks.GetProducts;
import com.thelogicalcoder.viazene.AsyncTasks.GetProductsImagePaths;
import com.thelogicalcoder.viazene.AsyncTasks.GetServices;
import com.thelogicalcoder.viazene.AsyncTasks.GetServicesImagePaths;
import com.thelogicalcoder.viazene.AsyncTasks.GetWall;
import com.thelogicalcoder.viazene.Interfaces.onAsyncCallBack;
import com.thelogicalcoder.viazene.Interfaces.onProductsLoadedListener;
import com.thelogicalcoder.viazene.Interfaces.onServicesImagesLoadedListener;
import com.thelogicalcoder.viazene.Interfaces.onServicesLoadedListener;
import com.thelogicalcoder.viazene.Interfaces.onWallCommentsLoadedListener;
import com.thelogicalcoder.viazene.Interfaces.onWallLoadedListener;
import com.thelogicalcoder.viazene.JSONParsers.ProductImagesParser;
import com.thelogicalcoder.viazene.JSONParsers.ProductsParser;
import com.thelogicalcoder.viazene.JSONParsers.ServicesImagesParser;
import com.thelogicalcoder.viazene.JSONParsers.ServicesParser;
import com.thelogicalcoder.viazene.JSONParsers.WallCommentsParser;
import com.thelogicalcoder.viazene.JSONParsers.WallParser;
import com.thelogicalcoder.viazene.R;

public class SplashScreen extends Activity {

    Boolean productsLoaded = false, wallLoaded = false, wallCommentsLoaded = false;
    Button retryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
        retryButton = (Button) findViewById(R.id.retryButton);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productsLoaded = false;
                wallLoaded = false;
                wallCommentsLoaded = false;
                retryButton.setVisibility(View.GONE);
                fetchInfo();
            }
        });
        //throw new RuntimeException();

        fetchInfo();
//        throw new RuntimeException(); TODO FOR CUSTOM CRASH

    }

    void fetchInfo() {
        retryButton.setVisibility(View.GONE);
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        new GetProducts(SplashScreen.this, new onProductsLoadedListener() {
            @Override
            public void onProductsLoaded(String response) {
                if (!response.equalsIgnoreCase("errorOccurred")) {
                    new ProductsParser(response);
                    productsLoaded = true;
                    new GetProductsImagePaths(SplashScreen.this, new onAsyncCallBack() {
                        @Override
                        public void onCallBack(String response) {
                            if (!response.equalsIgnoreCase("errorOccurred")) {
                                new ProductImagesParser(response);
                                new GetWall(SplashScreen.this, new onWallLoadedListener() {
                                    @Override
                                    public void onWallLoaded(String response) {
                                        if (!response.equalsIgnoreCase("errorOccurred")) {
                                            new WallParser(response);
                                            wallLoaded = true;
                                        } else {
                                            Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show();
                                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                                            retryButton.setVisibility(View.VISIBLE);
                                            wallLoaded = false;

                                        }
                                    }
                                }, new onWallCommentsLoadedListener() {
                                    @Override
                                    public void onWallCommentsLoaded(String response) {
                                        if (!response.equalsIgnoreCase("errorOccurred")) {
                                            wallCommentsLoaded = true;
                                            new WallCommentsParser(response);
                                            if (productsLoaded && wallLoaded && wallCommentsLoaded) {
                                                new GetServices(SplashScreen.this, new onServicesLoadedListener() {
                                                    @Override
                                                    public void onServicesLoaded(String response) {
                                                        if (!response.equalsIgnoreCase("errorOccurred")) {
                                                            new ServicesParser(response);

                                                            new GetServicesImagePaths(SplashScreen.this, new onServicesImagesLoadedListener() {
                                                                @Override
                                                                public void onServicesImagesLoaded(String response) {
                                                                    new ServicesImagesParser(response);
                                                                    findViewById(R.id.progressBar).setVisibility(View.GONE);
                                                                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                                                                    finish();
                                                                }
                                                            }).execute();
                                                        } else
                                                            Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show();
                                                    }
                                                }).execute();

                                            } else {
                                                Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show();
                                                findViewById(R.id.progressBar).setVisibility(View.GONE);
                                                retryButton.setVisibility(View.VISIBLE);
                                                wallCommentsLoaded = false;
                                                wallLoaded = false;
                                                productsLoaded = false;
                                            }
                                        } else {
                                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                                            retryButton.setVisibility(View.VISIBLE);
                                            wallCommentsLoaded = false;
                                        }
                                    }
                                }).execute();
                            } else {
                                Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show();
                                findViewById(R.id.progressBar).setVisibility(View.GONE);
                                retryButton.setVisibility(View.VISIBLE);
                                productsLoaded = false;
                            }

                        }
                    }).execute();
                } else {
                    Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show();
                    findViewById(R.id.progressBar).setVisibility(View.GONE);
                    retryButton.setVisibility(View.VISIBLE);
                    productsLoaded = false;
                }
            }

        }).execute();
    }



}

package com.thelogicalcoder.viazene.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;

import com.blunderer.materialdesignlibrary.activities.Activity;
import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.AsyncTasks.Register;
import com.thelogicalcoder.viazene.AsyncTasks.SendRegistrationEmail;
import com.thelogicalcoder.viazene.Interfaces.onAsyncCallBack;
import com.thelogicalcoder.viazene.Interfaces.onRegisterListener;
import com.thelogicalcoder.viazene.R;
import com.thelogicalcoder.viazene.Server.Server;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Aditya on 025, 25 July 2015.
 */
public class VerifyPhoneActivity extends Activity {
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.

    List<String> details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(Server.TWITTER_KEY, Server.TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits());

        details = getIntent().getStringArrayListExtra("details");
        //Digits.getSessionManager().clearActiveSession();

        /*DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
        digitsButton.setAuthTheme(R.style.AppThemeLight);*/

        /*findViewById(R.id.verifyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        AppController.getInstance().setAuthCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession digitsSession, String s) {
                final List<String> details = getIntent().getStringArrayListExtra("details");
                AppController.getInstance().showMaterialProgress(VerifyPhoneActivity.this);
                new Register(VerifyPhoneActivity.this, details.get(5), details.get(6), details.get(0), details.get(3), details.get(2), details.get(4), details.get(1), new onRegisterListener() {
                    @Override
                    public void onRegistered(String response) {
                        AppController.getInstance().dismissMaterialProgress();

                        if (response.trim().equalsIgnoreCase("success")) {
                            AppController.getInstance().showMaterialProgress(VerifyPhoneActivity.this);
                            new SendRegistrationEmail(VerifyPhoneActivity.this, details.get(5), details.get(6), details.get(0), new onAsyncCallBack() {
                                @Override
                                public void onCallBack(String response) {
                                    AppController.getInstance().dismissMaterialProgress();

                                    if (!response.equalsIgnoreCase("errorOccurred")) {
                                        AppController.getInstance().setRegistrationComplete(true);
                                        finish();
                                    } else {
                                        Snackbar.make(findViewById(R.id.root), "Register successful with some errors", Snackbar.LENGTH_SHORT).show();

                                    }
                                }
                            }).execute();

                        } else {
                            AppController.getInstance().setRegistrationComplete(false);
                            Snackbar.make(findViewById(R.id.root), "Some error occurred", Snackbar.LENGTH_SHORT).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 2000);
                        }
                        Digits.getSessionManager().clearActiveSession();
                    }
                }).execute();
            }

            @Override
            public void failure(DigitsException e) {

            }
        });

        Digits.authenticate(AppController.getInstance().getAuthCallback(), "+91" + details.get(1));
        /*digitsButton.setCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                // Do something with the session and phone number
                List<String> details = getIntent().getStringArrayListExtra("details");
                //reviewFragment.startLoad();

                new Register(VerifyPhoneActivity.this, details.get(5), details.get(6), details.get(0), details.get(1), details.get(2), details.get(3), details.get(4), new onRegisterListener() {
                    @Override
                    public void onRegistered(String response) {
                        if (response.trim().equalsIgnoreCase("success")) {
                            AppController.getInstance().setRegistrationComplete(true);
                            finish();
                            //startActivity(new Intent(RegistrationWizard.this,VerifyPhoneActivity.class));
                        } else {
                            AppController.getInstance().setRegistrationComplete(false);


                            //nextButton.setVisibility(View.VISIBLE);
                            Snackbar.make(findViewById(R.id.root), "Some error occurred", Snackbar.LENGTH_SHORT).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 2000);
                        }
                    }
                }).execute();
            }

            @Override
            public void failure(DigitsException exception) {
                // Do something on failure
            }
        });*/
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_verify_phone;
    }

    @Override
    protected void onResume() {
        //Digits.getSessionManager().clearActiveSession();
        super.onResume();
    }

    @Override
    protected boolean enableActionBarShadow() {
        return false;
    }

    @Override
    protected ActionBarHandler getActionBarHandler() {
        return null;
    }
}

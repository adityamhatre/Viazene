package com.thelogicalcoder.viazene.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.blunderer.materialdesignlibrary.activities.ViewPagerWithTabsActivity;
import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;
import com.blunderer.materialdesignlibrary.handlers.ViewPagerHandler;
import com.thelogicalcoder.viazene.Fragment.LoginFragment;
import com.thelogicalcoder.viazene.Fragment.RegisterFragment;

public class LoginRegisterActivity extends ViewPagerWithTabsActivity {

    LoginFragment loginFragment;
    RegisterFragment registerFragment;
    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager
            .OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    replaceTitle("Login");
                    break;
                case 1:
                    replaceTitle("Register");
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

    };

    public LoginRegisterActivity() {
        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment();
        setOnPageChangeListener(mOnPageChangeListener);
    }

    private void replaceTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceTitle("Login");
    }

    @Override
    protected boolean expandTabs() {
        return true;
    }

    @Override
    protected ActionBarHandler getActionBarHandler() {
        return null;
    }

    @Override
    public ViewPagerHandler getViewPagerHandler() {
        return new ViewPagerHandler(this).addPage("Login", loginFragment).addPage("Register", registerFragment);
    }


    @Override
    public int defaultViewPagerPageSelectedPosition() {
        return 0;
    }

    @Override
    public void onBackPressed() {
        registerFragment.enableControls();
        if (getIntent().getStringExtra("source").equalsIgnoreCase("mma")) {
            finish();
        } else if (getIntent().getStringExtra("source").equalsIgnoreCase("spv")) {
            finish();
        } else if (getIntent().getStringExtra("source").equalsIgnoreCase("wall")) {
            finish();
        } else if (getIntent().getStringExtra("source").equalsIgnoreCase("buyP")) {
            finish();
        } else if (getIntent().getStringExtra("source").equalsIgnoreCase("ssv")) {
            finish();
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        super.onBackPressed();

    }

    @Override
    protected boolean enableActionBarShadow() {
        return false;
    }


}
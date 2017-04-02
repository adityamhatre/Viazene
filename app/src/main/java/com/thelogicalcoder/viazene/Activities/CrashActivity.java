package com.thelogicalcoder.viazene.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.blunderer.materialdesignlibrary.activities.Activity;
import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;
import com.thelogicalcoder.viazene.R;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

/**
 * Created by Aditya on 010, 10, Aug, 2015.
 */
public class CrashActivity extends Activity {
    @Override
    protected int getContentView() {
        return R.layout.activity_crash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("CRASHED\n" + CustomActivityOnCrash.getAllErrorDetailsFromIntent(CrashActivity.this, getIntent()));

        findViewById(R.id.exitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        findViewById(R.id.sendReportButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"report@viazene.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Viazene App Crash Report");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, CustomActivityOnCrash.getAllErrorDetailsFromIntent(CrashActivity.this, getIntent()));
                startActivity(Intent.createChooser(emailIntent, "Send error report via"));
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                System.exit(0);
            }
        }, 700);
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

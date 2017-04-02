package com.thelogicalcoder.viazene.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;

import com.blunderer.materialdesignlibrary.activities.Activity;
import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.AsyncTasks.GetUserInfo;
import com.thelogicalcoder.viazene.AsyncTasks.UploadDp;
import com.thelogicalcoder.viazene.Helper.ImageFilePath;
import com.thelogicalcoder.viazene.Interfaces.UploadDPListener;
import com.thelogicalcoder.viazene.Interfaces.onUserInfoLoadedListener;
import com.thelogicalcoder.viazene.JSONParsers.UserInfoParser;
import com.thelogicalcoder.viazene.R;
import com.thelogicalcoder.viazene.Volley.FeedImageView;

import java.io.File;

/**
 * Created by Aditya on 004, 04 August 2015.
 */
public class ChangeDP extends Activity {
    ImageView editProfilePic;
    FeedImageView profilePicView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editProfilePic = (ImageView) findViewById(R.id.editProfilePic);
        profilePicView = (FeedImageView) findViewById(R.id.profilePicView);

        getSupportActionBar().setTitle(AppController.getInstance().getUserInfo().getUser());

        profilePicView.setDefaultImageResId(R.drawable.profile1);
        profilePicView.setImageUrl(AppController.getInstance().getUserInfo().getDpURL().trim().replace(" ", "%20") + "?" + System.currentTimeMillis(), AppController.getInstance().getImageLoader());
        profilePicView.setErrorImageResId(R.drawable.profile1);

        editProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/jpg");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select File"), 4532);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            System.out.println(requestCode + " " + 303);
            //if (requestCode == MY_INTENT_CLICK) {
            if (null == data) return;
            Uri selectedImageUri = data.getData();
            String selectedImagePath = ImageFilePath.getPath(this.getApplicationContext(), selectedImageUri);
            if (selectedImagePath.trim().substring(selectedImagePath.trim().lastIndexOf("."), selectedImagePath.length()).equalsIgnoreCase(".jpg")
                    ||
                    selectedImagePath.trim().substring(selectedImagePath.trim().lastIndexOf("."), selectedImagePath.length()).equalsIgnoreCase(".jpeg")
                    ||
                    selectedImagePath.trim().substring(selectedImagePath.trim().lastIndexOf("."), selectedImagePath.length()).equalsIgnoreCase(".png")
                    ||
                    selectedImagePath.trim().substring(selectedImagePath.trim().lastIndexOf("."), selectedImagePath.length()).equalsIgnoreCase(".bmp")) {
                uploadDp(selectedImagePath);
            } else
                Snackbar.make(findViewById(R.id.root), "Not a valid image", Snackbar.LENGTH_SHORT).show();
            //}
        }
    }

    public void uploadDp(String selectedImagePath) {
        new UploadDp(this, new File(selectedImagePath), new UploadDPListener() {
            @Override
            public void onDPUploaded(String response) {
                if (response.trim().equalsIgnoreCase("errorOccurred")) {
                    Snackbar.make(findViewById(R.id.root), "Some error occurred", Snackbar.LENGTH_SHORT).show();
                } else if (response.trim().contains("success")) {


                    new GetUserInfo(ChangeDP.this, AppController.getInstance().getUserInfo().getEmail(), new onUserInfoLoadedListener() {
                        @Override
                        public void onUserInfoLoaded(String response) {
                            System.out.println(response);
                            if (response.equalsIgnoreCase("") || response.equalsIgnoreCase("errorOccurred")) {
                                Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                            } else {
                                new UserInfoParser(ChangeDP.this, response);
                                AppController.getInstance().setLoggedIn(true);

                            }
                        }
                    }).execute();

                    Snackbar.make(findViewById(R.id.root), "Profile Picture Updated", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(findViewById(R.id.root), "Some error occurred", Snackbar.LENGTH_SHORT).show();
                }
            }
        }).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        profilePicView.setDefaultImageResId(R.drawable.profile1);
        profilePicView.setImageUrl(AppController.getInstance().getUserInfo().getDpURL().trim().replace(" ", "%20") + "?" + System.currentTimeMillis(), AppController.getInstance().getImageLoader());
        profilePicView.setErrorImageResId(R.drawable.profile1);
    }

    @Override
    protected int getContentView() {
        return R.layout.change_dp;
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

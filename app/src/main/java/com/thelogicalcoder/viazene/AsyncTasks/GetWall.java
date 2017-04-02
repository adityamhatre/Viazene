package com.thelogicalcoder.viazene.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;

import com.thelogicalcoder.viazene.Interfaces.onWallCommentsLoadedListener;
import com.thelogicalcoder.viazene.Interfaces.onWallLoadedListener;
import com.thelogicalcoder.viazene.Server.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Aditya on 011, 11 July 2015.
 */
public class GetWall extends AsyncTask<Void, Void, Void> {

    private ProgressDialog progressDialog;
    private String response = "", commentResponse = "", line;
    private onWallLoadedListener onWallLoadedListener;
    private onWallCommentsLoadedListener onWallCommentsLoadedListener;
    private static Context context;

    public GetWall(Context context, onWallLoadedListener onWallLoadedListener, onWallCommentsLoadedListener onWallCommentsLoadedListener) {
        GetWall.context = context;
        progressDialog = new ProgressDialog(context);
        this.onWallLoadedListener = onWallLoadedListener;
        this.onWallCommentsLoadedListener = onWallCommentsLoadedListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Wall...");
        //progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            URL url = new URL(Server.GET_WALL + "?" + System.currentTimeMillis());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            String base64EncodedCredentials = Base64.encodeToString((Server.APP_DOMAIN_USERNAME + ":" + Server.APP_DOMAIN_PASSWORD).getBytes("US-ASCII"), Base64.DEFAULT);
            httpURLConnection.addRequestProperty("Authorization", "Basic " + base64EncodedCredentials);
            httpURLConnection.setConnectTimeout(10 * 1000);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            if (httpURLConnection.getResponseCode() == 200) {
                while ((line = bufferedReader.readLine()) != null) {
                    response = response + line;
                }
                if (response.trim().equals("")) {
                    response = "errorOccurred";
                } else //now get wall comments
                {
                    System.out.println("getWall" + response);
                    url = new URL(Server.GET_WALL_COMMENTS + "?" + System.currentTimeMillis());
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    String base64EncodedCredentials2 = Base64.encodeToString((Server.APP_DOMAIN_USERNAME + ":" + Server.APP_DOMAIN_PASSWORD).getBytes("US-ASCII"), Base64.DEFAULT);
                    httpURLConnection.addRequestProperty("Authorization", "Basic " + base64EncodedCredentials2);
                    httpURLConnection.setConnectTimeout(10 * 1000);
                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    if (httpURLConnection.getResponseCode() == 200) {
                        while ((line = bufferedReader.readLine()) != null) {
                            commentResponse = commentResponse + line;
                        }
                        if (commentResponse.trim().equals("")) {
                            commentResponse = "errorOccurred";
                        }
                        System.out.println("getWallComments" + commentResponse);
                    } else commentResponse = "errorOccurred";
                }
            } else response = "errorOccurred";

        } catch (Exception e) {
            e.printStackTrace();
            response = "errorOccurred";
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //progressDialog.dismiss();
        if (!response.equals("errorOccurred")) {
            onWallLoadedListener.onWallLoaded(response);
        } else {
            onWallLoadedListener.onWallLoaded(response);
            //Toast.makeText(this.context, "Cannot connect to server", Toast.LENGTH_SHORT).show();
        }

        if (!commentResponse.equals("errorOccurred")) {
            onWallCommentsLoadedListener.onWallCommentsLoaded(commentResponse);
        } else {
            onWallCommentsLoadedListener.onWallCommentsLoaded(commentResponse);
            //Toast.makeText(this.context, "Cannot connect to server", Toast.LENGTH_SHORT).show();
        }
    }
}

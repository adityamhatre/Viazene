package com.thelogicalcoder.viazene.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;

import com.thelogicalcoder.viazene.Interfaces.onServicesImagesLoadedListener;
import com.thelogicalcoder.viazene.Server.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Aditya on 011, 11 July 2015.
 */
public class GetServicesImagePaths extends AsyncTask<Void, Void, Void> {

    private static Context context;
    private ProgressDialog progressDialog;
    private String response = "", line;
    private onServicesImagesLoadedListener onServicesImagesLoadedListener;

    public GetServicesImagePaths(Context context, onServicesImagesLoadedListener onServicesImagesLoadedListener) {
        GetServicesImagePaths.context = context;
        progressDialog = new ProgressDialog(context);
        this.onServicesImagesLoadedListener = onServicesImagesLoadedListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Services Images...");
        //progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            URL url = new URL(Server.GET_PICS_OF_SERVICES + "?" + System.currentTimeMillis());
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
                }
                System.out.println("getPicService" + response);
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
        // progressDialog.dismiss();
        if (!response.equals("errorOccurred")) {
            onServicesImagesLoadedListener.onServicesImagesLoaded(response);
        } else {
            onServicesImagesLoadedListener.onServicesImagesLoaded(response);
            //Toast.makeText(this.context, "Cannot connect to server", Toast.LENGTH_SHORT).show();
        }
    }
}

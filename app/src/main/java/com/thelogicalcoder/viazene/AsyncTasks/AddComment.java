package com.thelogicalcoder.viazene.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;

import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Interfaces.onWallCallBackListener;
import com.thelogicalcoder.viazene.Server.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Aditya on 007, 07 August 2015.
 */
public class AddComment extends AsyncTask<Void, Void, Void> {

    private Boolean likeMode = true; //false=unlike post....true=like post
    private HashMap<String, String> postParams;
    private onWallCallBackListener onWallCallBackListener;
    private String response = "", line;
    private Context context;

    public AddComment(Context context, String productID, String wallID, String comment, onWallCallBackListener onWallCallBackListener) {
        this.likeMode = likeMode;
        this.context = context;
        this.onWallCallBackListener = onWallCallBackListener;
        postParams = new HashMap<>();
        postParams.put("email", AppController.getInstance().getUserInfo().getEmail());
        postParams.put("user", AppController.getInstance().getUserInfo().getUser());
        postParams.put("productID", productID);
        postParams.put("wallID", wallID);
        postParams.put("comment", comment);
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            URL url = new URL(Server.ADD_COMMENT_TO_WALL + "?" + System.currentTimeMillis());

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();String base64EncodedCredentials = Base64.encodeToString((Server.APP_DOMAIN_USERNAME + ":" + Server.APP_DOMAIN_PASSWORD).getBytes("US-ASCII"), Base64.DEFAULT);    httpURLConnection.addRequestProperty("Authorization", "Basic " + base64EncodedCredentials);

            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);


            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(FormatPostData.getPostDataString(postParams));
            writer.flush();
            writer.close();
            os.close();

            System.out.println(postParams.toString());

            if (httpURLConnection.getResponseCode() == 200) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response = response + line;
                }
                System.out.println("addComment  : " + response);
                if (response.trim().equals("")) {
                    response = "errorOccurred";
                }
            } else {
                response = "errorOccurred";
            }

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
            onWallCallBackListener.onWallCallBack(response);
        } else {
            onWallCallBackListener.onWallCallBack(response);
            //Toast.makeText(context, "Cannot connect to server", Toast.LENGTH_SHORT).show();
        }
    }
}

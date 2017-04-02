package com.thelogicalcoder.viazene.AsyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;import android.util.Base64;
import com.thelogicalcoder.viazene.Interfaces.onUpdateInfoUpdateListener;
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
 * Created by Aditya on 015, 15 July 2015.
 */
public class UpdateInfo extends AsyncTask<Void, Void, Void> {

    private ProgressDialog progressDialog;
    private String response = "", line;
    private onUpdateInfoUpdateListener onUpdateInfoUpdateListener;
    private static Context context;
    private HashMap<String, String> info;

    public UpdateInfo(Context context, String user, String dob, String address, String pinCode, String contact, String email, onUpdateInfoUpdateListener onUpdateInfoUpdateListener) {
        UpdateInfo.context = context;
        progressDialog = new ProgressDialog(context);
        this.onUpdateInfoUpdateListener = onUpdateInfoUpdateListener;
        info = new HashMap<>();
        info.put("user", user);
        info.put("dob", dob);
        info.put("address", address);
        info.put("pincode", pinCode);
        info.put("contact", contact);
        info.put("email", email);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating info...");
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            URL url = new URL(Server.UPDATE_USER_INFO + "?" + System.currentTimeMillis());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();String base64EncodedCredentials = Base64.encodeToString(        (Server.APP_DOMAIN_USERNAME + ":" + Server.APP_DOMAIN_PASSWORD).getBytes("US-ASCII"), Base64.DEFAULT);    httpURLConnection.addRequestProperty("Authorization", "Basic " + base64EncodedCredentials);

            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);


            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(FormatPostData.getPostDataString(info));
            writer.flush();
            writer.close();
            os.close();

            System.out.println(info.toString());


            if (httpURLConnection.getResponseCode() == 200) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response = response + line;
                }
                System.out.println("updateUserInfo: " + response);
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
        progressDialog.dismiss();
        if (!response.equals("errorOccurred")) {
            onUpdateInfoUpdateListener.onUpdateInfoUpdated(response);
        } else {
            onUpdateInfoUpdateListener.onUpdateInfoUpdated(response);
//            Toast.makeText(context, "Cannot connect to server", Toast.LENGTH_SHORT).show();
        }
    }


}

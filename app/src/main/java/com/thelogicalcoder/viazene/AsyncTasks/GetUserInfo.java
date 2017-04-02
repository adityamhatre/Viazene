package com.thelogicalcoder.viazene.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;import android.util.Base64;
import com.thelogicalcoder.viazene.Interfaces.onUserInfoLoadedListener;
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
 * Created by Aditya on 011, 11 July 2015.
 */
public class GetUserInfo extends AsyncTask<Void, Void, Void> {

   // private ProgressDialog progressDialog;
    private String response = "", line, email;
    private onUserInfoLoadedListener onUserInfoLoadedListener;
    private static Context context;
    private HashMap<String, String> dpForEmail;

    public GetUserInfo(Context context, String email, onUserInfoLoadedListener onUserInfoLoadedListener) {
        GetUserInfo.context = context;
        this.email = email;
        dpForEmail = new HashMap<>();
        dpForEmail.put("email", email);
      //  progressDialog = new ProgressDialog(context);
        this.onUserInfoLoadedListener = onUserInfoLoadedListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
     //   progressDialog.setIndeterminate(true);
    //    progressDialog.setMessage("Fetching your details...");
       // progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            URL url = new URL(Server.GET_USER_INFO + "?" + System.currentTimeMillis());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();String base64EncodedCredentials = Base64.encodeToString(        (Server.APP_DOMAIN_USERNAME + ":" + Server.APP_DOMAIN_PASSWORD).getBytes("US-ASCII"), Base64.DEFAULT);    httpURLConnection.addRequestProperty("Authorization", "Basic " + base64EncodedCredentials);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);


            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(FormatPostData.getPostDataString(dpForEmail));
            writer.flush();
            writer.close();
            os.close();


            if (httpURLConnection.getResponseCode() == 200) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response = response + line;
                }
                System.out.println("getUserInfo" + response);
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
       // progressDialog.dismiss();
        if (!response.equals("errorOccurred")) {
            onUserInfoLoadedListener.onUserInfoLoaded(response);
        } else {
            onUserInfoLoadedListener.onUserInfoLoaded(response);
            //Toast.makeText(this.context, "Cannot connect to server", Toast.LENGTH_SHORT).show();
        }
    }


}

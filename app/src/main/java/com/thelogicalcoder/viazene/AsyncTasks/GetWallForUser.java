package com.thelogicalcoder.viazene.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;import android.util.Base64;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Interfaces.onUserWallLoadListener;
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
public class GetWallForUser extends AsyncTask<Void, Void, Void> {

    private String response = "", line;
    private onUserWallLoadListener onUserWallLoadListener;
    private static Context context;
    private HashMap<String, String> authentication;

    public GetWallForUser(Context context, onUserWallLoadListener onUserWallLoadListener) {
        GetWallForUser.context = context;
        this.onUserWallLoadListener = onUserWallLoadListener;
        authentication = new HashMap<>();
        authentication.put("email", AppController.getInstance().getUserInfo().getEmail());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            URL url = new URL(Server.GET_WALL_OF_USER + "?" + System.currentTimeMillis());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();String base64EncodedCredentials = Base64.encodeToString(        (Server.APP_DOMAIN_USERNAME + ":" + Server.APP_DOMAIN_PASSWORD).getBytes("US-ASCII"), Base64.DEFAULT);    httpURLConnection.addRequestProperty("Authorization", "Basic " + base64EncodedCredentials);

            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);


            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(FormatPostData.getPostDataString(authentication));
            writer.flush();
            writer.close();
            os.close();

            System.out.println(authentication.toString());


            if (httpURLConnection.getResponseCode() == 200) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response = response + line;
                }
                System.out.println("getWallForUser: " + response);
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
            onUserWallLoadListener.onUserWallLoaded(response);
        } else {
            onUserWallLoadListener.onUserWallLoaded(response);
            //Toast.makeText(context, "Cannot connect to server", Toast.LENGTH_SHORT).show();
        }
    }


}

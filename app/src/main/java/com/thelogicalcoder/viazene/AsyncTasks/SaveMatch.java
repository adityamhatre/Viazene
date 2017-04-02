package com.thelogicalcoder.viazene.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;import android.util.Base64;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Data.ProductData;
import com.thelogicalcoder.viazene.Interfaces.onMatchSavedListener;
import com.thelogicalcoder.viazene.Server.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Aditya on 015, 15 July 2015.
 */
public class SaveMatch extends AsyncTask<Void, Void, Void> {

    private ProgressDialog progressDialog;
    private String response = "", line;
    private onMatchSavedListener onMatchSavedListener;
    private static Context context;
    private HashMap<String, String> matchData;
    private Boolean editMode = false;
    private URL url;

    public SaveMatch(Context context, String matchName, List<ProductData> selectedMatchesList, Boolean editMode, onMatchSavedListener onMatchSavedListener) {
        SaveMatch.context = context;
        progressDialog = new ProgressDialog(context);
        this.editMode = editMode;
        this.onMatchSavedListener = onMatchSavedListener;
        matchData = new HashMap<>();
        matchData.put("email", AppController.getInstance().getUserInfo().getEmail());
        matchData.put("matchName", matchName);

        try {
            matchData.put("product1", selectedMatchesList.get(0).getProductID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            matchData.put("product2", selectedMatchesList.get(1).getProductID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            matchData.put("product3", selectedMatchesList.get(2).getProductID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            matchData.put("product4", selectedMatchesList.get(3).getProductID());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setIndeterminate(true);
        if (editMode) progressDialog.setMessage("Updating Match...");
        else
            progressDialog.setMessage("Saving Match...");
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            if (editMode) {
                url = new URL(Server.EDIT_MATCH + "?" + System.currentTimeMillis());
            } else url = new URL(Server.SAVE_MATCH + "?" + System.currentTimeMillis());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();String base64EncodedCredentials = Base64.encodeToString(        (Server.APP_DOMAIN_USERNAME + ":" + Server.APP_DOMAIN_PASSWORD).getBytes("US-ASCII"), Base64.DEFAULT);    httpURLConnection.addRequestProperty("Authorization", "Basic " + base64EncodedCredentials);

            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);


            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(FormatPostData.getPostDataString(matchData));
            writer.flush();
            writer.close();
            os.close();

            System.out.println(matchData.toString());


            if (httpURLConnection.getResponseCode() == 200) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response = response + line;
                }
                System.out.println("saveMatch: " + response);
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
            onMatchSavedListener.onMatchSaved(response);
        } else {
            onMatchSavedListener.onMatchSaved(response);
            //Toast.makeText(context, "Cannot connect to server", Toast.LENGTH_SHORT).show();
        }
    }


}

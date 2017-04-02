package com.thelogicalcoder.viazene.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;

import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Data.ProductData;
import com.thelogicalcoder.viazene.Interfaces.onAsyncCallBack;
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
 * Created by Aditya on 022, 22 July 2015.
 */
public class BuyProductCODAsync extends AsyncTask<Void, Void, Void> {

    private static Context context;
    private ProductData productData;
    private String response = "", line;
    private onAsyncCallBack onAsyncCallBack;
    private HashMap<String, String> postParams;

    public BuyProductCODAsync(Context context, ProductData productData, String orderID, onAsyncCallBack onAsyncCallBack) {
        BuyProductCODAsync.context = context;
        this.productData = productData;
        this.onAsyncCallBack = onAsyncCallBack;
        postParams = new HashMap<>();
        postParams.put("productID", productData.getProductID());
        postParams.put("email", AppController.getInstance().getUserInfo().getEmail());
        postParams.put("orderID", orderID);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progressDialog.setIndeterminate(true);
        //progressDialog.setMessage("Registering...");
        //progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            URL url = new URL(Server.BUY_PRODUCT + "?" + System.currentTimeMillis());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            String base64EncodedCredentials = Base64.encodeToString((Server.APP_DOMAIN_USERNAME + ":" + Server.APP_DOMAIN_PASSWORD).getBytes("US-ASCII"), Base64.DEFAULT);
            httpURLConnection.addRequestProperty("Authorization", "Basic " + base64EncodedCredentials);

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
                System.out.println("buyProduct: " + response);
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
            onAsyncCallBack.onCallBack(response);
        } else {
            onAsyncCallBack.onCallBack(response);
            //Toast.makeText(context, "Cannot connect to server", Toast.LENGTH_SHORT).show();
        }
    }
}

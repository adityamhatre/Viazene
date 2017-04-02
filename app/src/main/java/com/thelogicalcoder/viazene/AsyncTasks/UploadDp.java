package com.thelogicalcoder.viazene.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.View;

import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Interfaces.UploadDPListener;
import com.thelogicalcoder.viazene.R;
import com.thelogicalcoder.viazene.Server.Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Aditya on 004, 04 August 2015.
 */
public class UploadDp extends AsyncTask<Void, Void, Void> {
    private String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection httpConn;
    private String charset = "UTF-8";
    private OutputStream outputStream;
    private PrintWriter writer;
    String response = "";
    String line = "";
    static Context context;
    File file;
    private UploadDPListener uploadDPListener;
    AlertDialog profileLoadingDialog;

    public UploadDp(Context context, File file, UploadDPListener uploadDPListener) {
        UploadDp.context = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(View.inflate(context, R.layout.profile_loading_dialog, null));
        profileLoadingDialog = builder.create();
        this.file = file;
        this.uploadDPListener = uploadDPListener;

    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                .append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(
                LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName  name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile)
            throws IOException {
        String fileName = uploadFile.getName();
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append(
                "Content-Disposition: form-data; name=\"" + fieldName
                        + "\"; filename=\"" + fileName + "\"")
                .append(LINE_FEED);
        writer.append(
                "Content-Type: "
                        + URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();

        writer.append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a header field to the request.
     *
     * @param name  - name of the header field
     * @param value - value of the header field
     */
    public void addHeaderField(String name, String value) {
        writer.append(name + ": " + value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Completes the request and receives response from the server.
     *
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public String finish() throws IOException {
        writer.append(LINE_FEED).flush();
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.close();

        System.out.println(LINE_FEED);
        // checks server's status code first
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            while ((line = reader.readLine()) != null) {
                response = response + line;
            }
            reader.close();
            httpConn.disconnect();
        } else {
            response = "errorOccurred";
            throw new IOException("Server returned non-OK status: " + status);
        }

        return response;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        profileLoadingDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            boundary = "===" + System.currentTimeMillis() + "===";

            URL url = new URL(Server.PROFILE_PIC_UPLOAD + "?" + System.currentTimeMillis());
            httpConn = (HttpURLConnection) url.openConnection();
            String base64EncodedCredentials = Base64.encodeToString((Server.APP_DOMAIN_USERNAME + ":" + Server.APP_DOMAIN_PASSWORD).getBytes("US-ASCII"), Base64.DEFAULT);
            httpConn.addRequestProperty("Authorization", "Basic " + base64EncodedCredentials);
            httpConn.setUseCaches(false);
            httpConn.setDoOutput(true); // indicates POST method
            httpConn.setDoInput(true);
            httpConn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + boundary);
            outputStream = httpConn.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                    true);

            addFormField("email", AppController.getInstance().getUserInfo().getEmail().trim());
            addFilePart("fileToUpload", file);

            response = finish();

            System.out.println("UploadPic: " + response);
        } catch (Exception e) {
            response = "errorOccurred";
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        profileLoadingDialog.dismiss();

        if (!response.equals("errorOccurred")) {
            uploadDPListener.onDPUploaded(response);
        } else {
            uploadDPListener.onDPUploaded(response);
            //Toast.makeText(context, "Cannot connect to server", Toast.LENGTH_SHORT).show();
        }
    }
}

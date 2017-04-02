package com.thelogicalcoder.viazene.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Data.ServiceData;
import com.thelogicalcoder.viazene.R;
import com.thelogicalcoder.viazene.Server.Server;
import com.thelogicalcoder.viazene.Volley.FeedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aditya on 011, 11 July 2015.
 */
public class ServicesAdapter extends BaseAdapter {

    private Fragment fragment;
    private List<ServiceData> serviceDataList;
    private Context context;
    private LayoutInflater inflater;

    public ServicesAdapter(Context context, List<ServiceData> serviceDataList) {
        this.serviceDataList = serviceDataList;
        this.context = context;
    }

    public ServicesAdapter(Context context, List<ServiceData> serviceDataList, Fragment fragment) {
        this.serviceDataList = serviceDataList;
        this.context = context;
        this.fragment = fragment;
    }


    @Override
    public int getCount() {
        return serviceDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return serviceDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.services_card, null);
        }

        ServiceData serviceData = (ServiceData) getItem(position);
        List<String> urlList = new ArrayList<>();


        for (int i = 0; i < AppController.getInstance().getServiceImagesDataList().size(); i++) {
            if (AppController.getInstance().getServiceImagesDataList().get(i).getServiceID().trim().equalsIgnoreCase(serviceData.getServiceID().trim())) {
                urlList = AppController.getInstance().getServiceImagesDataList().get(i).getImageURLs();
            }
        }
        FeedImageView serviceBanner = (FeedImageView) convertView.findViewById(R.id.banner);
        TextView serviceText = (TextView) convertView.findViewById(R.id.textView);

        serviceText.setText(serviceData.getName().trim().toUpperCase());
        serviceBanner.setDefaultImageResId(R.drawable.logo_loading);
        serviceBanner.setImageUrl((Server.SERVICES_DOMAIN + serviceData.getCategory().trim() + "/" + serviceData.getName().trim() + "/" + urlList.get(0)).replace(" ", "%20"), AppController.getInstance().getImageLoader());
        serviceBanner.setErrorImageResId(R.drawable.error);

        return convertView;
    }
}

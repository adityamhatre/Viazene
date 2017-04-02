package com.thelogicalcoder.viazene.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thelogicalcoder.viazene.Activities.SingleProductAlertView;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Data.ProductData;
import com.thelogicalcoder.viazene.R;
import com.thelogicalcoder.viazene.Server.Server;
import com.thelogicalcoder.viazene.Volley.FeedImageView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Aditya on 018, 18 July 2015.
 */
public class PotentialMatchesAdapter extends BaseAdapter {
    private Context context;
    private List<ProductData> productDataList;
    private LayoutInflater inflater;
    private List<ProductData> selectedProducts;

    public PotentialMatchesAdapter(Context context, List<ProductData> productDataList, List<ProductData> selectedProducts, boolean editMode) {
        this.context = context;
        this.productDataList = new LinkedList<>(productDataList);
        this.selectedProducts = selectedProducts;

        if (!editMode) {
            for (int i = 0; i < productDataList.size(); i++) {
                if (productDataList.get(i).equals(selectedProducts.get(0))) {
                    this.productDataList.remove(selectedProducts.get(0));
                }
            }
        }

    }

    @Override
    public int getCount() {
        return productDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return productDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.match_card, null);

        FeedImageView staticProductImage = (FeedImageView) convertView.findViewById(R.id.staticProductImage);
        TextView staticProductName = (TextView) convertView.findViewById(R.id.staticProductName);
        RelativeLayout rootLayout = (RelativeLayout) convertView.findViewById(R.id.root);
        ImageView infoButton = (ImageView) convertView.findViewById(R.id.infoMatch);
        List<String> urlList = new ArrayList<>();

        for (int i = 0; i < AppController.getInstance().getProductImagesDataList().size(); i++) {
            if (AppController.getInstance().getProductImagesDataList().get(i).getProductID().trim().equalsIgnoreCase(productDataList.get(position).getProductID().trim())) {
                urlList = AppController.getInstance().getProductImagesDataList().get(i).getImageURLs();
            }
        }

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, SingleProductAlertView.class).putExtra("productID", productDataList.get(position).getProductID()));
            }
        });

        staticProductImage.setDefaultImageResId(R.drawable.logo_loading);
        staticProductImage.setErrorImageResId(R.drawable.error);
        staticProductImage.setImageUrl((Server.PRODUCTS_DOMAIN + productDataList.get(position).getProductCategory().trim() + "/" + productDataList.get(position).getProductSubCategory() + "/" + productDataList.get(position).getProductName().trim() + "/" + urlList.get(0)).replace(" ", "%20"), AppController.getInstance().getImageLoader());
        staticProductImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        staticProductName.setText(productDataList.get(position).getProductName());
        if (selectedProducts.contains(productDataList.get(position))) {
            rootLayout.setBackgroundColor(Color.parseColor("#7700ff00"));
        } else rootLayout.setBackgroundColor(Color.parseColor("#00000000"));
        return convertView;

    }
}

package com.thelogicalcoder.viazene.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.thelogicalcoder.viazene.Activities.MatchMakerActivity;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Data.ProductData;
import com.thelogicalcoder.viazene.Fragment.HomeFragment;
import com.thelogicalcoder.viazene.R;
import com.thelogicalcoder.viazene.Server.Server;
import com.thelogicalcoder.viazene.Volley.FeedImageView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Aditya on 018, 18 July 2015.
 */
public class SelectedMatchesAdapter extends BaseAdapter {
    private Context context;
    private List<ProductData> productDataList;
    private LayoutInflater inflater;
    private boolean editMode;
    private HomeFragment homeFragment = null;


    public SelectedMatchesAdapter(Context context, List<ProductData> productDataList, boolean editMode) {
        this.context = context;
        this.productDataList = productDataList;
        this.editMode = editMode;
    }

    public SelectedMatchesAdapter(Context context, LinkedList<ProductData> productDataList, boolean editMode, HomeFragment homeFragment) {
        this.context = context;
        this.productDataList = productDataList;
        this.editMode = editMode;
        this.homeFragment = homeFragment;
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
            convertView = inflater.inflate(R.layout.selected_match_card, null);

        FeedImageView staticProductImage = (FeedImageView) convertView.findViewById(R.id.staticProductImage);
        TextView staticProductName = (TextView) convertView.findViewById(R.id.staticProductName);
        final ImageView removeIcon = (ImageView) convertView.findViewById(R.id.removeIcon);

        List<String> urlList = new ArrayList<>();


        for (int i = 0; i < AppController.getInstance().getProductImagesDataList().size(); i++) {
            if (AppController.getInstance().getProductImagesDataList().get(i).getProductID().trim().equalsIgnoreCase(productDataList.get(position).getProductID().trim())) {
                urlList = AppController.getInstance().getProductImagesDataList().get(i).getImageURLs();
            }
        }
        if (editMode) {
            removeIcon.setVisibility(View.VISIBLE);
            removeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productDataList.remove(position);
                    SelectedMatchesAdapter.this.notifyDataSetChanged();
                    ((MatchMakerActivity) context).updateActionBarPriceVolume();
                    ((MatchMakerActivity) context).notifyDataSetChangedPotentialMatches();

                }
            });
        } else {
            if (homeFragment == null) {
                if (position == 0)
                    removeIcon.setVisibility(View.GONE);
                else {
                    removeIcon.setVisibility(View.VISIBLE);
                    removeIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            productDataList.remove(position);
                            SelectedMatchesAdapter.this.notifyDataSetChanged();
                            ((MatchMakerActivity) context).updateActionBarPriceVolume();
                            ((MatchMakerActivity) context).notifyDataSetChangedPotentialMatches();

                        }
                    });
                }
            }else{
                removeIcon.setVisibility(View.GONE);
            }
        }

        staticProductImage.setDefaultImageResId(R.drawable.logo_loading);
        staticProductImage.setErrorImageResId(R.drawable.error);
        staticProductImage.setImageUrl((Server.PRODUCTS_DOMAIN + productDataList.get(position).getProductCategory().trim() + "/" + productDataList.get(position).getProductSubCategory() + "/" + productDataList.get(position).getProductName().trim() + "/" + urlList.get(0)).replace(" ", "%20"), AppController.getInstance().getImageLoader());
        staticProductImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

        staticProductName.setText(productDataList.get(position).getProductName());
        return convertView;
    }
}

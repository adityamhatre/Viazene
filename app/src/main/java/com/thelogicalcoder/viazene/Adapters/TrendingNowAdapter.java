package com.thelogicalcoder.viazene.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Data.ProductData;
import com.thelogicalcoder.viazene.R;
import com.thelogicalcoder.viazene.Volley.FeedImageView;

import java.util.List;

import babushkatext.BabushkaText;

/**
 * Created by Aditya on 011, 11 July 2015.
 */
public class TrendingNowAdapter extends BaseAdapter {

    private Boolean isCart = false;
    private List<ProductData> productDataList;
    private ProductData productData;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;


    static class ViewHolder {
        TextView title;
        BabushkaText productPrice;
        FeedImageView productImage;
        ImageView removeIcon;
        MaterialRippleLayout removeHolder;

    }


    public TrendingNowAdapter(Context context, List<ProductData> productDataList) {
        this.productDataList = productDataList;
        this.context = context;
    }

    public TrendingNowAdapter(Context context, ProductData productData) {
        this.productData = productData;
        this.context = context;
    }

    public TrendingNowAdapter(Context context, List<ProductData> productDataList, Boolean isCart, Fragment fragment) {
        this.productDataList = productDataList;
        this.context = context;
        this.isCart = isCart;
        this.fragment = fragment;
    }


    public List<ProductData> getDataSet() {
        return this.productDataList;
    }

    @Override
    public int getCount() {
        //if (!(productDataList == null))
        return productDataList.size();
        // else return 1;
    }

    @Override
    public Object getItem(int position) {
        // if (!(productDataList == null))
        return productDataList.get(position);
        // else return productData;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.trending_now_card, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.productName);
            viewHolder.productPrice = (BabushkaText) convertView.findViewById(R.id.productPrice);
            viewHolder.productImage = (FeedImageView) convertView.findViewById(R.id.productImage);
            viewHolder.removeHolder = (MaterialRippleLayout) convertView.findViewById(R.id.removeHolder);
            viewHolder.removeIcon = (ImageView) convertView.findViewById(R.id.removeIcon);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ProductData productData = (ProductData) getItem(position);

        viewHolder.title.setText(productData.getProductName());
        viewHolder.productPrice.reset();
        viewHolder.productPrice.addPiece(new BabushkaText.Piece.Builder("Rs. " + productData.getProductPrice() + "\n").textSize(80).build());
        viewHolder.productPrice.display();
        viewHolder.productImage.setDefaultImageResId(R.drawable.logo_loading);
        viewHolder.productImage.setErrorImageResId(R.drawable.error);
        viewHolder.productImage.setImageUrl(productData.getProductURL().replace(" ", "%20") + "/image1.jpg", AppController.getInstance().getImageLoader());

        if (isCart) {
            viewHolder.removeHolder.setVisibility(View.VISIBLE);
        } else {
            viewHolder.removeHolder.setVisibility(View.GONE);
        }

        return convertView;
    }
}

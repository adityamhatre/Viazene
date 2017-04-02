package com.thelogicalcoder.viazene.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Data.ProductData;
import com.thelogicalcoder.viazene.R;
import com.thelogicalcoder.viazene.Server.Server;
import com.thelogicalcoder.viazene.Volley.FeedImageView;

import java.util.ArrayList;
import java.util.List;

import babushkatext.BabushkaText;

/**
 * Created by Aditya on 011, 11 July 2015.
 */
public class MyOrdersAdapter extends BaseAdapter {

    private List<ProductData> productDataList;
    private ProductData productData;
    private Context context;
    private LayoutInflater inflater;
    private List<String> orders;

    static class ViewHolder {
        TextView title;
        BabushkaText productPrice;
        FeedImageView productImage;

    }


    public MyOrdersAdapter(Context context, List<ProductData> productDataList, List<String> orders) {
        this.productDataList = productDataList;
        this.orders = orders;
        this.context = context;
    }

    public MyOrdersAdapter(Context context, ProductData productData) {
        this.productData = productData;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.product_card, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.productName);
            viewHolder.productPrice = (BabushkaText) convertView.findViewById(R.id.productPrice);
            viewHolder.productImage = (FeedImageView) convertView.findViewById(R.id.productImage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ProductData productData = (ProductData) getItem(position);
        List<String> urlList = new ArrayList<>();


        for (int i = 0; i < AppController.getInstance().getProductImagesDataList().size(); i++) {
            if (AppController.getInstance().getProductImagesDataList().get(i).getProductID().trim().equalsIgnoreCase(productData.getProductID().trim())) {
                urlList = AppController.getInstance().getProductImagesDataList().get(i).getImageURLs();
            }
        }
        viewHolder.title.setText(productData.getProductName());
        viewHolder.productPrice.reset();
        viewHolder.productPrice.addPiece(new BabushkaText.Piece.Builder(("ORDER ID: " + orders.get(position))).textSize(50).build());
        viewHolder.productPrice.display();
        viewHolder.productImage.setDefaultImageResId(R.drawable.logo_loading);
        viewHolder.productImage.setErrorImageResId(R.drawable.error);
        viewHolder.productImage.setImageUrl((Server.PRODUCTS_DOMAIN + productData.getProductCategory().trim() + "/" + productData.getProductSubCategory() + "/" + productData.getProductName().trim() + "/" + urlList.get(0)).replace(" ", "%20"), AppController.getInstance().getImageLoader());

        return convertView;
    }
}

package com.thelogicalcoder.viazene.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.AsyncTasks.CartOperation;
import com.thelogicalcoder.viazene.AsyncTasks.GetProfile;
import com.thelogicalcoder.viazene.Data.ProductData;
import com.thelogicalcoder.viazene.Fragment.MyCartFragment;
import com.thelogicalcoder.viazene.Interfaces.onCartCallBackListener;
import com.thelogicalcoder.viazene.Interfaces.onProfileLoadedListener;
import com.thelogicalcoder.viazene.JSONParsers.ProfileDetailsParser;
import com.thelogicalcoder.viazene.R;
import com.thelogicalcoder.viazene.Server.Server;
import com.thelogicalcoder.viazene.Volley.FeedImageView;

import java.util.ArrayList;
import java.util.List;

import babushkatext.BabushkaText;

/**
 * Created by Aditya on 011, 11 July 2015.
 */
public class ProductsAdapter extends BaseAdapter {

    private Boolean isCart = false;
    private List<ProductData> productDataList;
    private ProductData productData;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;


    public ProductsAdapter(Context context, List<ProductData> productDataList) {
        this.productDataList = productDataList;
        this.context = context;
    }


    public ProductsAdapter(Context context, ProductData productData) {
        this.productData = productData;
        this.context = context;
    }

    public ProductsAdapter(Context context, List<ProductData> productDataList, Boolean isCart, Fragment fragment) {
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
            convertView = inflater.inflate(R.layout.product_card, null);
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
        List<String> urlList = new ArrayList<>();


        for (int i = 0; i < AppController.getInstance().getProductImagesDataList().size(); i++) {
            if (AppController.getInstance().getProductImagesDataList().get(i).getProductID().trim().equalsIgnoreCase(productData.getProductID().trim())) {
                urlList = AppController.getInstance().getProductImagesDataList().get(i).getImageURLs();
            }
        }
        viewHolder.title.setText(productData.getProductName());
        viewHolder.productPrice.reset();
        viewHolder.productPrice.addPiece(new BabushkaText.Piece.Builder("Rs. " + productData.getProductPrice() + "\n").textSize(80).build());
        viewHolder.productPrice.addPiece(new BabushkaText.Piece.Builder("MRP: ").textSize(50).build());
        viewHolder.productPrice.addPiece(new BabushkaText.Piece.Builder("Rs. " + productData.getProductPriceBeforeDiscount()).strike().textSize(50).build());
        viewHolder.productPrice.addPiece(new BabushkaText.Piece.Builder("   " + productData.getProductDiscount() + "% OFF\n").textColor(Color.parseColor("#29A6A6")).textSize(50).build());
        viewHolder.productPrice.addPiece(new BabushkaText.Piece.Builder(("You save: Rs. " + productData.getProductPriceSaved())).textSize(50).build());
        viewHolder.productPrice.display();
        viewHolder.productImage.setDefaultImageResId(R.drawable.logo_loading);
        viewHolder.productImage.setErrorImageResId(R.drawable.error);
        viewHolder.productImage.setImageUrl((Server.PRODUCTS_DOMAIN + productData.getProductCategory().trim() + "/" + productData.getProductSubCategory() + "/" + productData.getProductName().trim() + "/" + urlList.get(0)).replace(" ", "%20"), AppController.getInstance().getImageLoader());

        if (isCart) {
            viewHolder.removeHolder.setVisibility(View.VISIBLE);
            viewHolder.removeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    AppController.getInstance().showMaterialProgress((Activity) context);
                    new CartOperation(context, false, productData.getProductID().trim(), new onCartCallBackListener() {
                        @Override
                        public void onCartCallBack(String response) {
                            if (!response.equalsIgnoreCase("errorOccurred")) {
                                new GetProfile(context, AppController.getInstance().getUserInfo().getEmail(), new onProfileLoadedListener() {
                                    @Override
                                    public void onProfileLoaded(String response) {
                                        AppController.getInstance().dismissMaterialProgress();
                                        if (!response.equalsIgnoreCase("errorOccurred")) {
                                            new ProfileDetailsParser(response);
                                            productDataList.remove(position);
                                            ProductsAdapter.this.notifyDataSetChanged();
                                            if (productDataList.size() == 0)
                                                ((MyCartFragment) fragment).showEmptyCart();
                                        } else {
                                            Snackbar.make(v, "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                        }
                                    }
                                }).execute();

                            } else {
                                Snackbar.make(v, "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }).execute();
                }
            });
        } else {
            viewHolder.removeHolder.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView title;
        BabushkaText productPrice;
        FeedImageView productImage;
        ImageView removeIcon;
        MaterialRippleLayout removeHolder;

    }
}

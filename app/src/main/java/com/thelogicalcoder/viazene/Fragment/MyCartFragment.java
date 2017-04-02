package com.thelogicalcoder.viazene.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.thelogicalcoder.viazene.Activities.SingleProductView;
import com.thelogicalcoder.viazene.Adapters.ProductsAdapter;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.AsyncTasks.GetProfile;
import com.thelogicalcoder.viazene.Data.ProductData;
import com.thelogicalcoder.viazene.Helper.Data;
import com.thelogicalcoder.viazene.Interfaces.onProfileLoadedListener;
import com.thelogicalcoder.viazene.JSONParsers.ProfileDetailsParser;
import com.thelogicalcoder.viazene.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyCartFragment extends android.support.v4.app.Fragment {

    AlertDialog profileLoadingDialog;
    ListView matchesList, cartList;
    TextView emptyMatch, emptyCart;
    View rootView;
    private MyCartFragment mInstance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        rootView = view;
        this.mInstance = this;
        cartList = (ListView) view.findViewById(R.id.cartList);
        emptyCart = (TextView) view.findViewById(R.id.emptyCart);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(View.inflate(getActivity(), R.layout.profile_loading_dialog, null));
        profileLoadingDialog = builder.create();
        profileLoadingDialog.show();

        AppController.getInstance().setMainActivityWhichClass(MyCartFragment.class);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetProfile(getActivity(), AppController.getInstance().getUserInfo().getEmail(), new onProfileLoadedListener() {
            @Override
            public void onProfileLoaded(String response) {
                profileLoadingDialog.dismiss();
                if (!response.trim().equalsIgnoreCase("errorOccurred")) {
                    new ProfileDetailsParser(response);


                    final List<ProductData> productDataList = new ArrayList<ProductData>();

                    if (AppController.getInstance().getUserProfile().getUserCart().size() != 0) {
                        for (int i = 0; i < AppController.getInstance().getUserProfile().getUserCart().size(); i++) {
                            productDataList.add(new Data().getProductFromID(AppController.getInstance().getUserProfile().getUserCart().get(i)));
                        }
                        /*cartList.setAdapter(new MaterialArrayAdapter(
                                getActivity().getApplicationContext(),
                                R.layout.material_simple_list_item_1,
                                AppController.getInstance().getUserProfile().getUserCart(),
                                "cart"
                        ));*/
                        Collections.reverse(productDataList);
                        cartList.setAdapter(new ProductsAdapter(getActivity(), productDataList, true, mInstance));
                        cartList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                startActivity(new Intent(getActivity(), SingleProductView.class).putExtra("productID", AppController.getInstance().getUserProfile().getUserCart().get((cartList.getCount() - 1) - position)));
                            }
                        });
                    } else {
                        emptyCart.setVisibility(View.VISIBLE);
                        cartList.setVisibility(View.GONE);
                    }
                } else
                    Snackbar.make(rootView.findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
            }
        }).execute();
    }

    public void showEmptyCart() {
        emptyCart.setVisibility(View.VISIBLE);
        cartList.setVisibility(View.GONE);
    }
}

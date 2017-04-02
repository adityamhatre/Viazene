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
import com.thelogicalcoder.viazene.Adapters.MyOrdersAdapter;
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

public class MyOrdersFragment extends android.support.v4.app.Fragment {

    AlertDialog profileLoadingDialog;
    ListView matchesList, boughtList;
    TextView emptyMatch, emptyBought;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        rootView = view;
        boughtList = (ListView) view.findViewById(R.id.ordersList);
        emptyBought = (TextView) view.findViewById(R.id.emptyOrders);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(View.inflate(getActivity(), R.layout.profile_loading_dialog, null));
        profileLoadingDialog = builder.create();
        profileLoadingDialog.show();

        AppController.getInstance().setMainActivityWhichClass(MyOrdersFragment.class);

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
                    List<String> productIDs = new ArrayList<String>();
                    List<ProductData> productDataList = new ArrayList<ProductData>();
                    for (int i = 0; i < AppController.getInstance().getUserProfile().getUserBought().size(); i++) {
                        productIDs.add(AppController.getInstance().getUserProfile().getUserBought().get(i).getProductID());
                        productDataList.add(new Data().getProductFromID(productIDs.get(i)));
                    }
                    Collections.reverse(productDataList);
                    List<String> orderIDs = new ArrayList<String>();
                    for (int i = 0; i < AppController.getInstance().getUserProfile().getUserBought().size(); i++) {
                        orderIDs.add(AppController.getInstance().getUserProfile().getUserBought().get(i).getOrderID());
                    }
                    Collections.reverse(orderIDs);
                    List<String> productsOrders = new ArrayList<String>();
                    for (int i = 0; i < productIDs.size(); i++) {
                        productsOrders.add(productIDs.get(i) + "\nOrder ID: " + orderIDs.get(i));
                        System.out.println(productsOrders.get(i));
                    }
                    if (productIDs.size() != 0) {
                        /*boughtList.setAdapter(new MaterialArrayAdapter(
                                getActivity().getApplicationContext(),
                                R.layout.material_simple_list_item_1,
                                productsOrders,
                                "orders"
                        ));*/
                        boughtList.setAdapter(new MyOrdersAdapter(getActivity(), productDataList, orderIDs));
                        boughtList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                startActivity(new Intent(getActivity(), SingleProductView.class).putExtra("productID", AppController.getInstance().getUserProfile().getUserBought().get((boughtList.getCount()-1) - position).getProductID()));
                            }
                        });
                    } else {
                        emptyBought.setVisibility(View.VISIBLE);
                        boughtList.setVisibility(View.GONE);
                    }
                } else
                    Snackbar.make(rootView.findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
            }
        }).execute();
    }
}

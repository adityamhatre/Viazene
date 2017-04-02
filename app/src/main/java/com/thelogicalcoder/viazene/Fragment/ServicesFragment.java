package com.thelogicalcoder.viazene.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;

import com.blunderer.materialdesignlibrary.fragments.ListViewFragment;
import com.thelogicalcoder.viazene.Activities.ServiceSearchActivity;
import com.thelogicalcoder.viazene.Activities.SingleServiceView;
import com.thelogicalcoder.viazene.Activities.ViewAllServices;
import com.thelogicalcoder.viazene.Adapters.ServicesLinearLayoutAdapter;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Helper.Data;
import com.thelogicalcoder.viazene.R;

import java.util.ArrayList;
import java.util.List;

public class ServicesFragment extends ListViewFragment implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout swipeRefreshLayout;
    ServicesLinearLayoutAdapter servicesLinearLayoutAdapter;
    View rootView;
    AutoCompleteTextView autoCompleteTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        rootView = view;
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.color_primary);
        swipeRefreshLayout.setOnRefreshListener(this);

        autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.searchText);
        final List<String> serviceNames = new ArrayList<>();
        for (int i = 0; i < AppController.getInstance().getServices().getServices().size(); i++) {
            if (AppController.getInstance().isLoggedIn()) {
                if (AppController.getInstance().getServices().getServices().get(i).getPinCodeAvailableList().contains(AppController.getInstance().getUserInfo().getPincode()) || AppController.getInstance().getServices().getServices().get(i).getPinCodeAvailable().equalsIgnoreCase("all"))  {
                    serviceNames.add(AppController.getInstance().getServices().getServices().get(i).getName());
                }
            } else
                serviceNames.add(AppController.getInstance().getServices().getServices().get(i).getName());
        }
        for (int i = 0; i < AppController.getInstance().getServices().getServices().size(); i++) {
            if (!serviceNames.contains("Service Category: " + AppController.getInstance().getServices().getServices().get(i).getCategory())) {
                serviceNames.add("Service Category: " + AppController.getInstance().getServices().getServices().get(i).getCategory());
            }
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, serviceNames);
        autoCompleteTextView.setAdapter(arrayAdapter);
        autoCompleteTextView.setThreshold(1);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (selection.contains("Category: ")) {
                    startActivity(new Intent(getActivity(), ServiceSearchActivity.class).putExtra("searchQuery", autoCompleteTextView.getText().toString()));
                } else
                    startActivity(new Intent(getActivity(), SingleServiceView.class).putExtra("serviceID", (new Data().getServiceFromName(selection)).getServiceID()));
            }
        });

        view.findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoCompleteTextView.getText().toString().isEmpty()) {
                    Snackbar.make(rootView, "Enter a search query", Snackbar.LENGTH_SHORT).show();
                } else
                    startActivity(new Intent(getActivity(), ServiceSearchActivity.class).putExtra("searchQuery", autoCompleteTextView.getText().toString()));
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public ListAdapter getListAdapter() {
        List<String> serviceCategoryDataList = AppController.getInstance().getServiceCategories();
        servicesLinearLayoutAdapter = new ServicesLinearLayoutAdapter(getActivity(), serviceCategoryDataList);
        return servicesLinearLayoutAdapter;
    }


    @Override
    public boolean useCustomContentView() {
        return true;
    }

    @Override
    public int getCustomContentView() {
        return R.layout.fragment_services;
    }


    @Override
    public boolean pullToRefreshEnabled() {
        return false;
    }

    @Override
    public int[] getPullToRefreshColorResources() {
        return new int[0];
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        startActivity(new Intent(getActivity(), ViewAllServices.class).putExtra("position", position));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
//        Collections.shuffle(AppController.getInstance().getProductDataList());
//        productsAdapter.notifyDataSetChanged();
    }
}


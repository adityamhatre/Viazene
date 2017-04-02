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

import com.thelogicalcoder.viazene.Activities.SingleServiceView;
import com.thelogicalcoder.viazene.Adapters.ServicesAdapter;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.AsyncTasks.GetProfile;
import com.thelogicalcoder.viazene.Data.ServiceData;
import com.thelogicalcoder.viazene.Helper.Data;
import com.thelogicalcoder.viazene.Interfaces.onProfileLoadedListener;
import com.thelogicalcoder.viazene.JSONParsers.ProfileDetailsParser;
import com.thelogicalcoder.viazene.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyServicesFragment extends android.support.v4.app.Fragment {

    AlertDialog profileLoadingDialog;
    ListView  serviceList;
    TextView  emptyService;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_services, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        rootView = view;
        serviceList = (ListView) view.findViewById(R.id.servicesList);
        emptyService = (TextView) view.findViewById(R.id.emptyServices);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(View.inflate(getActivity(), R.layout.profile_loading_dialog, null));
        profileLoadingDialog = builder.create();
        profileLoadingDialog.show();

        AppController.getInstance().setMainActivityWhichClass(MyServicesFragment.class);

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


                     final List<ServiceData> serviceDataList = new ArrayList<>();

                    if (AppController.getInstance().getUserProfile().getUserService().size() != 0) {
                        for (int i = 0; i < AppController.getInstance().getUserProfile().getUserService().size(); i++) {
                            serviceDataList.add(new Data().getServiceFromID(AppController.getInstance().getUserProfile().getUserService().get(i)));
                        }
                        Collections.reverse(serviceDataList);

                        serviceList.setAdapter(new ServicesAdapter(getActivity(), serviceDataList));
                        serviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                startActivity(new Intent(getActivity(), SingleServiceView.class).putExtra("serviceID", AppController.getInstance().getUserProfile().getUserService().get((serviceList.getCount() - 1) - position)));
                            }
                        });
                    } else {
                        emptyService.setVisibility(View.VISIBLE);
                        serviceList.setVisibility(View.GONE);
                    }
                } else
                    Snackbar.make(rootView.findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
            }
        }).execute();
    }

    public void showEmptyCart() {
        emptyService.setVisibility(View.VISIBLE);
        serviceList.setVisibility(View.GONE);
    }
}

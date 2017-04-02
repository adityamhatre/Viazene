package com.thelogicalcoder.viazene.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.blunderer.materialdesignlibrary.activities.Activity;
import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;
import com.thelogicalcoder.viazene.Adapters.ServicesAdapter;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Data.ServiceData;
import com.thelogicalcoder.viazene.R;

import java.util.ArrayList;
import java.util.List;

import babushkatext.BabushkaText;

/**
 * Created by Aditya on 002, 2, Dec, 2015.
 */
public class ServiceSearchActivity extends Activity {
    BabushkaText emptyListText;

    @Override
    protected int getContentView() {
        return R.layout.activity_service_search;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final List<ServiceData> foundServices = new ArrayList<>();
        getSupportActionBar().setTitle("Search Results");
        List<String> foundServicesS = new ArrayList<>();
        String query = getIntent().getStringExtra("searchQuery");
        emptyListText= (BabushkaText) findViewById(R.id.emptyListText);
        if (query.contains("Service Category: ")) {
            query = query.substring(query.indexOf("Service Category: ") + 18);
            for (int j = 0; j < AppController.getInstance().getServices().getServices().size(); j++) {
                if (AppController.getInstance().getServices().getServices().get(j).getCategory().trim().equalsIgnoreCase(query.trim())) {
                    if (AppController.getInstance().isLoggedIn()) {
                        if (AppController.getInstance().getServices().getServices().get(j).getPinCodeAvailableList().contains(AppController.getInstance().getUserInfo().getPincode()) || AppController.getInstance().getServices().getServices().get(j).getPinCodeAvailable().equalsIgnoreCase("all")) {
                            foundServices.add(AppController.getInstance().getServices().getServices().get(j));
                        }
                    } else
                        foundServices.add(AppController.getInstance().getServices().getServices().get(j));
                    //query = true;
                }
            }
        } else {
            for (int j = 0; j < AppController.getInstance().getServices().getServices().size(); j++) {
                if (AppController.getInstance().getServices().getServices().get(j).getName().toLowerCase().trim().contains(query.toLowerCase().trim())) {
                    if (AppController.getInstance().isLoggedIn()) {
                        if (AppController.getInstance().getServices().getServices().get(j).getPinCodeAvailableList().contains(AppController.getInstance().getUserInfo().getPincode()) || AppController.getInstance().getServices().getServices().get(j).getPinCodeAvailable().equalsIgnoreCase("all"))  {
                            foundServices.add(AppController.getInstance().getServices().getServices().get(j));
                        }
                    } else

                        foundServices.add(AppController.getInstance().getServices().getServices().get(j));
                    //query = true;
                }
            }
        }
        ListView listView = (ListView) findViewById(R.id.list);
        for (int i = 0; i < foundServicesS.size(); i++) {
            System.out.println(foundServicesS.get(i));
        }
        listView.setAdapter(new ServicesAdapter(this, foundServices));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(ServiceSearchActivity.this, SingleServiceView.class).putExtra("serviceID", foundServices.get(position).getServiceID()));
            }
        });
        if (foundServices.size() == 0) {
            emptyListText.addPiece(new BabushkaText.Piece.Builder("Sorry, No results found for ").build());
            emptyListText.addPiece(new BabushkaText.Piece.Builder("\"" + query + "\"").style(Typeface.BOLD).build());
            emptyListText.display();
            emptyListText.setVisibility(View.VISIBLE);
        }
        //listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, foundServicesS));
    }

    @Override
    protected boolean enableActionBarShadow() {
        return false;
    }

    @Override
    protected ActionBarHandler getActionBarHandler() {
        return null;
    }
}
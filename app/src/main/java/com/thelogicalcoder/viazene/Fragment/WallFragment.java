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
import android.widget.ListAdapter;

import com.blunderer.materialdesignlibrary.fragments.ListViewFragment;
import com.thelogicalcoder.viazene.Activities.ProductListView;
import com.thelogicalcoder.viazene.Adapters.WallAdapter;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.AsyncTasks.GetWall;
import com.thelogicalcoder.viazene.Data.WallData;
import com.thelogicalcoder.viazene.Interfaces.onWallCommentsLoadedListener;
import com.thelogicalcoder.viazene.Interfaces.onWallLoadedListener;
import com.thelogicalcoder.viazene.JSONParsers.WallCommentsParser;
import com.thelogicalcoder.viazene.JSONParsers.WallParser;
import com.thelogicalcoder.viazene.R;

import java.util.List;

public class WallFragment extends ListViewFragment implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout swipeRefreshLayout;
    WallAdapter wallAdapter;
    View rootView;
    List<WallData> wallDataList;

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
        AppController.getInstance().setWallInstance(this);
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public ListAdapter getListAdapter() {
        wallDataList = AppController.getInstance().getWallDataList();
        wallAdapter = new WallAdapter(getActivity(), wallDataList);
        return wallAdapter;
    }

    public void updateWall() {
        wallDataList.clear();
        wallDataList.addAll(AppController.getInstance().getWallDataList());
        wallAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean useCustomContentView() {
        return true;
    }

    @Override
    public int getCustomContentView() {
        return R.layout.fragment_wall;
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
        new GetWall(getActivity(), new onWallLoadedListener() {
            @Override
            public void onWallLoaded(String response) {
                if (!response.equalsIgnoreCase("errorOccurred")) {
                    new WallParser(response);
                } else {
                    Snackbar.make(rootView.findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }, new onWallCommentsLoadedListener() {
            @Override
            public void onWallCommentsLoaded(String response) {
                if (!response.equalsIgnoreCase("errorOccurred")) {
                    new WallCommentsParser(response);
//                    AppController.getInstance().getWallFragment().updateWall();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                            WallFragment.this.updateWall();
                        }
                    }, 2000);
                } else {
                    Snackbar.make(rootView.findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);

                }
            }
        }).execute();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        try {
            startActivity(new Intent(getActivity(), ProductListView.class).putExtra("productID", AppController.getInstance().getWallDataList().get(position).getID()));
        } catch (Exception e) {
            Snackbar.make(rootView.findViewById(R.id.root), "Not a product", Snackbar.LENGTH_SHORT).show();
        }
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


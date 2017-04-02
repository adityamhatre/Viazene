package com.thelogicalcoder.viazene.Fragment;

import android.os.Bundle;
import android.view.View;

import com.blunderer.materialdesignlibrary.fragments.ViewPagerWithTabsFragment;
import com.blunderer.materialdesignlibrary.handlers.ViewPagerHandler;
import com.thelogicalcoder.viazene.Activities.MainActivity;
import com.thelogicalcoder.viazene.Application.AppController;

public class MainFragment extends ViewPagerWithTabsFragment {
    @Override
    protected boolean expandTabs() {
        return true;
    }

    @Override
    public ViewPagerHandler getViewPagerHandler() {
        return new ViewPagerHandler(getActivity()).addPage("Wall", new WallFragment()).addPage("Home", new HomeFragment()).addPage("Services", new ServicesFragment());
    }






    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        AppController.getInstance().setMainActivityWhichClass(MainActivity.class);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int defaultViewPagerPageSelectedPosition() {
        return 1;
    }
}

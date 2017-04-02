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

import com.thelogicalcoder.viazene.Activities.ProductListView;
import com.thelogicalcoder.viazene.Adapters.MaterialArrayAdapter;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Application.UserMatch;
import com.thelogicalcoder.viazene.AsyncTasks.GetProfile;
import com.thelogicalcoder.viazene.Interfaces.onProfileLoadedListener;
import com.thelogicalcoder.viazene.JSONParsers.ProfileDetailsParser;
import com.thelogicalcoder.viazene.R;

import java.util.Collections;
import java.util.List;

public class MyMatchesFragment extends android.support.v4.app.Fragment {

    AlertDialog profileLoadingDialog;
    ListView matchesList, boughtList;
    TextView emptyMatch, emptyBought;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_matches, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        rootView = view;
        matchesList = (ListView) view.findViewById(R.id.matchesList);
        emptyMatch = (TextView) view.findViewById(R.id.emptyMatch);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(View.inflate(getActivity(), R.layout.profile_loading_dialog, null));
        profileLoadingDialog = builder.create();
        profileLoadingDialog.show();

        AppController.getInstance().setMainActivityWhichClass(MyMatchesFragment.class);

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
                    final List<String> matchList = AppController.getInstance().getUserProfile().getUserMatchNameList();
                    Collections.reverse(matchList);
                    if (AppController.getInstance().getUserProfile().getUserMatch().size() != 0) {
                        matchesList.setAdapter(new MaterialArrayAdapter(
                                getActivity(),
                                R.layout.material_simple_list_item_1, matchList
                                ,
                                "matches"
                        ));
                        final List<UserMatch> match=AppController.getInstance().getUserProfile().getUserMatch();
                        Collections.reverse(match);
                        matchesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                startActivity(new Intent(getActivity(), ProductListView.class).putExtra("productIDs", match.get(position).getAllProductsOfMatch()).putExtra("mode", "matches").putExtra("matchName", matchList.get(position)));
                                System.out.println(AppController.getInstance().getUserProfile().getUserMatch().get(position).getAllProductsOfMatch());
                            }
                        });
                    } else {
                        emptyMatch.setVisibility(View.VISIBLE);
                        matchesList.setVisibility(View.GONE);
                    }
                } else
                    Snackbar.make(rootView.findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();

            }
        }).execute();
    }
}

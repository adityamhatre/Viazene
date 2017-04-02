package com.thelogicalcoder.viazene.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blunderer.materialdesignlibrary.activities.Activity;
import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;
import com.thelogicalcoder.viazene.Adapters.PotentialMatchesAdapter;
import com.thelogicalcoder.viazene.Adapters.SelectedMatchesAdapter;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.AsyncTasks.SaveMatch;
import com.thelogicalcoder.viazene.Data.ProductData;
import com.thelogicalcoder.viazene.Helper.Data;
import com.thelogicalcoder.viazene.Interfaces.onMatchSavedListener;
import com.thelogicalcoder.viazene.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MatchMakerActivity extends Activity {
    @SuppressWarnings("deprecation")
    ProductData productData;
    Gallery selectedMatches, potentialMatches;

    List<ProductData> selectedMatchesList, potentialMatchesList;
    SelectedMatchesAdapter selectedMatchesAdapter;
    PotentialMatchesAdapter potentialMatchesAdapter;

    RadioGroup radioGroupCategories, radioGroupSubcategories;

    TextView emptyPotentialMatchText, volPrice;

    Button saveThisMatch, buyThisMatch;
    Boolean firstTime = true;

    AlertDialog saveMatchDialog;
    String matchName;
    Boolean editMode = false;

    View actionBar;
    float price = 0, vol = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("Match Maker");
        actionBar = View.inflate(this, R.layout.match_maker_actionbar, null);
        volPrice = (TextView) actionBar.findViewById(R.id.volprice);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(actionBar);


        try {
            editMode = getIntent().getBooleanExtra("editMode", false);
        } catch (Exception e) {

        }
        if (editMode) {
//            selectedMatchesList = new LinkedList<>((List<ProductData>) getIntent().getSerializableExtra("matches"));
//            emptyPotentialMatchText.setVisibility(View.GONE);
            matchName = getIntent().getStringExtra("matchName");
            potentialMatchesList = (AppController.getInstance().getProductDataList());
            potentialMatchesAdapter = new PotentialMatchesAdapter(MatchMakerActivity.this, potentialMatchesList, selectedMatchesList, editMode);
            selectedMatchesList = new ArrayList<>();
            for (int i = 0; i < ((List<ProductData>) getIntent().getSerializableExtra("matches")).size(); i++) {
                for (int j = 0; j < AppController.getInstance().getProductDataList().size(); j++) {
                    if (((List<ProductData>) getIntent().getSerializableExtra("matches")).get(i).getProductID().equalsIgnoreCase(((ProductData) (potentialMatchesAdapter.getItem(j))).getProductID())) {
                        selectedMatchesList.add((ProductData) (potentialMatchesAdapter.getItem(j)));
                    }
                }
            }
            price = 0;
            vol = 0;
            for (int i = 0; i < selectedMatchesList.size(); i++) {
                price = price + Float.parseFloat(selectedMatchesList.get(i).getProductPrice());
                vol = vol + Float.parseFloat(selectedMatchesList.get(i).getProductPoints());
            }
            //price = price - Float.parseFloat(potentialMatchesList.get(position).getProductPrice());
            //vol = vol - Float.parseFloat(potentialMatchesList.get(position).getProductPoints());
            volPrice.setText("Total Price: Rs. " + price + " /-\nTotal VP: " + vol);
        } else {
            productData = new Data().getProductFromID(getIntent().getStringExtra("productID"));
            selectedMatchesList = new ArrayList<>();
            selectedMatchesList.add(productData);
            price = price + Float.parseFloat(productData.getProductPrice());
            vol = vol + Float.parseFloat(productData.getProductPoints());
            volPrice.setText("Total Price: Rs. " + price + " /-\nTotal VP: " + vol);
        }

        selectedMatchesAdapter = new SelectedMatchesAdapter(this, selectedMatchesList, editMode);
        selectedMatches = (Gallery) findViewById(R.id.selectedMatches);
        selectedMatches.setAdapter(selectedMatchesAdapter);


        potentialMatches = (Gallery) findViewById(R.id.potentialMatches);

        radioGroupCategories = (RadioGroup) findViewById(R.id.radioGroupCategories);
        radioGroupSubcategories = (RadioGroup) findViewById(R.id.radioGroupSubcategories);

        emptyPotentialMatchText = (TextView) findViewById(R.id.emptyPotentialMatchText);

        for (int i = 0; i < AppController.getInstance().getProductCategories().size(); i++) {
            final RadioButton categoryRadioButton = new RadioButton(this);
            categoryRadioButton.setText(AppController.getInstance().getProductCategories().get(i));
            //categoryRadioButton.setTypeface(null, Typeface.BOLD);
            categoryRadioButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/arial_black.ttf"));
            categoryRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (firstTime) {
                        emptyPotentialMatchText.setText("GOOD !\nNOW, SELECT A SUBCATEGORY");
                    } else {
                        emptyPotentialMatchText.setText("SELECT A SUBCATEGORY");
                    }
                    radioGroupSubcategories.removeAllViews();
                    try {
                        potentialMatchesList = new LinkedList<ProductData>();
                        potentialMatchesAdapter = new PotentialMatchesAdapter(MatchMakerActivity.this, potentialMatchesList, selectedMatchesList, editMode);
                        potentialMatches.setAdapter(potentialMatchesAdapter);
                        emptyPotentialMatchText.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Maybe first time");
                    }
                    potentialMatches.setAdapter(potentialMatchesAdapter);
                    for (int j = 0; j < AppController.getInstance().getSubcategoriesOfCategory(categoryRadioButton.getText().toString()).size(); j++) {
                        final RadioButton subcategoryRadioButton = new RadioButton(MatchMakerActivity.this);

                        subcategoryRadioButton.setText(AppController.getInstance().getSubcategoriesOfCategory(categoryRadioButton.getText().toString()).get(j));
                        subcategoryRadioButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/arial.ttf"));
                        //subcategoryRadioButton.setTextSize(categoryRadioButton.getTextSize() - 3);
                        subcategoryRadioButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                firstTime = false;
                                emptyPotentialMatchText.setVisibility(View.GONE);
                                potentialMatchesList = new LinkedList<ProductData>(AppController.getInstance().filterProductsBySubcategory(subcategoryRadioButton.getText().toString()));
                                potentialMatchesAdapter = new PotentialMatchesAdapter(MatchMakerActivity.this, potentialMatchesList, selectedMatchesList, editMode);
                                potentialMatches.setAdapter(potentialMatchesAdapter);
                                potentialMatches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        if (!selectedMatchesList.contains(potentialMatchesAdapter.getItem(position))) {

                                            if (selectedMatchesList.size() < 4) {
                                                selectedMatchesList.add((ProductData) potentialMatchesAdapter.getItem(position));
                                                selectedMatchesAdapter.notifyDataSetChanged();
                                        /*        price = price + Float.parseFloat(potentialMatchesList.get(position).getProductPrice());
                                                vol = vol + Float.parseFloat(potentialMatchesList.get(position).getProductPoints());
                                                volPrice.setText("Total Price: Rs. " + price + " /-\nTotal VP: " + vol);*/
                                            } else {
                                                Snackbar.make(findViewById(R.id.root), "Match limit reached", Snackbar.LENGTH_LONG).show();
                                            }
                                        } else {
                                            selectedMatchesList.remove(potentialMatchesAdapter.getItem(position));
                                            selectedMatchesAdapter.notifyDataSetChanged();
                                            /*price = price - Float.parseFloat(potentialMatchesList.get(position).getProductPrice());
                                            vol = vol - Float.parseFloat(potentialMatchesList.get(position).getProductPoints());
                                            volPrice.setText("Total Price: Rs. " + price + " /-\nTotal VP: " + vol);*/
                                        }
                                        potentialMatchesList = new LinkedList<ProductData>(AppController.getInstance().filterProductsBySubcategory(subcategoryRadioButton.getText().toString()));
                                        potentialMatchesAdapter.notifyDataSetChanged();
                                        // System.out.println(selectedMatchesList.size());


                                        price = 0;
                                        vol = 0;
                                        for (int i = 0; i < selectedMatchesList.size(); i++) {
                                            price = price + Float.parseFloat(selectedMatchesList.get(i).getProductPrice());
                                            vol = vol + Float.parseFloat(selectedMatchesList.get(i).getProductPoints());
                                        }
                                        //price = price - Float.parseFloat(potentialMatchesList.get(position).getProductPrice());
                                        //vol = vol - Float.parseFloat(potentialMatchesList.get(position).getProductPoints());
                                        volPrice.setText("Total Price: Rs. " + price + " /-\nTotal VP: " + vol);

                                    }
                                });
                            }
                        });
                        radioGroupSubcategories.addView(subcategoryRadioButton);


                    }
                }
            });
            radioGroupCategories.addView(categoryRadioButton);
        }

        saveThisMatch = (Button) findViewById(R.id.saveMyMatchButton);
        buyThisMatch = (Button) findViewById(R.id.buyThisMatch);
        if (editMode) {
            saveThisMatch.setText("UPDATE THIS MATCH");
        }
        saveThisMatch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (editMode) {
                    if (selectedMatchesList.size() != 0) {
                        new SaveMatch(MatchMakerActivity.this, matchName, selectedMatchesList, editMode, new onMatchSavedListener() {
                            @Override
                            public void onMatchSaved(String response) {

                                if (response.trim().equalsIgnoreCase("success")) {
                                    Snackbar.make(findViewById(R.id.root), "Match updated", Snackbar.LENGTH_SHORT).show();
                                } else
                                    Snackbar.make(findViewById(R.id.root), "Could not update match", Snackbar.LENGTH_SHORT).show();
                            }
                        }).execute();
                    } else {
                        Snackbar.make(findViewById(R.id.root), "Select at least 1 product", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    if (AppController.getInstance().isLoggedIn()) {
                        if (selectedMatchesList.size() != 1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MatchMakerActivity.this);
                            builder.setTitle("Save Match");
                            final View view = View.inflate(MatchMakerActivity.this, R.layout.save_match_dialog, null);
                            builder.setView(view);

                            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EditText matchName = (EditText) view.findViewById(R.id.matchName);
                                    if (!matchName.getText().toString().isEmpty()) {
                                        new SaveMatch(MatchMakerActivity.this, matchName.getText().toString().trim(), selectedMatchesList, editMode, new onMatchSavedListener() {
                                            @Override
                                            public void onMatchSaved(String response) {
                                                if (response.trim().equalsIgnoreCase("fail")) {
                                                    Snackbar.make(findViewById(R.id.root), "Match name already exists", Snackbar.LENGTH_SHORT).show();
                                                }
                                                if (response.trim().equalsIgnoreCase("success")) {
                                                    Snackbar.make(findViewById(R.id.root), "Match saved", Snackbar.LENGTH_SHORT).show();
                                                }
                                            }
                                        }).execute();
                                    } else {
                                        matchName.setError("Enter Match Name");
                                    }
                                }
                            });
                            builder.setNegativeButton("Cancel", null);
                            saveMatchDialog = builder.create();
                            saveMatchDialog.show();
                        } else {
                            Snackbar.make(findViewById(R.id.root), "Select at least 1 product", Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(findViewById(R.id.root), "Log in first", Snackbar.LENGTH_SHORT).setAction("LOGIN", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(MatchMakerActivity.this, LoginRegisterActivity.class).putExtra("source", "mma"));
                                //finish();
                            }
                        }).show();
                    }
                }
            }
        });

        buyThisMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppController.getInstance().isLoggedIn()) {
                    if (selectedMatchesList.size() != 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MatchMakerActivity.this);
                        builder.setTitle("Save Match");
                        final View view = View.inflate(MatchMakerActivity.this, R.layout.save_match_dialog, null);
                        builder.setView(view);

                        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText matchName = (EditText) view.findViewById(R.id.matchName);
                                if (!matchName.getText().toString().isEmpty()) {
                                    new SaveMatch(MatchMakerActivity.this, matchName.getText().toString().trim(), selectedMatchesList, editMode, new onMatchSavedListener() {
                                        @Override
                                        public void onMatchSaved(String response) {
                                            if (response.trim().equalsIgnoreCase("fail")) {
                                                Snackbar.make(findViewById(R.id.root), "Match name already exists", Snackbar.LENGTH_SHORT).show();
                                            }
                                            if (response.trim().equalsIgnoreCase("success")) {
                                                Snackbar.make(findViewById(R.id.root), "Match saved", Snackbar.LENGTH_SHORT).show();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        startActivity(new Intent(MatchMakerActivity.this, BuyProductCOD.class).putExtra("productIDs", (Serializable) selectedMatchesList).putExtra("mode", "multiple"));
                                                    }
                                                }, 700);
                                            }
                                            if (response.trim().equalsIgnoreCase("errorOccurred")) {
                                                Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).execute();
                                } else {
                                    matchName.setError("Enter Match Name");
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", null);
                        builder.setCancelable(false);
                        saveMatchDialog = builder.create();
                        saveMatchDialog.show();
                    } else {
                        Snackbar.make(findViewById(R.id.root), "Select at least 1 product", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(findViewById(R.id.root), "Log in first", Snackbar.LENGTH_SHORT).setAction("LOGIN", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(MatchMakerActivity.this, LoginRegisterActivity.class).putExtra("source", "mma"));
                            //finish();
                        }
                    }).show();
                }
            }
        });
    }

    public void updateActionBarPriceVolume() {
        price = 0;
        vol = 0;
        for (int i = 0; i < selectedMatchesList.size(); i++) {
            price = price + Float.parseFloat(selectedMatchesList.get(i).getProductPrice());
            vol = vol + Float.parseFloat(selectedMatchesList.get(i).getProductPoints());
        }
        //price = price - Float.parseFloat(potentialMatchesList.get(position).getProductPrice());
        //vol = vol - Float.parseFloat(potentialMatchesList.get(position).getProductPoints());
        volPrice.setText("Total Price: Rs. " + price + " /-\nTotal VP: " + vol);
    }

    public void notifyDataSetChangedPotentialMatches() {
        potentialMatchesAdapter.notifyDataSetChanged();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_match_maker;
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

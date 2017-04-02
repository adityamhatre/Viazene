package com.thelogicalcoder.viazene.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.blunderer.materialdesignlibrary.activities.Activity;
import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;
import com.daimajia.slider.library.SliderLayout;
import com.thelogicalcoder.viazene.Adapters.ProductsAdapter;
import com.thelogicalcoder.viazene.Adapters.ServicesAdapter;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Data.ProductData;
import com.thelogicalcoder.viazene.Data.ServiceData;
import com.thelogicalcoder.viazene.Helper.Data;
import com.thelogicalcoder.viazene.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import babushkatext.BabushkaText;

public class ProductListView extends Activity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    Button editMatchOrIDK, buy;
    LinearLayout bottomBar, upperBar;
    SliderLayout sliderShow;
    String productID, productName,productNameLowerCase;
    //ProductData productData;
    List<ProductData> categoryProductDataList;
    List<ProductData> subCategoryProductDataList;
    List<ProductData> searchProductDataList;
    List<ServiceData> searchServiceDataList;
    List<ProductData> matchesProductDataList;
    List<ProductData> currentList;
    List<ProductData> filteredList;

    List<String> productIDs;
    ProductData productData;
    ServiceData serviceData;
    Boolean productFound = false;

    SwipeRefreshLayout swipeRefreshLayout;
    ProductsAdapter productsAdapter;
    ServicesAdapter servicesAdapter;

    ListView productListView;
    CheckBox priceFilter, colorFilter;
    float min = -1, max = -1;
    Boolean minSet = false, maxSet = false;
    List<String> selectedColors;
    BabushkaText emptyListText;

    @Override
    protected ActionBarHandler getActionBarHandler() {
        return null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        productListView = (ListView) findViewById(R.id.list);
        emptyListText = (BabushkaText) findViewById(R.id.emptyListText);
        categoryProductDataList = new ArrayList<>();
        subCategoryProductDataList = new ArrayList<>();
        matchesProductDataList = new ArrayList<>();
        searchProductDataList = new ArrayList<>();
        searchServiceDataList=new ArrayList<>();
        filteredList = new ArrayList<>();
        selectedColors = new ArrayList<>();
        productIDs = new ArrayList<>();
        productData = new ProductData();
        getSupportActionBar().setTitle("Search Results");
        try {
            productData = new Data().getProductFromID(getIntent().getStringExtra("productID"));
            if (productData != null) {
                productFound = true;
                startActivity(new Intent(ProductListView.this, SingleProductView.class).putExtra("productID", productData.getProductID()));
                finish();
                productListView.setAdapter(null);
                //return new ProductsAdapter(this, productData);
            } else {
                productFound = false;
                productListView.setAdapter(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            productName = getIntent().getStringExtra("productName").trim();
            productNameLowerCase=productName.toLowerCase();
            System.out.println("Search query: " + productName);
            for (int i = 0; i < AppController.getInstance().getProductDataList().size(); i++) {
                System.out.println("Matching " + productName + " with " + AppController.getInstance().getProductDataList().get(i).getProductName().trim());
                if (AppController.getInstance().getProductDataList().get(i).getProductName().trim().toLowerCase().contains(productNameLowerCase)) {
//                    productData = AppController.getInstance().getProductDataList().get(i);
//                    productFound = true;
//                    startActivity(new Intent(ProductListView.this, SingleProductView.class).putExtra("productID", productData.getProductID()));
//                    finish();
//                    //return new ProductsAdapter(this, productData);
                    //                   productName = productName.substring(productName.indexOf("Category: ") + 10);

                    searchProductDataList.add(AppController.getInstance().getProductDataList().get(i));
                    productFound = true;
                    productsAdapter = new ProductsAdapter(this, searchProductDataList);
                    productListView.setAdapter(productsAdapter);
                    currentList = searchProductDataList;
                } else if (productName.contains("Category: ")) {
                    productName = productName.substring(productName.indexOf("Category: ") + 10);
                    for (int j = 0; j < AppController.getInstance().getProductDataList().size(); j++) {
                        if (AppController.getInstance().getProductDataList().get(j).getProductCategory().trim().equals(productName.trim())) {
                            categoryProductDataList.add(AppController.getInstance().getProductDataList().get(j));
                            productFound = true;
                        }
                    }
                    productsAdapter = new ProductsAdapter(this, categoryProductDataList);
                    productListView.setAdapter(productsAdapter);
                    currentList = categoryProductDataList;
                } else if (productName.contains("Subcategory: ")) {
                    productName = productName.substring(productName.indexOf("Subcategory: ") + 13);
                    for (int j = 0; j < AppController.getInstance().getProductDataList().size(); j++) {
                        if (AppController.getInstance().getProductDataList().get(j).getProductSubCategory().trim().equals(productName.trim())) {
                            subCategoryProductDataList.add(AppController.getInstance().getProductDataList().get(j));
                            productFound = true;
                        }
                    }
                    productsAdapter = new ProductsAdapter(this, subCategoryProductDataList);
                    productListView.setAdapter(productsAdapter);
                    currentList = subCategoryProductDataList;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            productIDs = getIntent().getStringArrayListExtra("productIDs");
            for (int i = 0; i < productIDs.size(); i++) {
                for (int j = 0; j < AppController.getInstance().getProductDataList().size(); j++) {
                    if (productIDs.get(i).equalsIgnoreCase(AppController.getInstance().getProductDataList().get(j).getProductID())) {
                        matchesProductDataList.add(AppController.getInstance().getProductDataList().get(j));
                        productFound = true;
                    }
                }
            }
            productsAdapter = new ProductsAdapter(this, matchesProductDataList);
            productListView.setAdapter(productsAdapter);
            currentList = matchesProductDataList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startActivity(new Intent(ProductListView.this, SingleProductView.class).putExtra("productID", ((ProductData) (adapterView.getItemAtPosition(position))).getProductID()));
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        bottomBar = (LinearLayout) findViewById(R.id.bottomBar);
        upperBar = (LinearLayout) findViewById(R.id.upperBar);
        editMatchOrIDK = (Button) findViewById(R.id.editOrIDK);
        buy = (Button) findViewById(R.id.buy);

        swipeRefreshLayout.setColorSchemeResources(R.color.color_primary);
        swipeRefreshLayout.setOnRefreshListener(this);
        try {
            if (getIntent().getStringExtra("mode").trim().equalsIgnoreCase("matches")) {
                getSupportActionBar().setTitle("Match: " + getIntent().getStringExtra("matchName"));
                bottomBar.setVisibility(View.VISIBLE);
                buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ProductListView.this, BuyProductCOD.class).putExtra("productIDs", (Serializable) matchesProductDataList).putExtra("mode", "multiple"));
                    }
                });
                editMatchOrIDK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ProductListView.this, MatchMakerActivity.class).putExtra("editMode", true).putExtra("matches", (Serializable) matchesProductDataList).putExtra("matchName", getIntent().getStringExtra("matchName")));
                        finish();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (getIntent().getStringExtra("mode").trim().equalsIgnoreCase("showSubcategoryBar")) {
                upperBar.setVisibility(View.VISIBLE);
                upperBar.removeAllViews();
                for (int i = 0; i < AppController.getInstance().getSubcategoriesOfCategory(getIntent().getStringExtra("category").trim()).size(); i++) {
                    View view = View.inflate(ProductListView.this, R.layout.sub_category_button_for_bar, null);
                    final Button subCategoryButton = (Button) view.findViewById(R.id.subCategoryButton);
                    final int position = i;
                    subCategoryButton.setText(AppController.getInstance().getSubcategoriesOfCategory(getIntent().getStringExtra("category").trim()).get(i));
                    subCategoryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            List<ProductData> filteredBySubcategory = AppController.getInstance().filterProductsBySubcategory(subCategoryButton.getText().toString().trim());
                            productsAdapter = new ProductsAdapter(ProductListView.this, filteredBySubcategory);
                            productListView.setAdapter(productsAdapter);
                            currentList = filteredBySubcategory;
                        }
                    });
                    upperBar.addView(view);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (getIntent().getStringExtra("mode").trim().equalsIgnoreCase("new10")) {
                getSupportActionBar().setTitle("New Arrivals");
                bottomBar.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

        }catch (Exception e){
            e.printStackTrace();
        }


        if (productFound) {
            emptyListText.setVisibility(View.GONE);
        } else {
            emptyListText.addPiece(new BabushkaText.Piece.Builder("Sorry, No results found for ").build());
            emptyListText.addPiece(new BabushkaText.Piece.Builder("\"" + productName + "\"").style(Typeface.BOLD).build());
            emptyListText.display();
            emptyListText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_product_view;
    }


    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onClick(View view) {

    }


    CompoundButton.OnCheckedChangeListener priceCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                final EditText minText, maxText;
                View view1 = View.inflate(ProductListView.this, R.layout.price_filter, null);
                minText = (EditText) view1.findViewById(R.id.min);
                maxText = (EditText) view1.findViewById(R.id.max);
                final AlertDialog alertDialog;
                alertDialog = new AlertDialog.Builder(ProductListView.this).setView(view1).setPositiveButton("OK", null).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (min == -1 || max == -1) {
                            priceFilter.setChecked(false);
                            priceFilter.setText("Price Filter");
                        }
                    }
                }).create();
                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!minText.getText().toString().isEmpty()) {
                            minSet = true;
                        } else {
                            minText.setError("Enter a value");
                            minSet = false;
                        }
                        if (!maxText.getText().toString().isEmpty()) {
                            maxSet = true;
                        } else {
                            maxText.setError("Enter a value");
                            maxSet = false;
                        }
                        if (minSet && maxSet) {
                            min = Float.parseFloat(minText.getText().toString().trim());
                            max = Float.parseFloat(maxText.getText().toString().trim());
                            if (min < max) {
                                priceFilter.setText("Price Filter (Rs." + min + " - Rs." + max + ")");
                                alertDialog.cancel();
                            } else {
                                min = -1;
                                max = -1;
                                minText.setError("Value should be less than max");
                                maxText.setError("Value should be more than min");
                            }
                        }
                    }
                });
            } else {
                min = -1;
                max = -1;
                priceFilter.setText("Price FIlter");
            }
        }
    };

    CompoundButton.OnCheckedChangeListener colorCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                final ListView listView = new ListView(ProductListView.this);
                listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
                //listView.setAdapter(new MaterialArrayAdapter(getActivity(), R.layout.material_simple_list_item_1, AppController.getInstance().getColorList(), "colorList"));

                final List<String> selectedColors = new ArrayList<>();
                listView.setAdapter(new ArrayAdapter<>(ProductListView.this, android.R.layout.simple_list_item_multiple_choice, AppController.getInstance().getColorList()));

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (!selectedColors.contains(AppController.getInstance().getColorList().get(position))) {
                            selectedColors.add(AppController.getInstance().getColorList().get(position));
                        } else {
                            selectedColors.remove(AppController.getInstance().getColorList().get(position));
                        }
                    }
                });

                final AlertDialog alertDialog;
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductListView.this);
                builder1.setTitle("Select colors");
                builder1.setView(listView);
                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (!selectedColors.isEmpty()) {
                            colorFilter.setOnCheckedChangeListener(null);
                            colorFilter.setChecked(true);
                            colorFilter.setOnCheckedChangeListener(colorCheckedChangeListener);
                            String showThis = "";
                            int count = 0;
                            for (int i = 0; i < selectedColors.size(); i++) {
                                if (count < 3) {
                                    showThis = showThis + selectedColors.get(i) + ", ";
                                    count++;
                                } else break;
                            }
                            showThis = showThis.substring(0, showThis.length() - 2).trim();
                            colorFilter.setText("Color Filter (" + showThis + ")");
                        } else {
                            colorFilter.setText("Color Filter");
                            colorFilter.setChecked(false);
                        }
                    }
                });
                alertDialog = builder1.create();
                alertDialog.show();
                /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectedColor = AppController.getInstance().getColorList().get(position);
                        alertDialog.dismiss();
                    }
                });*/
            } else {
                selectedColors.removeAll(selectedColors);
                colorFilter.setText("Color Filter");
            }
        }
    };

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            if (!getIntent().getStringExtra("mode").equalsIgnoreCase("matches")) {
                menu.add(Menu.NONE, 0, Menu.FIRST, "Filter").setIcon(R.drawable.filter).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(ProductListView.this);
                        builder.setTitle("Filter Products");
                        View view = View.inflate(ProductListView.this, R.layout.filter_products_dialog, null);

                        priceFilter = (CheckBox) view.findViewById(R.id.priceFilter);
                        priceFilter.setOnCheckedChangeListener(priceCheckedChangeListener);
                        colorFilter = (CheckBox) view.findViewById(R.id.colorFilter);
                        colorFilter.setOnCheckedChangeListener(colorCheckedChangeListener);

                        if (min != -1 || max != -1) {
                            priceFilter.setOnCheckedChangeListener(null);
                            priceFilter.setChecked(true);
                            priceFilter.setOnCheckedChangeListener(priceCheckedChangeListener);
                            priceFilter.setText("Price Filter (Rs." + min + " - Rs." + max + ")");
                        }
                        if (!selectedColors.isEmpty()) {
                            colorFilter.setOnCheckedChangeListener(null);
                            colorFilter.setChecked(true);
                            colorFilter.setOnCheckedChangeListener(colorCheckedChangeListener);
                            colorFilter.setText("Color Filter (" + selectedColors + ")");
                        }

                        builder.setView(view);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (priceFilter.isChecked() && colorFilter.isChecked()) {
                                    filteredList = new LinkedList<ProductData>(AppController.getInstance().filterProductsByColors(currentList, selectedColors));
                                    currentList = filteredList;
                                    filteredList = new LinkedList<ProductData>(AppController.getInstance().filterProductsByColors(currentList, selectedColors));
                                    productsAdapter = new ProductsAdapter(ProductListView.this, filteredList);
                                    productListView.setAdapter(productsAdapter);
                                    currentList = filteredList;
                                } else if (priceFilter.isChecked()) {
                                    filteredList = new LinkedList<ProductData>(AppController.getInstance().filterProductsByPrice(currentList, min, max));
                                    productsAdapter = new ProductsAdapter(ProductListView.this, filteredList);
                                    productListView.setAdapter(productsAdapter);
                                    currentList = filteredList;
                                } else if (colorFilter.isChecked()) {
                                    filteredList = new LinkedList<ProductData>(AppController.getInstance().filterProductsByColors(currentList, selectedColors));
                                    productsAdapter = new ProductsAdapter(ProductListView.this, filteredList);
                                    productListView.setAdapter(productsAdapter);
                                    currentList = filteredList;
                                } else {
                                    productsAdapter = new ProductsAdapter(ProductListView.this, AppController.getInstance().getProductDataList());
                                    productListView.setAdapter(productsAdapter);
                                    currentList = AppController.getInstance().getProductDataList();
                                }
                            }
                        });
                        builder.setNegativeButton("REMOVE ALL FILTERS", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                productsAdapter = new ProductsAdapter(ProductListView.this, AppController.getInstance().getProductDataList());
                                productListView.setAdapter(productsAdapter);
                                currentList = AppController.getInstance().getProductDataList();
                                min = max = -1;
                                selectedColors.removeAll(selectedColors);
                                priceFilter.setOnCheckedChangeListener(null);
                                colorFilter.setOnCheckedChangeListener(null);
                                priceFilter.setChecked(false);
                                colorFilter.setChecked(false);
                                priceFilter.setOnCheckedChangeListener(priceCheckedChangeListener);
                                colorFilter.setOnCheckedChangeListener(colorCheckedChangeListener);
                            }
                        });
                        builder.create().show();
                        return false;
                    }
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                return super.onCreateOptionsMenu(menu);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }*/

    @Override
    protected boolean enableActionBarShadow() {
        return false;
    }

}
package com.thelogicalcoder.viazene.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import com.thelogicalcoder.viazene.Activities.SingleServiceView;
import com.thelogicalcoder.viazene.Adapters.ServicesAdapter;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Data.ServiceData;
import com.thelogicalcoder.viazene.Helper.ScrollTabHolderFragment;
import com.thelogicalcoder.viazene.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aditya on 030, 30 July 2015.
 */
public class ViewAllServicesFragment extends ScrollTabHolderFragment implements AbsListView.OnScrollListener {

    private static final String ARG_POSITION = "position";

    private ListView mListView;

    private int mPosition;
    List<ServiceData> categoryServiceDataList;
    List<ServiceData> subCategoryServiceDataList;
    List<ServiceData> matchesServiceDataList;
    List<ServiceData> currentList, loggedInList;
    List<ServiceData> filteredList;
    String serviceCateogry;

    List<String> productIDs;
    ServiceData ServiceData;
    Boolean productFound = false;
    ServicesAdapter servicesAdapter;
    private final List<String> TITLES = AppController.getInstance().getServiceCategories();

    CheckBox priceFilter, colorFilter;
    float min = -1, max = -1;
    Boolean minSet = false, maxSet = false;
    List<String> selectedColors;

    public static Fragment newInstance(int position) {
        ViewAllServicesFragment f = new ViewAllServicesFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(ARG_POSITION);


        categoryServiceDataList = new ArrayList<>();
        subCategoryServiceDataList = new ArrayList<>();
        matchesServiceDataList = new ArrayList<>();
        filteredList = new ArrayList<>();
        productIDs = new ArrayList<>();
        selectedColors = new ArrayList<>();
        ServiceData = new ServiceData();

        try {
            serviceCateogry = TITLES.get(mPosition);

            categoryServiceDataList = AppController.getInstance().filterServicesByCategory((serviceCateogry));
            currentList = categoryServiceDataList;
            if (AppController.getInstance().isLoggedIn()) {
                loggedInList = new ArrayList<>();
                for (int i = 0; i < currentList.size(); i++) {
                    if (currentList.get(i).getPinCodeAvailableList().contains(AppController.getInstance().getUserInfo().getPincode().trim()) || currentList.get(i).getPinCodeAvailable().equalsIgnoreCase("all")) {
                        loggedInList.add(currentList.get(i));
                    }
                }
                servicesAdapter = new ServicesAdapter(getActivity(), loggedInList);
            } else
                servicesAdapter = new ServicesAdapter(getActivity(), currentList);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        mListView = (ListView) v.findViewById(R.id.listView);

        View placeHolderView = inflater.inflate(R.layout.view_header_placeholder, mListView, false);
        mListView.addHeaderView(placeHolderView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                try {
                    startActivity(new Intent(getActivity(), SingleServiceView.class).putExtra("serviceID", ((ServiceData) (adapterView.getItemAtPosition(position))).getServiceID()));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("error because of observableScrollVIew");
                }
            }
        });


        return v;
    }


    /*public View getSortFilterView(final String productCategory) {
        Button sortButton, filterButton;
        View returnView = View.inflate(getActivity(), R.layout.sort_filter, null);
        sortButton = (Button) returnView.findViewById(R.id.sortButton);
        filterButton = (Button) returnView.findViewById(R.id.filterButton);

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(getActivity(), R.layout.sort_dialog, null);
                Button lowToHigh, highToLow, discount;
                lowToHigh = (Button) view.findViewById(R.id.lowToHighButton);
                highToLow = (Button) view.findViewById(R.id.highToLowButton);
                discount = (Button) view.findViewById(R.id.sort_discount);

                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setView(view).create();
                alertDialog.show();
                lowToHigh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentList = new LinkedList<>(currentList);
                        Collections.sort(currentList, new PriceSort());
                        servicesAdapter = new ServicesAdapter(getActivity(), currentList);
                        mListView.setAdapter(servicesAdapter);
                        alertDialog.dismiss();
                    }
                });
                highToLow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentList = new LinkedList<>(currentList);
                        Collections.sort(currentList, new PriceSort());
                        Collections.reverse(currentList);
                        servicesAdapter = new ServicesAdapter(getActivity(), currentList);
                        mListView.setAdapter(servicesAdapter);
                        alertDialog.dismiss();
                    }
                });

                discount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentList = new LinkedList<>(currentList);
                        Collections.sort(currentList, new DiscountSort());
                        Collections.reverse(currentList);
                        servicesAdapter = new ServicesAdapter(getActivity(), currentList);
                        mListView.setAdapter(servicesAdapter);
                        alertDialog.dismiss();
                    }
                });

            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Filter Products");
                    View view = View.inflate(getActivity(), R.layout.filter_products_dialog, null);

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
                    }

                    builder.setView(view);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (priceFilter.isChecked() && colorFilter.isChecked()) {
                                filteredList = new LinkedList<ServiceData>(AppController.getInstance().filterProductsByColors(currentList, selectedColors));
                                servicesAdapter = new ServicesAdapter(getActivity(), filteredList);
                                mListView.setAdapter(servicesAdapter);
                                currentList = filteredList;
                            } else if (priceFilter.isChecked()) {
                                filteredList = new LinkedList<ServiceData>(AppController.getInstance().filterProductsByPrice(currentList, min, max));
                                servicesAdapter = new ServicesAdapter(getActivity(), filteredList);
                                mListView.setAdapter(servicesAdapter);
                                currentList = filteredList;
                            } else if (colorFilter.isChecked()) {
                                filteredList = new LinkedList<ServiceData>(AppController.getInstance().filterProductsByColors(currentList, selectedColors));
                                servicesAdapter = new ServicesAdapter(getActivity(), filteredList);
                                mListView.setAdapter(servicesAdapter);
                                currentList = filteredList;
                            } else {
                                servicesAdapter = new ServicesAdapter(getActivity(), AppController.getInstance().getServiceDataList());
                                mListView.setAdapter(servicesAdapter);
                                currentList = AppController.getInstance().getServiceDataList();
                            }
                        }
                    });
                    builder.setNegativeButton("REMOVE ALL FILTERS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            categoryServiceDataList = AppController.getInstance().filterProductsByCategory(productCategory);
                            currentList = categoryServiceDataList;
                            servicesAdapter = new ServicesAdapter(getActivity(), currentList);
                            mListView.setAdapter(servicesAdapter);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return returnView;

    }*/

    /*public View getSubcategoryView(String productCategory) {
        View returnView = View.inflate(getActivity(), R.layout.sub_category_button_bar, null);
        LinearLayout upperBar = (LinearLayout) returnView.findViewById(R.id.upperBar);
        upperBar.removeAllViews();
        for (int i = 0; i < AppController.getInstance().getSubcategoriesOfCategory(productCategory.trim()).size(); i++) {
            View view = View.inflate(getActivity(), R.layout.sub_category_button_for_bar, null);
            final Button subCategoryButton = (Button) view.findViewById(R.id.subCategoryButton);
            final int position = i;
            subCategoryButton.setText(AppController.getInstance().getSubcategoriesOfCategory(productCategory.trim()).get(i));
            subCategoryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<ServiceData> filteredBySubcategory = AppController.getInstance().filterProductsBySubcategory(subCategoryButton.getText().toString().trim());
                    servicesAdapter = new ServicesAdapter(getActivity(), filteredBySubcategory);
                    mListView.setAdapter(servicesAdapter);
                    currentList = filteredBySubcategory;
                    servicesAdapter.notifyDataSetChanged();
                }
            });
            upperBar.addView(view);
        }
        return returnView;
    }*/

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListView.setOnScrollListener(this);
        mListView.setAdapter(servicesAdapter);
    }

    @Override
    public void adjustScroll(int scrollHeight) {
        if (scrollHeight == 0 && mListView.getFirstVisiblePosition() >= 1) {
            return;
        }

        mListView.setSelectionFromTop(1, scrollHeight);

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mScrollTabHolder != null)
            mScrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, mPosition);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    CompoundButton.OnCheckedChangeListener priceCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                final EditText minText, maxText;
                View view1 = View.inflate(getActivity(), R.layout.price_filter, null);
                minText = (EditText) view1.findViewById(R.id.min);
                maxText = (EditText) view1.findViewById(R.id.max);
                final AlertDialog alertDialog;
                alertDialog = new AlertDialog.Builder(getActivity()).setView(view1).setPositiveButton("OK", null).setOnCancelListener(new DialogInterface.OnCancelListener() {
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
                final ListView listView = new ListView(getActivity());
                listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

                listView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, AppController.getInstance().getColorList()));

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
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
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
                                } else {
                                    showThis = showThis.substring(0, showThis.length() - 2).trim();
                                    showThis = showThis + ", .....";
                                    break;
                                }
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

            } else {
                selectedColors.removeAll(selectedColors);
                colorFilter.setText("Color Filter");
            }
        }
    };
}
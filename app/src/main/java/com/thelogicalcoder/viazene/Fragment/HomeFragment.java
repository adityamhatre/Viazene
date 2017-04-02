package com.thelogicalcoder.viazene.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.thelogicalcoder.viazene.Activities.ProductListView;
import com.thelogicalcoder.viazene.Activities.SingleProductView;
import com.thelogicalcoder.viazene.Activities.SingleServiceView;
import com.thelogicalcoder.viazene.Activities.ViewAllProducts;
import com.thelogicalcoder.viazene.Adapters.SelectedMatchesAdapter;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Data.ProductData;
import com.thelogicalcoder.viazene.Helper.BoughtCountSort;
import com.thelogicalcoder.viazene.Helper.TimeStampSort;
import com.thelogicalcoder.viazene.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

@SuppressWarnings("deprecation")
public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    // SwipeRefreshLayout swipeRefreshLayout;
    //ProductsAdapter productsAdapter;
    // SliderLayout sliderShow;
    LinearLayout categoryList;
    View view;
    Button viewAllProducts;
    SliderLayout sliderShow;
    Gallery trendingNowList, newArrivalsList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
        // return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
//        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
//        swipeRefreshLayout.setColorSchemeResources(R.color.color_primary);
//        swipeRefreshLayout.setOnRefreshListener(this);
        //   sliderShow = (SliderLayout) view.findViewById(R.id.slider);
        this.view = view;
        //setupCategories();


//        for (int i = 0; i < AppController.getInstance().getProductDataList().size(); i++) {
//            final DefaultSliderView imageSlide = new DefaultSliderView(getActivity());
//            imageSlide.image(AppController.getInstance().getProductDataList().get(i).getProductURL().trim().replace(" ", "%20") + "/image" + (i + 1) + ".jpg").setScaleType(BaseSliderView.ScaleType.CenterInside);
//            sliderShow.addSlider(imageSlide);
//        }
//        sliderShow.setDuration(500);

        super.onViewCreated(view, savedInstanceState);
    }

//    @Override
//    public ListAdapter getListAdapter() {
//        List<ProductData> productDataList = AppController.getInstance().getProductDataList();
//        productsAdapter = new ProductsAdapter(getActivity(), productDataList);
//        return productsAdapter;
//    }
//
//    @Override
//    public boolean useCustomContentView() {
//        return true;
//    }
//
//    @Override
//    public int getCustomContentView() {
//        return R.layout.fragment_home;
//    }
//
//    @Override
//    public boolean pullToRefreshEnabled() {
//        return false;
//    }
//
//    @Override
//    public int[] getPullToRefreshColorResources() {
//        return new int[0];
//    }
//
//    @Override
//    public void onRefresh() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        }, 2000);
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//        startActivity(new Intent(getActivity(), ProductListView.class).putExtra("productID", AppController.getInstance().getProductDataList().get(position).getProductID()));
//    }
//
//    @Override
//    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
//        return false;
//    }

    void setupCategories() {
        categoryList = (LinearLayout) view.findViewById(R.id.categoryList);
        viewAllProducts = (Button) view.findViewById(R.id.viewAllProducts);
        sliderShow = (SliderLayout) view.findViewById(R.id.slider);
        trendingNowList = (Gallery) view.findViewById(R.id.trendingNowList);
        newArrivalsList = (Gallery) view.findViewById(R.id.newArrivalsList);
        sliderShow.addSlider(new DefaultSliderView(getActivity()).image(R.drawable.deal_of_the_day).setScaleType(BaseSliderView.ScaleType.CenterInside).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView baseSliderView) {
                int random = (int) (Math.random() * AppController.getInstance().getProductDataList().size() - 1);
                startActivity(new Intent(getActivity(), SingleProductView.class).putExtra("productID", AppController.getInstance().getProductDataList().get(random).getProductID()));
            }
        }));
        sliderShow.addSlider(new DefaultSliderView(getActivity()).image(R.drawable.new_arrivals).setScaleType(BaseSliderView.ScaleType.CenterInside).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView baseSliderView) {
                LinkedList<ProductData> new10Products = new LinkedList<ProductData>(AppController.getInstance().getProductDataList());
                Collections.sort(new10Products, new TimeStampSort());
                Collections.reverse(new10Products);
                ArrayList<String> new10ProductIDs = new ArrayList<String>();
                for (int i = 0; i < 10; i++) {
                    new10ProductIDs.add(new10Products.get(i).getProductID());
                }
                startActivity(new Intent(getActivity(), ProductListView.class).putExtra("productIDs", new10ProductIDs).putExtra("mode", "new10"));
            }
        }));
        sliderShow.addSlider(new DefaultSliderView(getActivity()).image(R.drawable.services).setScaleType(BaseSliderView.ScaleType.CenterInside).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView baseSliderView) {
                int random = (int) (Math.random() * AppController.getInstance().getServices().getServices().size() - 1);
                startActivity(new Intent(getActivity(), SingleServiceView.class).putExtra("serviceID", AppController.getInstance().getServices().getServices().get(random).getServiceID()));
            }
        }));
        sliderShow.addSlider(new DefaultSliderView(getActivity()).image(R.drawable.match_maker).setScaleType(BaseSliderView.ScaleType.CenterInside).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView baseSliderView) {
                Snackbar.make(view.findViewById(R.id.root), "Match Maker Video", Snackbar.LENGTH_LONG).show();
            }
        }));


        sliderShow.setDuration(4000);
        viewAllProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ViewAllProducts.class));
            }
        });
        categoryList.removeAllViews();
        for (int i = 0; i < AppController.getInstance().getProductCategories().size(); i++) {
            final int position = i;
            View categoryRow = View.inflate(getActivity(), R.layout.category_row, null);
            TextView category, subcategories;
            ImageView categoryIcon;
            category = (TextView) categoryRow.findViewById(R.id.category);
            subcategories = (TextView) categoryRow.findViewById(R.id.subCategories);
            categoryIcon = (ImageView) categoryRow.findViewById(R.id.categoryIcon);

            category.setText(AppController.getInstance().getProductCategories().get(i));
            subcategories.setText(AppController.getInstance().getSubcategoriesOfCategory(AppController.getInstance().getProductCategories().get(i)).toString().replace("[", "").replace("]", ""));
            categoryIcon.setImageResource(getCategoryIcon(position));
            categoryList.addView(categoryRow);
        }
        for (int i = 0; i < categoryList.getChildCount(); i++) {
            final int position = i;
            categoryList.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //startActivity(new Intent(getActivity(), ProductListViewFilter.class));

                    //startActivity(new Intent(getActivity(), ProductListView.class).putExtra("productName", "Category: " + AppController.getInstance().getProductCategories().get(position)).putExtra("mode", "showSubcategoryBar").putExtra("category", AppController.getInstance().getProductCategories().get(position)));
                    startActivity(new Intent(getActivity(), ViewAllProducts.class).putExtra("position", position));

                }
            });
        }


        LinkedList<ProductData> top10Products = new LinkedList<ProductData>(AppController.getInstance().getProductDataList());
        Collections.sort(top10Products, new BoughtCountSort());
        Collections.reverse(top10Products);

        top10Products = new LinkedList<>(top10Products.subList(0, 9));

        trendingNowList.setAdapter(new SelectedMatchesAdapter(getActivity(), top10Products, false, HomeFragment.this));
        final LinkedList<ProductData> finalTop10Products = top10Products;
        trendingNowList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), SingleProductView.class).putExtra("productID", finalTop10Products.get(position).getProductID()));
            }
        });

        LinkedList<ProductData> new10Products = new LinkedList<ProductData>(AppController.getInstance().getProductDataList());
        Collections.sort(new10Products, new TimeStampSort());
        Collections.reverse(new10Products);

        new10Products = new LinkedList<>(new10Products.subList(0, 9));

        newArrivalsList.setAdapter(new SelectedMatchesAdapter(getActivity(), new10Products, false, HomeFragment.this));
        final LinkedList<ProductData> finalNew10Products = new10Products;
        newArrivalsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), SingleProductView.class).putExtra("productID", finalNew10Products.get(position).getProductID()));
            }
        });

    }

    private int getCategoryIcon(int position) {
        switch (position) {
            case 0:
                return R.drawable.clothing;
            case 1:
                return R.drawable.bags;
            case 2:
                return R.drawable.jewellery;
            case 3:
                return R.drawable.footwear;
            default:
                return R.drawable.logo;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        Collections.shuffle(AppController.getInstance().getProductDataList());
//        productsAdapter.notifyDataSetChanged();
        setupCategories();
    }

    @Override
    public void onRefresh() {

    }
}
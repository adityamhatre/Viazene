package com.thelogicalcoder.viazene.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aditya on 011, 11 July 2015.
 */
public class HomeCategoriesAdapter extends BaseAdapter {

    private List<String> categoryList;
    private List<String> subCategoryList;
    private Context context;
    private LayoutInflater inflater;


    static class ViewHolder {
        TextView category, subcategories;
        ImageView categoryIcon;
    }


    public HomeCategoriesAdapter(Context context, List<String> categoryList) {
        this.categoryList = categoryList;
        subCategoryList = new ArrayList<>();
        this.context = context;
    }


    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.category_row, null);
            viewHolder = new ViewHolder();
            viewHolder.category = (TextView) convertView.findViewById(R.id.category);
            viewHolder.subcategories = (TextView) convertView.findViewById(R.id.subCategories);
            viewHolder.categoryIcon = (ImageView) convertView.findViewById(R.id.categoryIcon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        subCategoryList = AppController.getInstance().getSubcategoriesOfCategory((String) getItem(position));


        viewHolder.category.setText((String) getItem(position));
        viewHolder.subcategories.setText(subCategoryList.toString());



        return convertView;
    }
}

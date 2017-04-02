package com.thelogicalcoder.viazene.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.R;

import java.util.List;

/**
 * Created by Aditya on 028, 28 July 2015.
 */
public class MaterialArrayAdapter extends BaseAdapter {

    private Context context;
    private int layoutResource;
    private List<String> collection;
    private LayoutInflater layoutInflater;
    private String whatList;

    private class ViewHolder {
        TextView textView;
        CheckBox checkBox;
    }

    public MaterialArrayAdapter(Context context, int layoutResource, List<String> collection, String whatList) {
        this.context = context;
        this.layoutResource = layoutResource;
        this.collection = collection;
        this.whatList = whatList;
    }

    @Override
    public int getCount() {
        return collection.size();
    }

    @Override
    public Object getItem(int position) {
        return collection.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (layoutInflater == null)
            layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = layoutInflater.inflate(layoutResource, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.text1);
            try {
                viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            } catch (Exception e) {
                e.printStackTrace();
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (this.whatList.trim().equalsIgnoreCase("orders")) {
            String productID = ((String) getItem(position)).substring(0, ((String) getItem(position)).indexOf("\nOrder ID"));
            String orderID = ((String) getItem(position)).substring(((String) getItem(position)).indexOf("\nOrder ID") + 1, ((String) getItem(position)).length());
            viewHolder.textView.setText(AppController.getInstance().getProductNameByID(productID) + "\n" + orderID);
        } else if (this.whatList.trim().equalsIgnoreCase("colorList")) {
            viewHolder.textView.setText((String) getItem(position));
        } else if (this.whatList.trim().equalsIgnoreCase("cart")) {
            viewHolder.textView.setText(AppController.getInstance().getProductNameByID((String) getItem(position)));
        } else viewHolder.textView.setText((String) getItem(position));


        return convertView;
    }
}

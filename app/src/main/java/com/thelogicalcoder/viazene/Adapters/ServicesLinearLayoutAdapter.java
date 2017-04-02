package com.thelogicalcoder.viazene.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.thelogicalcoder.viazene.R;

import java.util.List;

/**
 * Created by Aditya on 011, 11 July 2015.
 */
public class ServicesLinearLayoutAdapter extends BaseAdapter {

    private List<String> serviceCategoryDataList;
    private Context context;
    private LayoutInflater inflater;

    public ServicesLinearLayoutAdapter(Context context, List<String> serviceCategoryDataList) {
        this.serviceCategoryDataList = serviceCategoryDataList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return serviceCategoryDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return serviceCategoryDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.services_card_linear_layout, null);
        }

        /*SubsamplingScaleImageView serviceBanner = (SubsamplingScaleImageView) convertView.findViewById(R.id.banner);*/
        ImageView serviceBanner=(ImageView)convertView.findViewById(R.id.banner);
        TextView serviceText = (TextView) convertView.findViewById(R.id.textView);


        serviceText.setText(serviceCategoryDataList.get(position).trim().toUpperCase());
        switch (serviceCategoryDataList.get(position).toLowerCase().trim()) {
            case "mehndi":
                //serviceBanner.setImage(ImageSource.resource(R.drawable.mehndi_banner));
                serviceBanner.setImageResource(R.drawable.mehndi_banner);
                break;
            case "tattoo":
                //serviceBanner.setImage(ImageSource.resource(R.drawable.tatto_banner));
                serviceBanner.setImageResource(R.drawable.tatto_banner);
                break;
            case "image consultant":
                //serviceBanner.setImage(ImageSource.resource(R.drawable.imageconsultant_banner));
                serviceBanner.setImageResource(R.drawable.imageconsultant_banner);
                break;
            case "bride":
                //serviceBanner.setImage(ImageSource.resource(R.drawable.bridal_banner));
                serviceBanner.setImageResource(R.drawable.bridal_banner);
                break;
            case "photoshoot":
                //serviceBanner.setImage(ImageSource.resource(R.drawable.photoshoot_banner));
                serviceBanner.setImageResource(R.drawable.photoshoot_banner);
                break;
            case "massage":
                //serviceBanner.setImage(ImageSource.resource(R.drawable.massage_banner));
                serviceBanner.setImageResource(R.drawable.massage_banner);
                break;
        }

        return convertView;
    }
}

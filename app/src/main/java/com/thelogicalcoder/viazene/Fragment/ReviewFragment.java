package com.thelogicalcoder.viazene.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.R;

public class ReviewFragment extends android.support.v4.app.Fragment {
    TextView reviewText;
    ProgressWheel progressWheel;
    RelativeLayout overLay;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_review, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        super.onViewCreated(view, savedInstanceState);
    }

    public void showReviewText() {
        try {
            reviewText = (TextView) view.findViewById(R.id.reviewText);
            progressWheel = (ProgressWheel) view.findViewById(R.id.pbar);
            overLay = (RelativeLayout) view.findViewById(R.id.overLay);
            reviewText.setText(
                    "Name: " + AppController.getInstance().getTempUserInfo().get(0) + "\n" +
                            "Phone: " + AppController.getInstance().getTempUserInfo().get(1) + "\n" +
                            "Address: " + AppController.getInstance().getTempUserInfo().get(2) + "\n" +
                            "D.O.B.: " + AppController.getInstance().getTempUserInfo().get(3) + "\n" +
                            "Pin Code: " + AppController.getInstance().getTempUserInfo().get(4)
            );
            AppController.getInstance().getTempUserInfo().clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startLoad() {
        overLay.setVisibility(View.VISIBLE);
    }

    public void endLoad() {
        overLay.setVisibility(View.GONE);
    }
}
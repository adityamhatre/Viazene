package com.thelogicalcoder.viazene.Interfaces;

/**
 * Created by Aditya on 030, 30 July 2015.
 */
import android.widget.AbsListView;

public interface ScrollTabHolder {

    void adjustScroll(int scrollHeight);

    void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition);
}
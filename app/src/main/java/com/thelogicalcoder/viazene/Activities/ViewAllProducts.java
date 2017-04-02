package com.thelogicalcoder.viazene.Activities;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.nineoldandroids.view.ViewHelper;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Fragment.ViewAllProductsFragment;
import com.thelogicalcoder.viazene.Helper.AlphaForegroundColorSpan;
import com.thelogicalcoder.viazene.Helper.KenBurnsSupportView;
import com.thelogicalcoder.viazene.Helper.ScrollTabHolderFragment;
import com.thelogicalcoder.viazene.Interfaces.ScrollTabHolder;
import com.thelogicalcoder.viazene.R;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class ViewAllProducts extends AppCompatActivity implements ScrollTabHolder, ViewPager.OnPageChangeListener {

    private static AccelerateDecelerateInterpolator sSmoothInterpolator = new AccelerateDecelerateInterpolator();

    private KenBurnsSupportView mHeaderPicture;
    private View mHeader;

    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    private int mActionBarHeight;
    private int mMinHeaderHeight;
    private int mHeaderHeight;
    private int mMinHeaderTranslation;
    private ImageView mHeaderLogo;

    private RectF mRect1 = new RectF();
    private RectF mRect2 = new RectF();

    private TypedValue mTypedValue = new TypedValue();
    private SpannableString mSpannableString;
    private AlphaForegroundColorSpan mAlphaForegroundColorSpan;
    private ImageView toolbarIcon,filterIcon;

    private int position;
    CheckBox priceFilter, colorFilter;
    Button sortByDiscount, sortByPriceASC, sortByPriceDESC;
    float min = -1, max = -1;
    Boolean minSet = false, maxSet = false;
    String selectedColor = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_all_products);

        toolbarIcon = (ImageView) findViewById(R.id.toolBarIcon);

        try {
            position = getIntent().getIntExtra("position", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //toolbar.setTitle("Title");
        // toolbar.setSubtitle("Sub");
        //toolbar.setNavigationIcon(R.drawable.ic_header);
        toolbarIcon.setImageResource(R.drawable.ic_header);

        mMinHeaderHeight = getResources().getDimensionPixelSize(R.dimen.min_header_height);
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mMinHeaderTranslation = -mMinHeaderHeight + getActionBarHeight();
        mHeaderPicture = (KenBurnsSupportView) findViewById(R.id.header_picture);
        //mHeaderPicture.setResourceIds(R.drawable.clothing_banner, R.drawable.bags_banner, R.drawable.jewellery_banner, R.drawable.footwear_banner);
        mHeaderPicture.setResourceId(R.drawable.clothing_banner);
        mHeaderLogo = (ImageView) findViewById(R.id.header_logo);
        filterIcon= (ImageView) findViewById(R.id.filterButton);
        mHeader = findViewById(R.id.header);

        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(4);

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPagerAdapter.setTabHolderScrollingContent(this);


        mViewPager.setAdapter(mPagerAdapter);

        mViewPager.setCurrentItem(position);
        switch (position) {
            case 0:
                mHeaderLogo.setImageResource(R.drawable.clothing);
                mHeaderPicture.setResourceId(R.drawable.clothing_banner);
                toolbarIcon.setImageResource(R.drawable.clothing);
                break;
            case 1:
                mHeaderLogo.setImageResource(R.drawable.bags);
                mHeaderPicture.setResourceId(R.drawable.bags_banner);
                toolbarIcon.setImageResource(R.drawable.bags);
                break;
            case 2:
                mHeaderLogo.setImageResource(R.drawable.jewellery);
                mHeaderPicture.setResourceId(R.drawable.jewellery_banner);
                toolbarIcon.setImageResource(R.drawable.jewellery);
                break;
            case 3:
                mHeaderLogo.setImageResource(R.drawable.footwear);
                mHeaderPicture.setResourceId(R.drawable.footwear_banner);
                toolbarIcon.setImageResource(R.drawable.footwear);
                break;
        }
        
        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mPagerSlidingTabStrip.setViewPager(mViewPager);
        mPagerSlidingTabStrip.setTextColor(Color.WHITE);
        mPagerSlidingTabStrip.setIndicatorColor(Color.WHITE);
        mPagerSlidingTabStrip.setOnPageChangeListener(this);
        mSpannableString = new SpannableString(getString(R.string.actionbar_title));
        mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(0xffffffff);

        ViewHelper.setAlpha(getActionBarIconView(), 0f);

//        getSupportActionBar().setBackgroundDrawable(null);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // nothing
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // nothing
    }

    @Override
    public void onPageSelected(int position) {
        SparseArrayCompat<ScrollTabHolder> scrollTabHolders = mPagerAdapter.getScrollTabHolders();
        ScrollTabHolder currentHolder = scrollTabHolders.valueAt(position);
        System.out.println("on page " + (position + 1));

        switch (position) {
            case 0:
                mHeaderLogo.setImageResource(R.drawable.clothing);
                mHeaderPicture.setResourceId(R.drawable.clothing_banner);
                toolbarIcon.setImageResource(R.drawable.clothing);
                break;
            case 1:
                mHeaderLogo.setImageResource(R.drawable.bags);
                mHeaderPicture.setResourceId(R.drawable.bags_banner);
                toolbarIcon.setImageResource(R.drawable.bags);
                break;
            case 2:
                mHeaderLogo.setImageResource(R.drawable.jewellery);
                mHeaderPicture.setResourceId(R.drawable.jewellery_banner);
                toolbarIcon.setImageResource(R.drawable.jewellery);
                break;
            case 3:
                mHeaderLogo.setImageResource(R.drawable.footwear);
                mHeaderPicture.setResourceId(R.drawable.footwear_banner);
                toolbarIcon.setImageResource(R.drawable.footwear);
                break;
        }
        try {
            currentHolder.adjustScroll((int) (mHeader.getHeight() + ViewHelper.getTranslationY(mHeader)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition) {
        if (mViewPager.getCurrentItem() == pagePosition) {
            int scrollY = getScrollY(view);
            ViewHelper.setTranslationY(mHeader, Math.max(-scrollY, mMinHeaderTranslation));
            float ratio = clamp(ViewHelper.getTranslationY(mHeader) / mMinHeaderTranslation, 0.0f, 1.0f);
            interpolate(mHeaderLogo, getActionBarIconView(), sSmoothInterpolator.getInterpolation(ratio));
            setTitleAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));
        }
    }

    @Override
    public void adjustScroll(int scrollHeight) {
        // nothing
    }

    public int getScrollY(AbsListView view) {
        View c = view.getChildAt(0);
        if (c == null) {
            return 0;
        }

        int firstVisiblePosition = view.getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = mHeaderHeight;
        }

        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    public static float clamp(float value, float max, float min) {
        return Math.max(Math.min(value, min), max);
    }

    private void interpolate(View view1, View view2, float interpolation) {
        getOnScreenRect(mRect1, view1);
        getOnScreenRect(mRect2, view2);

        float scaleX = 1.0F + interpolation * (mRect2.width() / mRect1.width() - 1.0F);
        float scaleY = 1.0F + interpolation * (mRect2.height() / mRect1.height() - 1.0F);
        float translationX = 0.5F * (interpolation * (mRect2.left + mRect2.right - mRect1.left - mRect1.right));
        float translationY = 0.5F * (interpolation * (mRect2.top + mRect2.bottom - mRect1.top - mRect1.bottom));

        ViewHelper.setTranslationX(view1, translationX);
        ViewHelper.setTranslationY(view1, translationY - ViewHelper.getTranslationY(mHeader));
        ViewHelper.setScaleX(view1, scaleX);
        ViewHelper.setScaleY(view1, scaleY);
    }

    private RectF getOnScreenRect(RectF rect, View view) {
        rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        return rect;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public int getActionBarHeight() {
        if (mActionBarHeight != 0) {
            return mActionBarHeight;
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue, true);
        } else {
            getTheme().resolveAttribute(R.attr.actionBarSize, mTypedValue, true);
        }

        mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data, getResources().getDisplayMetrics());

        return mActionBarHeight;
    }

    private void setTitleAlpha(float alpha) {
        mAlphaForegroundColorSpan.setAlpha(alpha);
        mSpannableString.setSpan(mAlphaForegroundColorSpan, 0, mSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        getSupportActionBar().setTitle(mSpannableString);

    }

    private ImageView getActionBarIconView() {
        //return (ImageView) getNavigationIcon(toolbar);
        return (ImageView) findViewById(R.id.toolBarIcon);
    }

    private View getLogoView(Toolbar toolbar) {
        for (int i = 0; i < toolbar.getChildCount(); i++)
            if (toolbar.getChildAt(i) instanceof ImageView)
                return toolbar.getChildAt(i);

        return null;
    }

    public View getNavigationIcon(Toolbar toolbar) {
        //check if contentDescription previously was set
        boolean hadContentDescription = TextUtils.isEmpty(toolbar.getNavigationContentDescription());
        String contentDescription = !hadContentDescription ? toolbar.getNavigationContentDescription().toString() : "navigationIcon";
        toolbar.setNavigationContentDescription(contentDescription);
        ArrayList<View> potentialViews = new ArrayList<View>();
        //find the view based on it's content description, set programatically or with android:contentDescription
        toolbar.findViewsWithText(potentialViews, contentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        //Nav icon is always instantiated at this point because calling setNavigationContentDescription ensures its existence
        View navIcon = null;
        if (potentialViews.size() > 0) {
            navIcon = potentialViews.get(0); //navigation icon is ImageButton
        }
        //Clear content description if not previously present
        if (hadContentDescription)
            toolbar.setNavigationContentDescription(null);
        return navIcon;
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        private SparseArrayCompat<ScrollTabHolder> mScrollTabHolders;
        //private final String[] TITLES = {"Clothing", "Bags", "Jewellery", "Footwear"};
        private final List<String> TITLES = AppController.getInstance().getProductCategories();
        private ScrollTabHolder mListener;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            mScrollTabHolders = new SparseArrayCompat<ScrollTabHolder>();
        }

        public void setTabHolderScrollingContent(ScrollTabHolder listener) {
            mListener = listener;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES.get(position);
        }

        @Override
        public int getCount() {
            return TITLES.size();
        }

        @Override
        public Fragment getItem(int position) {
            ScrollTabHolderFragment fragment = (ScrollTabHolderFragment) ViewAllProductsFragment.newInstance(position);

            mScrollTabHolders.put(position, fragment);
            if (mListener != null) {
                fragment.setScrollTabHolder(mListener);
            }

            return fragment;
        }

        public SparseArrayCompat<ScrollTabHolder> getScrollTabHolders() {
            return mScrollTabHolders;
        }

    }

    @Override
    protected void onPause() {

        super.onPause();
    }
}
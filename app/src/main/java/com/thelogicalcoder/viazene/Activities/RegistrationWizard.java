package com.thelogicalcoder.viazene.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.balysv.materialripple.MaterialRippleLayout;
import com.blunderer.materialdesignlibrary.activities.Activity;
import com.blunderer.materialdesignlibrary.adapters.ViewPagerAdapter;
import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;
import com.blunderer.materialdesignlibrary.models.ViewPagerItem;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Fragment.BasicDetailsFragment;
import com.thelogicalcoder.viazene.Fragment.ContactFragment;
import com.thelogicalcoder.viazene.Fragment.NameFragment;
import com.thelogicalcoder.viazene.Fragment.ReviewFragment;
import com.thelogicalcoder.viazene.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aditya on 022, 22 July 2015.
 */
public class RegistrationWizard extends Activity {
    protected ViewPager mViewPager;
    protected CirclePageIndicator mViewPagerIndicator;
    protected Button nextButton, previousButton;
    protected MaterialRippleLayout nextRipple;
    private List<ViewPagerItem> mViewPagerItems;
    private NameFragment nameFragment;
    private ContactFragment contactFragment;
    private BasicDetailsFragment basicDetailsFragment;
    private ReviewFragment reviewFragment;

    private String userName, phoneNumber, address, pinCode, dob;

    private final ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager
            .OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            replaceTitle(mViewPagerItems.get(position).getTitle()+": Step "+(position+1)+" of 4");
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        setupViewPager();

        replaceTitle(mViewPagerItems.get(0).getTitle());
        nextButton = (Button) findViewById(R.id.nextButton);
        previousButton = (Button) findViewById(R.id.previousButton);
        previousButton.setVisibility(View.GONE);
        nextRipple = (MaterialRippleLayout) findViewById(R.id.ripple);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mViewPager.getCurrentItem()) {
                    case 0:
                        previousButton.setVisibility(View.GONE);
                        if (!nameFragment.getUserName().trim().isEmpty()) {
                            userName = nameFragment.getUserName().trim();
                            mViewPager.setCurrentItem(1, true);
                            previousButton.setVisibility(View.VISIBLE);
                        } else {
                            nameFragment.setFocusForUser();
                            nameFragment.setErrorForUser("Enter Name");
                        }
                        break;
                    case 1:
                        Boolean phoneValid = false, address1Valid = false, address2Valid = false, address3Valid = false;
                        if (!contactFragment.getPhoneNumber().trim().isEmpty()) {
                            if (contactFragment.getPhoneNumber().trim().length() == 10) {
                                phoneValid = true;
                            } else {
                                contactFragment.setErrorForPhone("Enter correct phone number");
                                phoneValid = false;
                                contactFragment.setFocusForPhone();
                            }
                        } else {
                            contactFragment.setErrorForPhone("Enter phone number");
                            phoneValid = false;
                        }

                        if (!contactFragment.getAddress1().trim().isEmpty()) {
                            address1Valid = true;
                        } else {
                            contactFragment.setErrorForAddress1("Enter address");
                            address1Valid = false;
                            contactFragment.setFocusForAddress1();
                        }

                        if (!contactFragment.getAddress2().trim().isEmpty()) {
                            address2Valid = true;
                        } else {
                            contactFragment.setErrorForAddress2("Enter address");
                            address2Valid = false;
                            contactFragment.setFocusForAddress2();
                        }

                        if (!contactFragment.getAddress3().trim().isEmpty()) {
                            address3Valid = true;
                        } else {
                            contactFragment.setErrorForAddress3("Enter address");
                            address3Valid = false;
                            contactFragment.setFocusForAddress3();
                        }

                        if (phoneValid && address1Valid && address2Valid && address3Valid) {
                            address = contactFragment.getAddress1() + ", " + contactFragment.getAddress2() + ", " + contactFragment.getAddress3();
                            phoneNumber = contactFragment.getPhoneNumber();
                            mViewPager.setCurrentItem(2, true);
                            previousButton.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 2:
                        Boolean pinSet = false;
                        if (basicDetailsFragment.dobSet) {
                            dob = basicDetailsFragment.getDOB();
                        } else
                            Snackbar.make(findViewById(R.id.root), "Enter Date of Birth", Snackbar.LENGTH_SHORT).show();

                        if (basicDetailsFragment.getPinCode().isEmpty()) {
                            basicDetailsFragment.setPinCodeError("Enter pin code");
                        } else if (basicDetailsFragment.getPinCode().length() != 6) {
                            basicDetailsFragment.setPinCodeError("Enter valid pin code");
                        } else {
                            pinCode = basicDetailsFragment.getPinCode();
                            pinSet = true;
                        }

                        if (pinSet && basicDetailsFragment.dobSet) {
                            mViewPager.setCurrentItem(3, true);
                            List<String> tempUserInfo = new ArrayList<String>();
                            tempUserInfo.add(userName);
                            tempUserInfo.add(phoneNumber);
                            tempUserInfo.add(address);
                            tempUserInfo.add(dob);
                            tempUserInfo.add(pinCode);
                            System.out.println(tempUserInfo);
                            AppController.getInstance().setTempUserInfo(tempUserInfo);
                            reviewFragment.showReviewText();
                            tempUserInfo.clear();
                        }
                        break;
                    case 3:

                        //nextButton.setVisibility(View.GONE);
                        System.out.println("Name: " + userName);
                        System.out.println("Phone Number: " + phoneNumber);
                        System.out.println("Address: " + address);
                        System.out.println("Date of Birth: " + dob);
                        System.out.println("PinCode: " + pinCode);
                        List<String> details = new ArrayList<String>();
                        details.add(userName);
                        details.add(phoneNumber);
                        details.add(address);
                        details.add(dob);
                        details.add(pinCode);
                        details.add(getIntent().getStringExtra("email"));
                        details.add(getIntent().getStringExtra("password"));

                        startActivity(new Intent(RegistrationWizard.this, VerifyPhoneActivity.class).putStringArrayListExtra("details", (ArrayList<String>) details));
                        /*reviewFragment.startLoad();
                        new Register(RegistrationWizard.this, getIntent().getStringExtra("email"), getIntent().getStringExtra("password"), userName.trim(), dob.trim(), address.trim(), pinCode.trim(), phoneNumber.trim(), new onRegisterListener() {
                            @Override
                            public void onRegistered(String response) {
                                if (response.trim().equalsIgnoreCase("success")) {
                                    AppController.getInstance().setRegistrationComplete(true);
                                    finish();
                                    //startActivity(new Intent(RegistrationWizard.this,VerifyPhoneActivity.class));
                                } else {
                                    AppController.getInstance().setRegistrationComplete(false);
                                    nextButton.setVisibility(View.VISIBLE);
                                    Snackbar.make(findViewById(R.id.root), "Some error occurred", Snackbar.LENGTH_SHORT).show();
                                    reviewFragment.endLoad();
                                }
                            }
                        }).execute();*/
                        break;
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mViewPager.getCurrentItem()) {
                    case 3:
                        mViewPager.setCurrentItem(2, true);
                        break;
                    case 2:
                        mViewPager.setCurrentItem(1, true);
                        break;
                    case 1:
                        mViewPager.setCurrentItem(0, true);
                        previousButton.setVisibility(View.GONE);
                        break;
                }
            }
        });

    }

    private void showIndicator(ViewPager pager) {
        mViewPagerIndicator = (CirclePageIndicator) findViewById(R.id.viewpagerindicator);
        mViewPagerIndicator.setViewPager(pager);
        mViewPagerIndicator.setVisibility(View.VISIBLE);
        mViewPagerIndicator.setOnPageChangeListener(mOnPageChangeListener);
        mViewPagerIndicator.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }

    void setupViewPager() {
        mViewPager.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });
        mViewPagerItems = new ArrayList<>();

        ViewPagerItem viewPagerItem = new ViewPagerItem();
        nameFragment = new NameFragment();
        viewPagerItem.setPagerItem("Name", nameFragment);

        ViewPagerItem viewPagerItem2 = new ViewPagerItem();
        contactFragment = new ContactFragment();
        viewPagerItem2.setPagerItem("Contact", contactFragment);

        ViewPagerItem viewPagerItem3 = new ViewPagerItem();
        basicDetailsFragment = new BasicDetailsFragment();
        viewPagerItem3.setPagerItem("Basic Details", basicDetailsFragment);

        ViewPagerItem viewPagerItem4 = new ViewPagerItem();
        reviewFragment = new ReviewFragment();
        viewPagerItem4.setPagerItem("Review Details", reviewFragment);

        mViewPagerItems.add(viewPagerItem);
        mViewPagerItems.add(viewPagerItem2);
        mViewPagerItems.add(viewPagerItem3);
        mViewPagerItems.add(viewPagerItem4);

        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mViewPagerItems));
        showIndicator(mViewPager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppController.getInstance().isRegistrationComplete()) {
            finish();
        } else {

        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_registration_wizard;
    }

    @Override
    protected boolean enableActionBarShadow() {
        return false;
    }

    @Override
    protected ActionBarHandler getActionBarHandler() {
        return null;
    }

    private void replaceTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
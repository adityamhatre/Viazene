package com.thelogicalcoder.viazene.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blunderer.materialdesignlibrary.activities.Activity;
import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.Application.SelectedService;
import com.thelogicalcoder.viazene.AsyncTasks.BuyServiceCODAsync;
import com.thelogicalcoder.viazene.Data.ServiceData;
import com.thelogicalcoder.viazene.Data.ServiceHelperData;
import com.thelogicalcoder.viazene.Helper.Data;
import com.thelogicalcoder.viazene.Interfaces.onAsyncCallBack;
import com.thelogicalcoder.viazene.R;
import com.thelogicalcoder.viazene.Server.Server;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import babushkatext.BabushkaText;

public class SingleServiceView extends Activity {

    ServiceData serviceData;
    List<ServiceHelperData> serviceHelperDataList;
    SliderLayout sliderShow;
    LinearLayout checkBoxHolder;
    List<SelectedService> selectedServiceList = new ArrayList<>();

    BabushkaText productName, productPrice, productDescription;
    Button buyNow, getQuotes;
    //ImageView shareButton, cartButton;
    float total = 0;

    public static long differenceInHours(java.sql.Timestamp currentTime, java.sql.Timestamp oldTime) {
        long milliseconds1 = oldTime.getTime();
        long milliseconds2 = currentTime.getTime();

        long diff = milliseconds2 - milliseconds1;
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        diffHours++;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        return diffHours;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceData = new Data().getServiceFromID(getIntent().getStringExtra("serviceID"));
        serviceHelperDataList = new ArrayList<>();
        sliderShow = (SliderLayout) findViewById(R.id.slider);
        checkBoxHolder = (LinearLayout) findViewById(R.id.checkboxHolder);
        buyNow = (Button) findViewById(R.id.buyNow);
        getQuotes = (Button) findViewById(R.id.getQuotesButton);
        for (int i = 0; i < AppController.getInstance().getServiceImageURLList(serviceData).size(); i++) {
            final DefaultSliderView imageSlide = new DefaultSliderView(this);
            imageSlide.empty(R.drawable.logo_loading);
            imageSlide
                    .image((Server.SERVICES_DOMAIN + serviceData.getCategory().trim() + "/" + serviceData.getName().trim() + "/" + AppController.getInstance().getServiceImageURLList(serviceData).get(i)).replace(" ", "%20"))
                    .setScaleType(BaseSliderView.ScaleType.CenterInside);
            imageSlide.error(R.drawable.error);
            final int pos = i;
            imageSlide.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView baseSliderView) {
                    View view = View.inflate(SingleServiceView.this, R.layout.zoom_image_view, null);
                    WebView webView = (WebView) view.findViewById(R.id.webView);
                    final ImageView imageView = (ImageView) view.findViewById(R.id.cover);
                    webView.loadUrl((Server.SERVICES_DOMAIN + serviceData.getCategory().trim() + "/" + serviceData.getName().trim() + "/" + AppController.getInstance().getServiceImageURLList(serviceData).get(pos)).replace(" ", "%20"));
                    WebSettings settings = webView.getSettings();
                    settings.setJavaScriptEnabled(true);
                    webView.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            imageView.setVisibility(View.GONE);
                            super.onPageFinished(view, url);
                        }
                    });
                    settings.setSupportZoom(true);
                    settings.setBuiltInZoomControls(true);
                    new AlertDialog.Builder(SingleServiceView.this).setView(view).create().show();
                }
            });
            sliderShow.addSlider(imageSlide);
        }
        sliderShow.setDuration(4000);
        /*productName = (BabushkaText) findViewById(R.id.productName);
        productPrice = (BabushkaText) findViewById(R.id.productPrice);
        productDescription = (BabushkaText) findViewById(R.id.descriptionText);
        shareButton = (ImageView) findViewById(R.id.shareButton);
        cartButton = (ImageView) findViewById(R.id.cartButton);
        productName.addPiece(new BabushkaText.Piece.Builder(serviceData.getName()).textColor(Color.parseColor("#000000")).textSize(70).build());
        productName.display();


        productDescription = (BabushkaText) findViewById(R.id.descriptionText);

        productDescription.addPiece(new BabushkaText.Piece.Builder((serviceData.getDetails())).textSize(50).build());
        productDescription.display();*/

        sliderShow.startAutoCycle();

        productDescription = (BabushkaText) findViewById(R.id.descriptionText);
        productDescription.addPiece(new BabushkaText.Piece.Builder((serviceData.getDetails())).textSize(50).build());
        /*productDescription.addPiece(new BabushkaText.Piece.Builder(("\n\nAVAILABLE AT THESE PINCODES")).textSize(50).style(Typeface.BOLD).build());
        for (int i = 0; i < serviceData.getPinCodeAvailableList().size(); i++) {
            productDescription.addPiece(new BabushkaText.Piece.Builder(("\n• " + serviceData.getPinCodeAvailableList().get(i))).textSize(50).build());
        }*/
        productDescription.display();


        for (int i = 0; i < AppController.getInstance().getServices().getServicesHelper().size(); i++) {
            if (serviceData.getServiceID().trim().equalsIgnoreCase(AppController.getInstance().getServices().getServicesHelper().get(i).getServiceID())) {
                serviceHelperDataList.add(AppController.getInstance().getServices().getServicesHelper().get(i));
            }
        }


        for (int i = 0; i < serviceHelperDataList.size(); i++) {
            View view = View.inflate(this, R.layout.checkbox_service_helper, null);
            final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            checkBox.setText(serviceHelperDataList.get(i).getSubService());
            final int pos = i;
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    final SelectedService selectedService = new SelectedService();
                    if (isChecked) {
                        final Calendar now = Calendar.getInstance();
                        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                        @SuppressWarnings("unused")
                                        String date = "";
                                        if (dayOfMonth <= 9) {
                                            date = "0" + dayOfMonth + "/" + (++monthOfYear) + "/" + year;
                                        } else
                                            date = dayOfMonth + "/" + (++monthOfYear) + "/" + year;

                                        final String finalDate = date;
                                        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i1) {
                                                String time = "";
                                                if (i >= 13) {
                                                    i = i - 12;
                                                    time = i1 >= 10 ? i + ":" + i1 + " PM" : i + ":0" + i1 + " PM";
                                                } else if (i == 12) {
                                                    time = i1 >= 10 ? i + ":" + i1 + " PM" : i + ":0" + i1 + " PM";
                                                } else
                                                    time = i1 >= 10 ? i + ":" + i1 + " AM" : i + ":0" + i1 + " AM";

                                                Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
                                                Timestamp selectedTimeStamp = null;
                                                try {
                                                    String sTime = finalDate + " " + time;
                                                    System.out.println(sTime);
                                                    selectedTimeStamp = new Timestamp(new SimpleDateFormat("dd/MM/yyyy h:mm aa").parse(sTime).getTime());

                                                    System.out.println("-----\n-----\n----\n");
                                                    System.out.println("Current time: " + currentTimeStamp);
                                                    System.out.println("Selected time: " + selectedTimeStamp);
                                                    System.out.println("Difference in hours is: " + differenceInHours(selectedTimeStamp, currentTimeStamp));
                                                    System.out.println("-----\n-----\n----");

                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                if (differenceInHours(selectedTimeStamp, currentTimeStamp) <= 3) {
                                                    Snackbar.make(findViewById(R.id.root), "Please select at least 3 hours late than current time", Snackbar.LENGTH_SHORT).show();
                                                    checkBox.setChecked(false);
                                                } else {
                                                    Calendar calender = Calendar.getInstance();
                                                    calender.setTime(new Date(selectedTimeStamp.getTime()));
                                                    int hour = calender.get(Calendar.HOUR_OF_DAY);
                                                    int minute = calender.get(Calendar.MINUTE);
                                                    if (hour >= serviceData.getFromTime() && hour < serviceData.getToTime()) {
                                                        selectedService.setDate(finalDate);
                                                        selectedService.setTime(time);
                                                        selectedService.setServiceHelperData(serviceHelperDataList.get(pos));
                                                        selectedServiceList.add(selectedService);
                                                        //buy();
                                                    } else {
                                                        Snackbar.make(findViewById(R.id.root), ("Please select between " + (serviceData.getFromTime() > 12 ? serviceData.getFromTime() - 12 + " PM" : (serviceData.getFromTime() == 12 ? serviceData.getFromTime() + " PM" : serviceData.getFromTime() == 0 ? (serviceData.getFromTime() + 12) + " AM" : serviceData.getFromTime() + " AM")) + " to " + (serviceData.getToTime() > 12 ? serviceData.getToTime() - 12 + " PM" : (serviceData.getToTime() == 12 ? serviceData.getToTime() + " PM" : serviceData.getToTime() + " AM"))), Snackbar.LENGTH_SHORT).show();
                                                        checkBox.setChecked(false);
                                                    }
                                                }
                                            }
                                        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false);

                                        timePickerDialog.show(SingleServiceView.this.getFragmentManager(), "TIME");

                                        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialog) {
                                                checkBox.setChecked(false);
                                            }
                                        });
                                    }
                                },
                                now.get(Calendar.YEAR),
                                now.get(Calendar.MONTH),
                                now.get(Calendar.DAY_OF_MONTH)
                        );
                        datePickerDialog.setFirstDayOfWeek(1);
                        datePickerDialog.setMinDate(now);
                        datePickerDialog.show(SingleServiceView.this.getFragmentManager(), "DATE");
                        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                checkBox.setChecked(false);
                            }
                        });

                    } else {
                        for (int j = 0; j < selectedServiceList.size(); j++) {
                            if (selectedServiceList.get(j).getServiceHelperData().getSubService().equalsIgnoreCase(serviceHelperDataList.get(pos).getSubService())) { //TODO ID is same of all subservices in a service....therefore using the subservice name as a primary key
                                selectedServiceList.remove(j);
                            }
                        }
                    }

                }
            });
            ((TextView) (findViewById(R.id.timings))).setText("Available from " + (serviceData.getFromTime() > 12 ? serviceData.getFromTime() - 12 + " PM" : (serviceData.getFromTime() == 12 ? serviceData.getFromTime() + " PM" : serviceData.getFromTime() == 0 ? (serviceData.getFromTime() + 12) + " AM" : serviceData.getFromTime() + " AM")) + " to " + (serviceData.getToTime() > 12 ? serviceData.getToTime() - 12 + " PM" : (serviceData.getToTime() == 12 ? serviceData.getToTime() + " PM" : serviceData.getToTime() + " AM")));
            checkBoxHolder.addView(view);
        }

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buy();
            }
        });

        getQuotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //:TODO MAYBE IN NEXT UPDATE         View view=View.inflate(SingleServiceView.this,R.layout.get_quotes,null);

                /*AppController.getInstance().showMaterialProgress(SingleServiceView.this);
                new SendGetQuotesEmail(SingleServiceView.this, serviceData, new onAsyncCallBack() {
                    @Override
                    public void onCallBack(String response) {
                        AppController.getInstance().dismissMaterialProgress();
                        Snackbar.make(findViewById(R.id.root), "Request for quoataion sent", Snackbar.LENGTH_SHORT).show();
                    }
                }).execute();*/
                if (AppController.getInstance().isLoggedIn()) {
                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"services.quotations@viazene.com"});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "QUOTATION REQUEST");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "There is a request for quotation for the service \"" + serviceData.getName() + "(Service ID: " + serviceData.getServiceID() + ")\" by the user \"" + AppController.getInstance().getUserInfo().getUser() + "(" + AppController.getInstance().getUserInfo().getEmail() + ")\"\nContact number: " + AppController.getInstance().getUserInfo().getContact());
                    startActivity(Intent.createChooser(emailIntent, "Send quotation request"));
                } else
                    Snackbar.make(findViewById(R.id.root), "Login to request service quotes", Snackbar.LENGTH_LONG).setAction("LOGIN", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(SingleServiceView.this, LoginRegisterActivity.class).putExtra("source", "ssv"));
                        }
                    }).show();
            }
        });


        getSupportActionBar().setTitle(serviceData.getName());
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


       /* makeMyMatch = (Button) findViewById(R.id.makeMyMatchButton);
        makeMyMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buyNow = (Button) findViewById(R.id.buyNow);
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/


    }

    void buy()
    {
        if (AppController.getInstance().isLoggedIn()) {
            float total = 0;
            String showThis = "";
            for (int i = 0; i < selectedServiceList.size(); i++) {
                // showThis += "Service \"" + selectedServiceList.get(i).getServiceHelperData().getSubService() + "\" of \"" + serviceData.getName() + "\" booked for " + selectedServiceList.get(i).getDate() + " at " + selectedServiceList.get(i).getTime() + "\n\n";
                total = total + Float.parseFloat(selectedServiceList.get(i).getServiceHelperData().getPrice());
            }
            if (total == 0) {
                Snackbar.make(findViewById(R.id.root), "No service selected", Snackbar.LENGTH_SHORT).show();
            } else {
                showThis += "\nTotal cost is Rs. " + total + "/-";
                showThis = showThis.trim();
                final String finalShowThis = showThis;
                new AlertDialog.Builder(SingleServiceView.this).setTitle("Info").setMessage(showThis).setPositiveButton("Book", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppController.getInstance().showMaterialProgress(SingleServiceView.this);
                        new BuyServiceCODAsync(SingleServiceView.this, serviceData, finalShowThis, new onAsyncCallBack() {
                            @Override
                            public void onCallBack(String response) {
                                AppController.getInstance().dismissMaterialProgress();
                                if (!response.equalsIgnoreCase("errorOccurred")) {
                                    new AlertDialog.Builder(SingleServiceView.this).setTitle("Info").setMessage(("Service \"" + serviceData.getName() + "\" booked\nCheck your email(\"" + AppController.getInstance().getUserInfo().getEmail() + "\") for details").trim()).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(SingleServiceView.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                            finish();
                                        }
                                    }).setCancelable(false).create().show();
                                } else
                                    Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                            }
                        }).execute();
                    }
                }).setNegativeButton("Cancel", null).create().show();
            }
        } else {
            Snackbar.make(findViewById(R.id.root), "Login to book service", Snackbar.LENGTH_LONG).setAction("LOGIN", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SingleServiceView.this, LoginRegisterActivity.class).putExtra("source", "ssv"));
                }
            }).show();
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_single_service_view;
    }

    @Override
    protected ActionBarHandler getActionBarHandler() {
        return null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        sliderShow.stopAutoCycle();
    }


    @Override
    protected boolean enableActionBarShadow() {
        return false;
    }

}
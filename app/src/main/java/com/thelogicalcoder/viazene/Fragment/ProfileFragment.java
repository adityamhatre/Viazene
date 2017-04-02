package com.thelogicalcoder.viazene.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.thelogicalcoder.viazene.Activities.ChangeDP;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.AsyncTasks.GetUserInfo;
import com.thelogicalcoder.viazene.AsyncTasks.UpdateInfo;
import com.thelogicalcoder.viazene.Interfaces.onUpdateInfoUpdateListener;
import com.thelogicalcoder.viazene.Interfaces.onUserInfoLoadedListener;
import com.thelogicalcoder.viazene.JSONParsers.UserInfoParser;
import com.thelogicalcoder.viazene.R;
import com.thelogicalcoder.viazene.Volley.FeedImageView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

public class ProfileFragment extends android.support.v4.app.Fragment {

    private static final int MY_INTENT_CLICK = 302;
    TextView userName;
    FeedImageView profilePicView;
    ImageView editProfilePic;
    static View mainView;
    Button editInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_profile, container, false);
        return mainView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        userName = (TextView) view.findViewById(R.id.userName);
        profilePicView = (FeedImageView) view.findViewById(R.id.profilePicView);
        editProfilePic = (ImageView) view.findViewById(R.id.editProfilePic);
        editInfo = (Button) view.findViewById(R.id.editInfo);
        userName.setText(AppController.getInstance().getUserInfo().getUser() + "\n" + AppController.getInstance().getViazeneTotalPoints() + " VZ Points");
        profilePicView.setDefaultImageResId(R.drawable.profile1);

        profilePicView.setImageUrl(AppController.getInstance().getUserInfo().getDpURL().trim().replace(" ", "%20") + "?" + System.currentTimeMillis(), AppController.getInstance().getImageLoader());
        profilePicView.setErrorImageResId(R.drawable.profile1);

        editProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent();
                intent.setType("image/jpg");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select File"), MY_INTENT_CLICK);*/
                startActivity(new Intent(getActivity(), ChangeDP.class));
            }
        });

        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = View.inflate(getActivity(), R.layout.edit_info, null);
                final EditText userText, dobText, contactText, pinCodeText, addressText;
                final Button dateSel;
                userText = (EditText) view.findViewById(R.id.userText);
                dobText = (EditText) view.findViewById(R.id.dobText);
                contactText = (EditText) view.findViewById(R.id.contactText);
                pinCodeText = (EditText) view.findViewById(R.id.pinCodeText);
                addressText = (EditText) view.findViewById(R.id.addressText);
                dateSel = (Button) view.findViewById(R.id.changeDOB);


                userText.setText(AppController.getInstance().getUserInfo().getUser());
                dobText.setText(AppController.getInstance().getUserInfo().getDob());
                dobText.setEnabled(false);
                contactText.setText(AppController.getInstance().getUserInfo().getContact());
                pinCodeText.setText(AppController.getInstance().getUserInfo().getPincode());
                addressText.setText(AppController.getInstance().getUserInfo().getAddress());

                dateSel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar now = Calendar.getInstance();
                        DatePickerDialog dpd = DatePickerDialog.newInstance(
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                        @SuppressWarnings("unused")
                                        String date = "You picked the following date: " + dayOfMonth + "/" + (++monthOfYear) + "/" + year;
                                        dobText.setText("");
                                        if (dayOfMonth == 1) {
                                            dobText.setText(dobText.getText() + "" + dayOfMonth + "st");
                                        } else if (dayOfMonth == 2) {
                                            dobText.setText(dobText.getText() + "" + dayOfMonth + "nd");
                                        } else if (dayOfMonth == 3) {
                                            dobText.setText(dobText.getText() + "" + dayOfMonth + "rd");
                                        } else {
                                            dobText.setText(dobText.getText() + "" + dayOfMonth + "th");
                                        }

                                        String dobMonthText = "";
                                        switch (monthOfYear) {
                                            case 1:
                                                dobMonthText = " January, ";
                                                break;
                                            case 2:
                                                dobMonthText = " February, ";
                                                break;
                                            case 3:
                                                dobMonthText = " March, ";
                                                break;
                                            case 4:
                                                dobMonthText = " April, ";
                                                break;
                                            case 5:
                                                dobMonthText = " May, ";
                                                break;
                                            case 6:
                                                dobMonthText = " June, ";
                                                break;
                                            case 7:
                                                dobMonthText = " July, ";
                                                break;
                                            case 8:
                                                dobMonthText = " August, ";
                                                break;
                                            case 9:
                                                dobMonthText = " September, ";
                                                break;
                                            case 10:
                                                dobMonthText = " October, ";
                                                break;
                                            case 11:
                                                dobMonthText = " November, ";
                                                break;
                                            case 12:
                                                dobMonthText = " December, ";
                                                break;
                                        }
                                        dobText.setText(dobText.getText() + "" + dobMonthText);
                                        dobText.setText(dobText.getText() + "" + year);
                                    }
                                },
                                now.get(Calendar.YEAR) - 20,
                                Calendar.JANUARY,
                                1
                        );
                        dpd.setFirstDayOfWeek(1);
                        dpd.show(getActivity().getFragmentManager(), "DATE");
                    }
                });

                final AlertDialog alertDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setTitle("Update Info");
                alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                        .setOnClickListener
                                (new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         Boolean user = false, dob = false, contact = false, pinCode = false, address = false;
                                         if (userText.getText().toString().isEmpty()) {
                                             user = false;
                                             userText.setError("Enter user name");
                                         } else {
                                             user = true;
                                         }
                                         if (dobText.getText().toString().isEmpty()) {
                                             dob = false;
                                             dobText.setError("Enter DOB");
                                         } else {
                                             dob = true;
                                         }
                                         if (addressText.getText().toString().isEmpty()) {
                                             address = false;
                                             addressText.setError("Enter Address");
                                         } else {
                                             address = true;
                                         }
                                         if (pinCodeText.getText().toString().isEmpty()) {
                                             pinCode = false;
                                             pinCodeText.setError("Enter Pin Code");
                                         } else {
                                             pinCode = true;
                                         }
                                         if (contactText.getText().toString().isEmpty()) {
                                             contact = false;
                                             contactText.setError("Enter Contact");
                                         } else {
                                             contact = true;
                                         }

                                         if (user && address && contact && dob && pinCode) {
                                             new UpdateInfo(getActivity(),
                                                     userText.getText().toString().trim(),
                                                     dobText.getText().toString().trim(),
                                                     addressText.getText().toString().trim(),
                                                     pinCodeText.getText().toString().trim(),
                                                     contactText.getText().toString().trim(),
                                                     AppController.getInstance().getUserInfo().getEmail(),
                                                     new onUpdateInfoUpdateListener() {
                                                         @Override
                                                         public void onUpdateInfoUpdated(String response) {
                                                             if (response.trim().equalsIgnoreCase("success")) {
                                                                 alertDialog.dismiss();

                                                                 /*new GetProfile(getActivity(), AppController.getInstance().getUserInfo().getEmail(), new onProfileLoadedListener() {
                                                                     @Override
                                                                     public void onProfileLoaded(String response) {
                                                                         if (!response.equalsIgnoreCase("errorOccurred")) {
                                                                             new ProfileDetailsParser(response);
                                                                             userName.setText(AppController.getInstance().getUserInfo().getUser());
                                                                         } else {
                                                                             Snackbar.make(view.findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                                                         }
                                                                     }
                                                                 }).execute();*/
                                                                 new GetUserInfo(getActivity(), AppController.getInstance().getUserInfo().getEmail(), new onUserInfoLoadedListener() {
                                                                     @Override
                                                                     public void onUserInfoLoaded(String response) {
                                                                         System.out.println(response);
                                                                         if (response.equalsIgnoreCase("") || response.equalsIgnoreCase("errorOccurred")) {
                                                                             Snackbar.make(view.findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                                                         } else {
                                                                             new UserInfoParser(getActivity(), response);
                                                                             userName.setText(AppController.getInstance().getUserInfo().getUser());
                                                                         }
                                                                     }
                                                                 }).execute();
                                                             } else {
                                                                 Snackbar.make(mainView.findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                                             }
                                                         }
                                                     }).execute();
                                         }
                                     }
                                 }

                                );
            }
        });


        view.findViewById(R.id.logOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.getInstance().logOut();
                getActivity().finish();
                getActivity().startActivity(getActivity().getIntent());
            }
        });

        AppController.getInstance().setMainActivityWhichClass(ProfileFragment.class);


        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        profilePicView.setDefaultImageResId(R.drawable.profile1);
        profilePicView.setImageUrl(AppController.getInstance().getUserInfo().getDpURL().trim().replace(" ", "%20") + "?" + System.currentTimeMillis(), AppController.getInstance().getImageLoader());
        profilePicView.setErrorImageResId(R.drawable.profile1);
    }
}









/*
new GetProfile(getActivity(), AppController.getInstance().getUserInfo().getEmail(), new onProfileLoadedListener() {
@Override
public void onProfileLoaded(String response) {
        profileLoadingDialog.dismiss();

        new ProfileDetailsParser(response);

        if (AppController.getInstance().getUserProfile().getUserMatch().size() != 0) {
        matchesList.setAdapter(new MaterialArrayAdapter(
        getActivity(),
        R.layout.material_simple_list_item_1,
        AppController.getInstance().getUserProfile().getUserMatchNameList(),
        "matches"
        ));
        matchesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
@Override
public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(getActivity(), ProductListView.class).putExtra("productIDs", AppController.getInstance().getUserProfile().getUserMatch().get(position).getAllProductsOfMatch()).putExtra("mode", "matches").putExtra("matchName", AppController.getInstance().getUserProfile().getUserMatchNameList().get(position)));
        System.out.println(AppController.getInstance().getUserProfile().getUserMatch().get(position).getAllProductsOfMatch());
        }
        });
        } else {
        emptyMatch.setVisibility(View.VISIBLE);
        matchesList.setVisibility(View.GONE);
        }
        if (AppController.getInstance().getUserProfile().getUserBought().size() != 0) {
        boughtList.setAdapter(new MaterialArrayAdapter(
        getActivity().getApplicationContext(),
        R.layout.material_simple_list_item_1,
        AppController.getInstance().getUserProfile().getUserBought(),
        "orders"
        ));
        boughtList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
@Override
public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(getActivity(), SingleProductView.class).putExtra("productID", AppController.getInstance().getUserProfile().getUserBought().get(position)));
        }
        });
        } else {
        emptyBought.setVisibility(View.VISIBLE);
        boughtList.setVisibility(View.GONE);
        }
        }
        }).execute();*/

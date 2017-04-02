package com.thelogicalcoder.viazene.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.thelogicalcoder.viazene.Activities.MainActivity;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.AsyncTasks.CheckLogin;
import com.thelogicalcoder.viazene.AsyncTasks.GetProfile;
import com.thelogicalcoder.viazene.AsyncTasks.GetUserInfo;
import com.thelogicalcoder.viazene.AsyncTasks.GetWallForUser;
import com.thelogicalcoder.viazene.Interfaces.onLoginCheckedListener;
import com.thelogicalcoder.viazene.Interfaces.onProfileLoadedListener;
import com.thelogicalcoder.viazene.Interfaces.onUserInfoLoadedListener;
import com.thelogicalcoder.viazene.Interfaces.onUserWallLoadListener;
import com.thelogicalcoder.viazene.JSONParsers.ProfileDetailsParser;
import com.thelogicalcoder.viazene.JSONParsers.UserInfoParser;
import com.thelogicalcoder.viazene.JSONParsers.UserWallParser;
import com.thelogicalcoder.viazene.R;

public class LoginFragment extends android.support.v4.app.Fragment {
    EditText emailText, passwordText;
    Button loginButton;
    ProgressWheel loginProgress;
    Boolean emailValid = false, passwordValid = false;
    CheckBox stayLoggedInCheckBox;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        emailText = (EditText) view.findViewById(R.id.emailText);
        passwordText = (EditText) view.findViewById(R.id.passwordText);
        loginButton = (Button) view.findViewById(R.id.loginButton);
        loginProgress = (ProgressWheel) view.findViewById(R.id.progressBar);
        stayLoggedInCheckBox = (CheckBox) view.findViewById(R.id.stayLoggedInCheckBox);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailText.getText().toString().trim().equals("")) {
                    emailText.setError("Enter email");
                    emailText.requestFocus();
                    emailValid = false;
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText.getText().toString().trim()).matches()) {
                    emailText.requestFocus();
                    emailText.setError("Not a valid email");
                    emailValid = false;
                } else emailValid = true;

                if (passwordText.getText().toString().trim().equals("")) {
                    passwordText.setError("Enter password");
                    passwordText.requestFocus();
                    passwordValid = false;
                } else passwordValid = true;

                if (emailValid && passwordValid) {
                    loginButton.setVisibility(View.GONE);
                    stayLoggedInCheckBox.setVisibility(View.GONE);
                    loginProgress.setVisibility(View.VISIBLE);
                    new CheckLogin(getActivity(), emailText.getText().toString().trim(), passwordText.getText().toString().trim(), new onLoginCheckedListener() {
                        @Override
                        public void onLoginChecked(String response) {
                            if (response.trim().equalsIgnoreCase("success")) {
                                if (stayLoggedInCheckBox.isChecked()) {
                                    getActivity().getSharedPreferences("savedSession", Context.MODE_PRIVATE).edit().putString("email", emailText.getText().toString().trim()).putString("password", passwordText.getText().toString().trim()).commit();
                                }
                                new GetUserInfo(getActivity(), emailText.getText().toString().trim(), new onUserInfoLoadedListener() {
                                    @Override
                                    public void onUserInfoLoaded(String response) {
                                        System.out.println(response);
                                        if (response.equalsIgnoreCase("") || response.equalsIgnoreCase("errorOccurred")) {
                                            loginButton.setVisibility(View.VISIBLE);
                                            stayLoggedInCheckBox.setVisibility(View.VISIBLE);
                                            loginProgress.setVisibility(View.GONE);
                                            Snackbar.make(view.findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                        } else {
                                            new UserInfoParser(getActivity(), response);
                                            new GetWallForUser(getActivity(), new onUserWallLoadListener() {
                                                @Override
                                                public void onUserWallLoaded(String response) {
                                                    if (response.equalsIgnoreCase("") || response.equalsIgnoreCase("errorOccurred")) {
                                                        //loginButton.setVisibility(View.VISIBLE);
                                                        //loginProgress.setVisibility(View.GONE);
                                                        Snackbar.make(view.findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                                    } else {
                                                        new UserWallParser(response);
                                                        //loginProgress.setVisibility(View.GONE);

                                                        new GetProfile(getActivity(), AppController.getInstance().getUserInfo().getEmail(), new onProfileLoadedListener() {
                                                            @Override
                                                            public void onProfileLoaded(String response) {
                                                                if (response.equalsIgnoreCase("") || response.equalsIgnoreCase("errorOccurred")) {
                                                                    loginButton.setVisibility(View.VISIBLE);
                                                                    stayLoggedInCheckBox.setVisibility(View.VISIBLE);
                                                                    loginProgress.setVisibility(View.GONE);
                                                                    Snackbar.make(view.findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                                                } else {

                                                                    new ProfileDetailsParser(response);
                                                                    loginProgress.setVisibility(View.GONE);
                                                                    AppController.getInstance().setLoggedIn(true);
                                                                    String source = "";
                                                                    try {
                                                                        source = getActivity().getIntent().getStringExtra("source");
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                    if (source.equalsIgnoreCase("mma") || source.equalsIgnoreCase("spv") || source.equalsIgnoreCase("wall") || source.equalsIgnoreCase("buyP")) {
                                                                        getActivity().onBackPressed();
                                                                    } else {
                                                                        startActivity(new Intent(getActivity(), MainActivity.class));
                                                                        getActivity().finish();
                                                                    }
                                                                }
                                                            }
                                                        }).execute();


                                                        /*AppController.getInstance().setLoggedIn(true);
                                                        String source = "";


                                                        try {
                                                            source = getActivity().getIntent().getStringExtra("source");
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                        if (source.equalsIgnoreCase("mma") || source.equalsIgnoreCase("spv")) {
                                                            getActivity().onBackPressed();
                                                        } else {
                                                            startActivity(new Intent(getActivity(), MainActivity.class));
                                                            getActivity().finish();
                                                        }*/
                                                    }
                                                }
                                            }).execute();


                                        }
                                    }
                                }).execute();
                            } else if (response.trim().equalsIgnoreCase("fail")) {
                                Snackbar.make(view.findViewById(R.id.root), "Invalid credentials", Snackbar.LENGTH_SHORT).show();
                                loginButton.setVisibility(View.VISIBLE);
                                stayLoggedInCheckBox.setVisibility(View.VISIBLE);
                                loginProgress.setVisibility(View.GONE);
                            } else {
                                loginButton.setVisibility(View.VISIBLE);
                                stayLoggedInCheckBox.setVisibility(View.VISIBLE);
                                loginProgress.setVisibility(View.GONE);
                                Snackbar.make(view.findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }).execute();
                }
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}
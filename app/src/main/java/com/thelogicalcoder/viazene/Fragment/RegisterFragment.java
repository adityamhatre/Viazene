package com.thelogicalcoder.viazene.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.thelogicalcoder.viazene.Activities.RegistrationWizard;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.AsyncTasks.CheckEmail;
import com.thelogicalcoder.viazene.Interfaces.onCheckEmailListener;
import com.thelogicalcoder.viazene.R;

public class RegisterFragment extends android.support.v4.app.Fragment {
    EditText emailText, passwordText, confirmPasswordText;
    Button registerButton;
    ProgressWheel registerProgress;
    Boolean emailValid = false, passwordValid = false, confirmPasswordValid = false;
    TextView successText;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        rootView = view;
        emailText = (EditText) view.findViewById(R.id.emailText);
        passwordText = (EditText) view.findViewById(R.id.passwordText);
        confirmPasswordText = (EditText) view.findViewById(R.id.confirmPasswordText);
        registerButton = (Button) view.findViewById(R.id.loginButton);
        registerProgress = (ProgressWheel) view.findViewById(R.id.progressBar);
        successText = (TextView) view.findViewById(R.id.successText);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailText.getText().toString().trim().equals("")) {
                    emailText.setError("Enter email");
                    emailValid = false;
                    emailText.requestFocus();
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

                if (confirmPasswordText.getText().toString().trim().equals("")) {
                    confirmPasswordText.setError("Enter password");
                    confirmPasswordText.requestFocus();
                    confirmPasswordValid = false;
                } else confirmPasswordValid = true;

                if (emailValid && passwordValid) {
                    if (passwordText.getText().toString().trim().equals(confirmPasswordText.getText().toString())) {
                        registerButton.setVisibility(View.GONE);
                        registerProgress.setVisibility(View.VISIBLE);
                        /*new Register(getActivity(), emailText.getText().toString().trim(), passwordText.getText().toString().trim(), new onRegisterListener() {
                            @Override
                            public void onRegistered(String response) {

                                if (response.trim().equalsIgnoreCase("success")) {
                                    registerProgress.setVisibility(View.GONE);
                                    Snackbar.make(view.findViewById(R.id.root), "Follow the steps to complete registration", Snackbar.LENGTH_SHORT).show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(new Intent(getActivity(), RegistrationWizard.class).putExtra("email", emailText.getText().toString().trim()));
                                        }
                                    }, 1000);
                                } else if (response.trim().equalsIgnoreCase("fail")) {
                                    Snackbar.make(view.findViewById(R.id.root), "Email already taken", Snackbar.LENGTH_SHORT).show();
                                    registerButton.setVisibility(View.VISIBLE);
                                    registerProgress.setVisibility(View.GONE);
                                } else {
                                    registerButton.setVisibility(View.VISIBLE);
                                    registerProgress.setVisibility(View.GONE);
                                }
                                firstTime = false;

                            }
                        }).execute();*/

                        new CheckEmail(getActivity(), emailText.getText().toString().trim(), new onCheckEmailListener() {
                            @Override
                            public void onEmailChecked(String response) {

                                if (response.trim().equalsIgnoreCase("success")) {
                                    registerProgress.setVisibility(View.GONE);
                                    Snackbar.make(view.findViewById(R.id.root), "Follow the steps to complete registration", Snackbar.LENGTH_SHORT).show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(new Intent(getActivity(), RegistrationWizard.class).putExtra("email", emailText.getText().toString().trim()).putExtra("password", passwordText.getText().toString().trim()));
                                        }
                                    }, 1500);
                                } else if (response.trim().equalsIgnoreCase("fail")) {
                                    Snackbar.make(view.findViewById(R.id.root), "Email already taken", Snackbar.LENGTH_SHORT).show();
                                    registerButton.setVisibility(View.VISIBLE);
                                    registerProgress.setVisibility(View.GONE);
                                } else {
                                    Snackbar.make(view.findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                    registerButton.setVisibility(View.VISIBLE);
                                    registerProgress.setVisibility(View.GONE);
                                }

                            }
                        }).execute();
                    } else {
                        Snackbar.make(view.findViewById(R.id.root), "Passwords not match", Snackbar.LENGTH_SHORT).show();
                        passwordText.setError("Check password");
                        confirmPasswordText.setError("Check password");
                    }
                }

            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    public void disableControls() {
        registerProgress.setVisibility(View.GONE);
        emailText.setEnabled(false);
        passwordText.setEnabled(false);
        confirmPasswordText.setEnabled(false);
        successText.setVisibility(View.VISIBLE);
    }

    public void enableControls() {
        emailText.setEnabled(true);
        passwordText.setEnabled(true);
        confirmPasswordText.setEnabled(true);
        successText.setVisibility(View.GONE);
        registerButton.setVisibility(View.VISIBLE);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppController.getInstance().isRegistrationComplete()) {
            disableControls();
            AppController.getInstance().setRegistrationComplete(false);
        } else enableControls();
        /*if (AppController.getInstance().isRegistrationComplete()) {
            disableControls();
            AppController.getInstance().setRegistrationComplete(false);
        } else {
            if (!firstTime) {
                Snackbar.make(rootView.findViewById(R.id.root), "Registration did not complete", Snackbar.LENGTH_LONG).show();
                new DeleteComment(getActivity(), emailText.getText().toString(), new onCommentDeleteListener() {
                    @Override
                    public void onCommentDeleted(String response) {
                        if (response.trim().equalsIgnoreCase("success")) {
                            Snackbar.make(rootView.findViewById(R.id.root), "EMAIL FREED", Snackbar.LENGTH_SHORT).show();
                            registerButton.setVisibility(View.VISIBLE);
                            registerProgress.setVisibility(View.GONE);
                            enableControls();
                        }
                    }
                }).execute();
            }
        }*/
    }


}
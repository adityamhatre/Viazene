package com.thelogicalcoder.viazene.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.thelogicalcoder.viazene.R;

public class ContactFragment extends android.support.v4.app.Fragment {
    EditText phoneNumberText, addressText1, addressText2, addressText3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        phoneNumberText = (EditText) view.findViewById(R.id.phoneNumberText);
        addressText1 = (EditText) view.findViewById(R.id.addressText1);
        addressText2 = (EditText) view.findViewById(R.id.addressText2);
        addressText3 = (EditText) view.findViewById(R.id.addressText3);

        super.onViewCreated(view, savedInstanceState);
    }

    public String getPhoneNumber() {
        return phoneNumberText.getText().toString();
    }

    public void setErrorForPhone(String errorMessage) {
        phoneNumberText.setError(errorMessage);
    }

    public String getAddress1() {
        return addressText1.getText().toString();
    }

    public String getAddress2() {
        return addressText2.getText().toString();
    }

    public String getAddress3() {
        return addressText3.getText().toString();
    }

    public void setErrorForAddress1(String errorMessage) {
        addressText1.setError(errorMessage);
    }

    public void setErrorForAddress2(String errorMessage) {
        addressText2.setError(errorMessage);
    }

    public void setErrorForAddress3(String errorMessage) {
        addressText3.setError(errorMessage);
    }

    public void setFocusForPhone() {
        phoneNumberText.requestFocus();
    }

    public void setFocusForAddress1() {
        addressText1.requestFocus();
    }

    public void setFocusForAddress2() {
        addressText2.requestFocus();
    }

    public void setFocusForAddress3() {
        addressText3.requestFocus();
    }
}
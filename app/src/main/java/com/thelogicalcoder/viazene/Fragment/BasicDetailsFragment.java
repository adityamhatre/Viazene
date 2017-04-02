package com.thelogicalcoder.viazene.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.thelogicalcoder.viazene.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import babushkatext.BabushkaText;

public class BasicDetailsFragment extends android.support.v4.app.Fragment {
    BabushkaText dobTextView;
    EditText pinCode;
    public Boolean dobSet = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_basic_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        dobTextView = (BabushkaText) view.findViewById(R.id.birthDate);
        pinCode = (EditText) view.findViewById(R.id.pincode);
        view.findViewById(R.id.openCalender).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                @SuppressWarnings("unused")
                                String date = "You picked the following date: " + dayOfMonth + "/" + (++monthOfYear) + "/" + year;
                                dobTextView.reset();
                                if (dayOfMonth == 1) {
                                    dobTextView.addPiece(new BabushkaText.Piece.Builder("" + dayOfMonth).textSize(80).build());
                                    dobTextView.addPiece(new BabushkaText.Piece.Builder("st").superscript().textSize(40).build());
                                } else if (dayOfMonth == 2) {
                                    dobTextView.addPiece(new BabushkaText.Piece.Builder("" + dayOfMonth).textSize(80).build());
                                    dobTextView.addPiece(new BabushkaText.Piece.Builder("nd").superscript().textSize(40).build());
                                } else if (dayOfMonth == 3) {
                                    dobTextView.addPiece(new BabushkaText.Piece.Builder("" + dayOfMonth).textSize(80).build());
                                    dobTextView.addPiece(new BabushkaText.Piece.Builder("rd").superscript().textSize(40).build());
                                } else {
                                    dobTextView.addPiece(new BabushkaText.Piece.Builder("" + dayOfMonth).textSize(80).build());
                                    dobTextView.addPiece(new BabushkaText.Piece.Builder("th").superscript().textSize(40).build());
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
                                dobTextView.addPiece(new BabushkaText.Piece.Builder(dobMonthText).textSize(80).build());
                                dobTextView.addPiece(new BabushkaText.Piece.Builder("" + year).textSize(80).build());
                                dobTextView.display();
                                dobTextView.setVisibility(View.VISIBLE);
                            }
                        },
                        now.get(Calendar.YEAR) - 20,
                        Calendar.JANUARY,
                        1
                );
                dpd.setFirstDayOfWeek(1);
                dpd.show(getActivity().getFragmentManager(), "DATE");
                dobSet = true;
            }
        });


        super.onViewCreated(view, savedInstanceState);
    }

    public String getDOB() {
        return dobTextView.getText().toString().trim();
    }

    public String getPinCode() {
        return pinCode.getText().toString().trim();
    }

    public void setPinCodeError(String error) {
        pinCode.setError(error);
    }
}

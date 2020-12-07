package com.dialog.dialoggo.utils.helpers;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CustomTextviewWatcher implements TextWatcher {

    private final TextView textInputLayout;
    private final EditText editText;


    public CustomTextviewWatcher(TextView textInputLayout, EditText etPhoneNo) {
        this.textInputLayout = textInputLayout;
        this.editText = etPhoneNo;
    }

//    public static synchronized CustomTextWatcher getInstance(TextInputLayout textInputLayout)
//
//    {
//        if (customTextWatcher == null) {
//            customTextWatcher = new CustomTextWatcher(textInputLayout);
//        }
//
//        return customTextWatcher;
//    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (s.length() > 0) {
            textInputLayout.setVisibility(View.GONE);
            editText.getBackground().clearColorFilter();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 0) {
            textInputLayout.setVisibility(View.GONE);
            editText.getBackground().clearColorFilter();
        }
    }
//
//    @Override
//    protected Object clone() throws CloneNotSupportedException {
////        return super.clone();
//
//        throw new CloneNotSupportedException();
//    }

}


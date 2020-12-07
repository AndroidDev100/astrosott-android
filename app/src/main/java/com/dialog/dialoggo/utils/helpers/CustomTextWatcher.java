package com.dialog.dialoggo.utils.helpers;

import com.google.android.material.textfield.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;

public class CustomTextWatcher implements TextWatcher/*, Cloneable */ {

//    private static CustomTextWatcher customTextWatcher;

    private final TextInputLayout textInputLayout;


    public CustomTextWatcher(TextInputLayout textInputLayout) {
        this.textInputLayout = textInputLayout;
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
            textInputLayout.setErrorEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
//
//    @Override
//    protected Object clone() throws CloneNotSupportedException {
////        return super.clone();
//
//        throw new CloneNotSupportedException();
//    }

}

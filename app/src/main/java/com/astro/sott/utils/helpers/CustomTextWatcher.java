package com.astro.sott.utils.helpers;

import com.astro.sott.callBacks.TextWatcherCallBack;
import com.google.android.material.textfield.TextInputLayout;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

public class CustomTextWatcher implements TextWatcher {

    private final TextWatcherCallBack textWatcherCallBack;

    public CustomTextWatcher(Context applicationContext, TextWatcherCallBack callBack) {
        textWatcherCallBack = callBack;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        textWatcherCallBack.beforeTextChanged(charSequence, i, i1, i2);
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        textWatcherCallBack.onTextChanged(charSequence, i, i1, i2);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        textWatcherCallBack.afterTextChanged(editable);
    }
}

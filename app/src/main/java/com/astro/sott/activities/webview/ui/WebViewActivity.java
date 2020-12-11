package com.astro.sott.activities.webview.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;

import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.WebviewBinding;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.NetworkConnectivity;

public class WebViewActivity extends BaseBindingActivity<WebviewBinding> {


    private String strWebview = "";

    @Override
    public WebviewBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return WebviewBinding.inflate(inflater);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        connectionObserver();

    }


    private void connectionObserver() {

        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
            getBinding().noConnectionLayout.setVisibility(View.GONE);

            if (getIntent().hasExtra(AppLevelConstants.WEBVIEW)) {
                strWebview = getIntent().getStringExtra(AppLevelConstants.WEBVIEW);
            }


            setSupportActionBar(getBinding().include.toolbar);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(strWebview);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }


            WebSettings webSettings = getBinding().webview.getSettings();
            webSettings.setJavaScriptEnabled(true);


            if (!TextUtils.isEmpty(strWebview)) {

                if (strWebview.equalsIgnoreCase(AppLevelConstants.TNC)) {
                    getBinding().webview.loadUrl(AppConstants.TNC_URL);

                } else if (strWebview.equalsIgnoreCase(AppLevelConstants.HELP)) {
                    getBinding().webview.loadUrl(AppConstants.HELP_URL);
                }
            }

        } else {
            noConnectionLayout();
        }


    }


    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);

        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

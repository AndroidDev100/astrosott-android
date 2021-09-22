package com.astro.sott.activities.webview.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.WebviewBinding;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.NetworkConnectivity;

public class WebViewActivity extends BaseBindingActivity<WebviewBinding> {


    private String strWebview = "";
    private String url = "";

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
            ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(this);

            if (strWebview.equalsIgnoreCase(AppLevelConstants.HELP)) {
                if (responseDmsModel != null && responseDmsModel.getParams() != null && responseDmsModel.getParams().getZendeskURL() != null && responseDmsModel.getParams().getZendeskURL().getURL() != null) {
                    url = responseDmsModel.getParams().getZendeskURL().getURL();
                }

            } else if (strWebview.equalsIgnoreCase(AppLevelConstants.TNC)) {
                if (responseDmsModel != null && responseDmsModel.getParams() != null && responseDmsModel.getParams().getTermsAndConditionsURL() != null && responseDmsModel.getParams().getTermsAndConditionsURL().getURL() != null) {
                    url = responseDmsModel.getParams().getTermsAndConditionsURL().getURL();
                }
            } else {
                if (responseDmsModel != null && responseDmsModel.getParams() != null && responseDmsModel.getParams().getPrivacyPolicyURL() != null && responseDmsModel.getParams().getPrivacyPolicyURL().getURL() != null) {
                    url = responseDmsModel.getParams().getPrivacyPolicyURL().getURL();
                }
            }

            WebSettings webSettings = getBinding().webview.getSettings();
            webSettings.setJavaScriptEnabled(true);


            if (!TextUtils.isEmpty(strWebview)) {

              /*  if (strWebview.equalsIgnoreCase(AppLevelConstants.TNC)) {
                    getBinding().webview.loadUrl(AppConstants.TNC_URL);

                } else if (strWebview.equalsIgnoreCase(AppLevelConstants.HELP)) {*/
                getBinding().webview.loadUrl(url);
               getBinding().webview.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        view.loadUrl(request.getUrl().toString());
                        return false;
                    }
                });
                /* }*/
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

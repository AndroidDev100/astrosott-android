package com.astro.sott.activities.crWebViewActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;

import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.databinding.ActivityCrWebViewBinding;
import com.astro.sott.utils.helpers.NetworkConnectivity;

public class CrWebViewActivity extends BaseBindingActivity<ActivityCrWebViewBinding> {
    private String strWebview = "";
    private String promoAssetName = "";

    @Override
    protected ActivityCrWebViewBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityCrWebViewBinding.inflate(inflater);
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
            if (getIntent().hasExtra(AppLevelConstants.PROMO_ASSET_NAME)) {
                promoAssetName = getIntent().getStringExtra(AppLevelConstants.PROMO_ASSET_NAME);
            }


            setSupportActionBar(getBinding().include.toolbar);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(promoAssetName);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }


            WebSettings webSettings = getBinding().webview.getSettings();
            webSettings.setJavaScriptEnabled(true);


            if (!TextUtils.isEmpty(strWebview)) {

                getBinding().webview.loadUrl(strWebview);

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

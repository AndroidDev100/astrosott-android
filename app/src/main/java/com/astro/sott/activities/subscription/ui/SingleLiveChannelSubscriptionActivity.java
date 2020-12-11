package com.astro.sott.activities.subscription.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.astro.sott.activities.subscription.callback.SubscriptionActivityCallBack;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivitySingleChannelSubscriptionBinding;

public class SingleLiveChannelSubscriptionActivity extends BaseBindingActivity<ActivitySingleChannelSubscriptionBinding> implements SubscriptionActivityCallBack {


    @Override
    public ActivitySingleChannelSubscriptionBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivitySingleChannelSubscriptionBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(getBinding().include.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    @Override
    public void showProgressBar(boolean show) {

    }

    @Override
    public void showToolBar(boolean show) {
        getBinding().lnToolBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setToolBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

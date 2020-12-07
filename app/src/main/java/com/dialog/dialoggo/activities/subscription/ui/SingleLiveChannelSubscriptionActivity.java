package com.dialog.dialoggo.activities.subscription.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.dialog.dialoggo.activities.subscription.callback.SubscriptionActivityCallBack;
import com.dialog.dialoggo.baseModel.BaseBindingActivity;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.databinding.ActivitySingleChannelSubscriptionBinding;
import com.dialog.dialoggo.databinding.ActivitySubscriptionBinding;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;

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

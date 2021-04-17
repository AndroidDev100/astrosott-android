package com.astro.sott.activities.subscriptionActivity.ui;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.astro.sott.activities.changePaymentMethod.ui.ChangePaymentMethodActivity;
import com.astro.sott.activities.myPlans.ui.MyPlansActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.R;
import com.astro.sott.databinding.ActivityMyPlanBinding;

public class SubscriptionAndMyPlanActivity extends BaseBindingActivity<ActivityMyPlanBinding> implements View.OnClickListener {
    @Override
    public ActivityMyPlanBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityMyPlanBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(getBinding().include.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.subscription));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getBinding().tvChangePaymentMethod.setOnClickListener(this);
        getBinding().tvMyPlan.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_change_payment_method:
                openChangePaymentMethodActivity();
                break;

            case R.id.tv_my_plan:
                openMyPlansActivity();
        }

    }

    private void openMyPlansActivity() {
        new ActivityLauncher(SubscriptionAndMyPlanActivity.this).myPlan(SubscriptionAndMyPlanActivity.this, MyPlansActivity.class);
    }

    private void openChangePaymentMethodActivity() {
     new ActivityLauncher(SubscriptionAndMyPlanActivity.this).changePaymentMethod(SubscriptionAndMyPlanActivity.this, ChangePaymentMethodActivity.class);
    }
}

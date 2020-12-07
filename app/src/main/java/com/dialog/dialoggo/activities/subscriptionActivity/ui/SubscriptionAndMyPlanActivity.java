package com.dialog.dialoggo.activities.subscriptionActivity.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.changePaymentMethod.ui.ChangePaymentMethodActivity;
import com.dialog.dialoggo.activities.myPlans.ui.MyPlansActivity;
import com.dialog.dialoggo.baseModel.BaseBindingActivity;
import com.dialog.dialoggo.databinding.AccountSettingsBinding;
import com.dialog.dialoggo.databinding.ActivityMyPlanBinding;
import com.dialog.dialoggo.utils.helpers.ActivityLauncher;

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

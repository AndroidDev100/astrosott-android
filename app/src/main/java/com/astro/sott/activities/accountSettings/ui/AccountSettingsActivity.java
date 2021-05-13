package com.astro.sott.activities.accountSettings.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.astro.sott.activities.dtvActivity.UI.dtvActivity;
import com.astro.sott.activities.parentalControl.ui.ParentalControl;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.R;
import com.astro.sott.activities.mbbaccount.ui.MBBAccountActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.AccountSettingsBinding;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;

import static com.astro.sott.R.id.tv_dtv_account;
import static com.astro.sott.R.id.tv_parental_control;
import static com.astro.sott.R.id.tv_subscription;

public class AccountSettingsActivity extends BaseBindingActivity<AccountSettingsBinding> implements View.OnClickListener {
    @Override
    public AccountSettingsBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return AccountSettingsBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(getBinding().include.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.account_settings));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (!TextUtils.isEmpty(KsPreferenceKey.getInstance(this).getUser().getUsername())) {

            if (KsPreferenceKey.getInstance(this).getUser().getUsername().length() > 9 &&
                    KsPreferenceKey.getInstance(this).getUser().getUsername().startsWith("94")
            ) {
                String phnNumber = KsPreferenceKey.getInstance(this).getUser().getUsername().substring(2, KsPreferenceKey.getInstance(this).getUser().getUsername().length());
                getBinding().tvPhoneNo.setText("0" + phnNumber);
            } else {
                getBinding().tvPhoneNo.setText(KsPreferenceKey.getInstance(this).getUser().getUsername());

            }
        }
        getBinding().tvParentalControl.setOnClickListener(this);
        getBinding().tvDtvAccount.setOnClickListener(this);
        getBinding().tvSubscription.setOnClickListener(this);
       // getBinding().tvMbbAccount.setOnClickListener(this);
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

    public void parentalControlActivity() {
        Intent intent = new Intent(this, ParentalControl.class);
        startActivity(intent);
    }

    private void dtvAccountActivity() {

        new ActivityLauncher(AccountSettingsActivity.this).dtvActivity(AccountSettingsActivity.this, dtvActivity.class);
    }

    private void mbbAccountActivity() {
        new ActivityLauncher(AccountSettingsActivity.this).mbbActivity(AccountSettingsActivity.this, MBBAccountActivity.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case tv_parental_control:
                parentalControlActivity();
                break;

            //Performing click for DTV Account
            case tv_dtv_account:
                dtvAccountActivity();
                break;

            case tv_subscription:
//                openSubscriptionActivity();
                break;

//            case R.id.tv_mbb_account:
//                mbbAccountActivity();
//                break;
        }
    }

//    private void openSubscriptionActivity() {
//        new ActivityLauncher(AccountSettingsActivity.this).planActivity(AccountSettingsActivity.this, SubscriptionAndMyPlanActivity.class);
//    }


}

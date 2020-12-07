package com.dialog.dialoggo.activities.accountSettings.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.dtvActivity.UI.dtvActivity;
import com.dialog.dialoggo.activities.mbbaccount.ui.MBBAccountActivity;
import com.dialog.dialoggo.activities.parentalControl.ui.ParentalControl;
import com.dialog.dialoggo.activities.subscriptionActivity.ui.SubscriptionAndMyPlanActivity;
import com.dialog.dialoggo.baseModel.BaseBindingActivity;
import com.dialog.dialoggo.databinding.AccountSettingsBinding;
import com.dialog.dialoggo.utils.helpers.ActivityLauncher;
import com.dialog.dialoggo.utils.ksPreferenceKey.KsPreferenceKey;

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
            case R.id.tv_parental_control:
                parentalControlActivity();
                break;

            //Performing click for DTV Account
            case R.id.tv_dtv_account:
                dtvAccountActivity();
                break;

            case R.id.tv_subscription:
                openSubscriptionActivity();
                break;

//            case R.id.tv_mbb_account:
//                mbbAccountActivity();
//                break;
        }
    }

    private void openSubscriptionActivity() {
        new ActivityLauncher(AccountSettingsActivity.this).planActivity(AccountSettingsActivity.this, SubscriptionAndMyPlanActivity.class);
    }


}

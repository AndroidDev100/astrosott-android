package com.astro.sott.activities.parentalControl.ui;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.astro.sott.activities.parentalControl.viewmodels.ParentalControlViewModel;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.R;
import com.astro.sott.activities.loginActivity.LoginActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.callBacks.commonCallBacks.ParentalDialogCallbacks;
import com.astro.sott.databinding.ActivityParentalControlBinding;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.DialogHelper;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;

public class ParentalControl extends BaseBindingActivity<ActivityParentalControlBinding> implements View.OnClickListener {

    private ParentalControlViewModel parentalViewModel;

    @Override
    protected ActivityParentalControlBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityParentalControlBinding.inflate(inflater);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(getBinding().include.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.parental_control));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        getBinding().tvChangePin.setOnClickListener(this);

        callViewModel();
        connectionObserver();
    }

    private void callViewModel() {
        parentalViewModel = ViewModelProviders.of(this).get(ParentalControlViewModel.class);

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
            UIinitialization();
        } else {

        }
    }

    private void UIinitialization() {
        changePinVisibility();
        setParentalRadio();

        handleParentalSwitch();

        getBinding().tvViewRestriction.setOnClickListener(view -> {
            new ActivityLauncher(ParentalControl.this).loginActivity(ParentalControl.this, LoginActivity.class, 0,AppLevelConstants.ACTICITY_NAME);
           // new ActivityLauncher(ParentalControl.this).viewRestrictionActivity(ParentalControl.this, ViewingRestrictionActivity.class);
        });

    }

    private void changePinVisibility() {
        if (KsPreferenceKey.getInstance(this).getParentalActive()){
            getBinding().tvChangePin.setVisibility(View.VISIBLE);
        }else {
            getBinding().tvChangePin.setVisibility(View.GONE);
        }
    }

    private void setParentalRadio() {

        if (KsPreferenceKey.getInstance(this).getParentalActive()){
            getBinding().switchPush.setChecked(true);
        }else {
            getBinding().switchPush.setChecked(false);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_change_pin:
                if (NetworkConnectivity.isOnline(getApplication())) {
                    changePinFragment();
                } else {
                    Toast.makeText(ParentalControl.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void handleParentalSwitch() {


        getBinding().switchPush.setOnCheckedChangeListener((compoundButton, b) -> {
            if(!compoundButton.isPressed()) {
                return;
            }
            new ActivityLauncher(ParentalControl.this).loginActivity(ParentalControl.this, LoginActivity.class, 0,AppLevelConstants.PARENTAL_SWITCH);
        });
    }

    private void changePinFragment() {
        DialogHelper.showValidatePinDialog(this, true, "PARENTAL", new ParentalDialogCallbacks() {
            @Override
            public void onPositiveClick(String pinText) {
                if (NetworkConnectivity.isOnline(getApplication())) {
                    parentalViewModel.validatePin(ParentalControl.this, pinText).observe(ParentalControl.this, commonResponse -> {
                        if (commonResponse.getStatus()) {
                            DialogHelper.hideValidatePinDialog();
                            showSetPinDialog();
                        } else {
                            Toast.makeText(ParentalControl.this, getString(R.string.incorrect_parental_pin), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(ParentalControl.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onNegativeClick() {
                DialogHelper.hideValidatePinDialog();
            }
        });
    }

    private void showSetPinDialog() {
        DialogHelper.showValidatePinDialog(this, false, "PARENTAL", new ParentalDialogCallbacks() {
            @Override
            public void onPositiveClick(String pinText) {
                parentalViewModel.setPin(ParentalControl.this, pinText).observe(ParentalControl.this, new Observer<CommonResponse>() {
                    @Override
                    public void onChanged(@Nullable CommonResponse commonResponse) {
                        if (NetworkConnectivity.isOnline(getApplication())) {
                            if (commonResponse.getStatus()) {
                                Toast.makeText(ParentalControl.this, getString(R.string.parental_pin_updated), Toast.LENGTH_LONG).show();
                                DialogHelper.hideValidatePinDialog();
                            } else {
                                Toast.makeText(ParentalControl.this, getString(R.string.incorrect_parental_pin), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(ParentalControl.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }

            @Override
            public void onNegativeClick() {
                DialogHelper.hideValidatePinDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setParentalRadio();
        changePinVisibility();
    }

}

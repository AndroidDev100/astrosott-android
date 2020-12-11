package com.astro.sott.activities.notificationSetting.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.fragments.dialog.AlertDialogSingleButtonFragment;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.R;
import com.astro.sott.databinding.ActivityNotificationSettingBinding;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.helpers.NetworkConnectivity;


public class NotificationSettingActivity extends BaseBindingActivity<ActivityNotificationSettingBinding> implements AlertDialogSingleButtonFragment.AlertDialogListener {


    private KsServices ksServices;

    @Override
    public ActivityNotificationSettingBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityNotificationSettingBinding.inflate(inflater);


    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ksServices = new KsServices(this);
        connectionObserver();


        setSupportActionBar(getBinding().include.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.notification_settings));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
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
            getBinding().connection.rlConnection.setVisibility(View.GONE);

            // modelCall();
            UIinitialization();


        } else {
            noConnectionLayout();
        }


    }

    private void UIinitialization() {

        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        if (KsPreferenceKey.getInstance(this).getUserActive()) {
            ksServices.getNotificationSetting((status, response) -> runOnUiThread(() -> {

                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                if (status) {

                    if (response.isNotification()) {
                        getBinding().switchPush.setChecked(true);

                    } else {
                        getBinding().switchPush.setChecked(false);

                    }
                } else {
                    showDialog(response.getMessage());

                }
            }));
        } else {
            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
            getBinding().switchPush.setEnabled(false);
        }

        getBinding().switchPush.setOnCheckedChangeListener((compoundButton, b) ->{

            getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
            ksServices.getNotificationSettingUpdate(getBinding().switchPush.isChecked(), (status, message) -> runOnUiThread(() -> {

                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                if (status) {

                    if (getBinding().switchPush.isChecked()) {
                        getBinding().switchPush.setChecked(true);
                    } else {
                        getBinding().switchPush.setChecked(false);

                    }

                } else {

                    if (getBinding().switchPush.isChecked()) {
                        getBinding().switchPush.setChecked(false);
                    } else {
                        getBinding().switchPush.setChecked(true);

                    }
                    showDialog(message);
                }
            }));

        });
    }


    private void noConnectionLayout() {
        getBinding().connection.rlConnection.setVisibility(View.VISIBLE);

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

    private void showDialog(String message) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(getResources().getString(R.string.dialog), message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }


    @Override
    public void onFinishDialog() {
    }
}

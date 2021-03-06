package com.astro.sott.activities.mbbaccount.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.activities.liveChannel.ui.LiveChannel;
import com.astro.sott.activities.mbbaccount.viewmodel.MBBViewModel;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.R;
import com.astro.sott.activities.mbbaccount.adapter.MBBAccountAdapter;
import com.astro.sott.activities.mbbaccount.listener.MBBItemClickListner;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivityMbbaccountBinding;
import com.astro.sott.fragments.dialog.AlertDialogSingleButtonFragment;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.ToastHandler;

public class MBBAccountActivity extends BaseBindingActivity<ActivityMbbaccountBinding> implements MBBItemClickListner, AlertDialogSingleButtonFragment.AlertDialogListener {

    private static final String TAG = "MBBAccountActivity";
    private MBBAccountAdapter mbbAccountAdapter;
    private MBBViewModel viewModel;
    private String mbbAccountNumber = "";
    private boolean isAlertDialogShowing = false;

    @Override
    public ActivityMbbaccountBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityMbbaccountBinding.inflate(inflater);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connectionObserver();
        setSupportActionBar(getBinding().include.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.mbb_account));
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
            getBinding().noConnectionLayout.setVisibility(View.GONE);
            modelCall();
            UIinitialization();
            loadDataFromModel();
            getBinding().plusImage.setOnClickListener(view -> new ActivityLauncher(MBBAccountActivity.this).addMBBAccountActivity(MBBAccountActivity.this, AddMBBAccountActivity.class, AppLevelConstants.DTVFRAGMENT,mbbAccountNumber));


        } else {
            noConnectionLayout();
        }
    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);

        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }


    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(MBBViewModel.class);
    }

    private void UIinitialization() {
        getBinding().recyclerview.hasFixedSize();
        getBinding().recyclerview.setNestedScrollingEnabled(false);
        getBinding().recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

    }

    //Fetching MBBAccount Details
    private void loadDataFromModel() {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        if (this.getApplicationContext() != null && NetworkConnectivity.isOnline(MBBAccountActivity.this)) {
            viewModel.getMBBAccountList().observe(this, new Observer<String>() {
                @Override
                public void onChanged(@Nullable String mbbAccount) {
                    if (mbbAccount != null) {
                        PrintLogging.printLog("",mbbAccount);
                        if(mbbAccount.equalsIgnoreCase("0")){
                            getBinding().recyclerview.setVisibility(View.GONE);
                            getBinding().addDtvAccount.setVisibility(View.VISIBLE);
                            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                        }else if(mbbAccount.equalsIgnoreCase("")){
                            getBinding().recyclerview.setVisibility(View.GONE);
                            getBinding().addDtvAccount.setVisibility(View.VISIBLE);
                            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                        }
                        else {

                            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                            getBinding().recyclerview.setVisibility(View.VISIBLE);
                            getBinding().addDtvAccount.setVisibility(View.GONE);
                            PrintLogging.printLog("",mbbAccount);
                            mbbAccountAdapter = new MBBAccountAdapter(MBBAccountActivity.this, MBBAccountActivity.this, mbbAccount);
                            getBinding().recyclerview.setAdapter(mbbAccountAdapter);
                        }

                    }
                    else {
                        getBinding().recyclerview.setVisibility(View.GONE);
                        getBinding().addDtvAccount.setVisibility(View.GONE);
                        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                        showDialog(getString(R.string.something_went_wrong_try_again));
                    }
                }
            });
        }else {
            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
            ToastHandler.show(getString(R.string.no_internet_connection),
                    MBBAccountActivity.this);
        }
    }

    private void showDialog(String message) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(getResources().getString(R.string.dialog), message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(MBBAccountActivity.this);
        if(!isAlertDialogShowing) {
            isAlertDialogShowing = true;
            alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
        }
    }

    @Override
    public void onFinishDialog() {

    }

    @Override
    public void onClick(String type, String accountNumber) {
        this.mbbAccountNumber = accountNumber;
        if(type.equalsIgnoreCase("Change")){
            new ActivityLauncher(MBBAccountActivity.this).addMBBAccountActivity(MBBAccountActivity.this, AddMBBAccountActivity.class, AppLevelConstants.CHANGEFRAGMENT,mbbAccountNumber);
        } else {
            new ActivityLauncher(MBBAccountActivity.this).addMBBAccountActivity(MBBAccountActivity.this, AddMBBAccountActivity.class, AppLevelConstants.DELETEFRAGMENT,mbbAccountNumber);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isAlertDialogShowing = false;
        connectionObserver();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}

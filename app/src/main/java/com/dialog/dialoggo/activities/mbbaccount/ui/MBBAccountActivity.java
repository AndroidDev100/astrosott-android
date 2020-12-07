package com.dialog.dialoggo.activities.mbbaccount.ui;

import android.os.Bundle;
import android.util.Log;
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

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.addDTVAccountNumber.UI.addDTVAccountNumberActivity;
import com.dialog.dialoggo.activities.dtvActivity.UI.dtvActivity;
import com.dialog.dialoggo.activities.dtvActivity.adapter.DTVAdapter;
import com.dialog.dialoggo.activities.mbbaccount.adapter.MBBAccountAdapter;
import com.dialog.dialoggo.activities.mbbaccount.listener.MBBItemClickListner;
import com.dialog.dialoggo.activities.mbbaccount.viewmodel.MBBViewModel;
import com.dialog.dialoggo.baseModel.BaseBindingActivity;
import com.dialog.dialoggo.databinding.ActivityMbbaccountBinding;
import com.dialog.dialoggo.fragments.dialog.AlertDialogSingleButtonFragment;
import com.dialog.dialoggo.utils.helpers.ActivityLauncher;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.helpers.NetworkConnectivity;
import com.dialog.dialoggo.utils.helpers.PrintLogging;

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
            Toast.makeText(getApplicationContext(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
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

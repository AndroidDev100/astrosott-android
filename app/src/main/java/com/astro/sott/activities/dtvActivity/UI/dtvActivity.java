package com.astro.sott.activities.dtvActivity.UI;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.NonNull;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.astro.sott.activities.mbbaccount.ui.MBBAccountActivity;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.R;
import com.astro.sott.activities.addDTVAccountNumber.UI.addDTVAccountNumberActivity;
import com.astro.sott.activities.dtvActivity.adapter.DTVAdapter;
import com.astro.sott.activities.dtvActivity.viewModel.dtvViewModel;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.callBacks.DTVItemClickListner;
import com.astro.sott.databinding.ActivityDtvBinding;
import com.astro.sott.fragments.dialog.AlertDialogSingleButtonFragment;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.ToastHandler;

public class dtvActivity extends BaseBindingActivity<ActivityDtvBinding> implements DTVItemClickListner, AlertDialogSingleButtonFragment.AlertDialogListener {
    private DTVAdapter dtvAdapter;
    private dtvViewModel viewModel;
    private String dtvAccountNumber = "";
    private boolean isAlertDialogShowing = false;

    @Override
    public ActivityDtvBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityDtvBinding.inflate(inflater);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionObserver();
      //  new ToolBarHandler(this).setAction(getBinding().toolbarLayout);

        setSupportActionBar(getBinding().include.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.dtv_account));
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
            getBinding().plusImage.setOnClickListener(view -> new ActivityLauncher(dtvActivity.this).addDtvActivity(dtvActivity.this, addDTVAccountNumberActivity.class, AppLevelConstants.DTVFRAGMENT,dtvAccountNumber));


        } else {
            noConnectionLayout();
        }


    }

    //Fetching DtvAccount Details
    private void loadDataFromModel() {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        if (this.getApplicationContext() != null && NetworkConnectivity.isOnline(dtvActivity.this)) {
            viewModel.getDtvAccountList().observe(this, new Observer<String>() {
                @Override
                public void onChanged(@Nullable String dtvAccount) {
                    if (dtvAccount != null) {
                        if(dtvAccount.equalsIgnoreCase("0")){
                            getBinding().recyclerview.setVisibility(View.GONE);
                            getBinding().addDtvAccount.setVisibility(View.VISIBLE);
                            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                        }else if(dtvAccount.equalsIgnoreCase("")){
                            getBinding().recyclerview.setVisibility(View.GONE);
                            getBinding().addDtvAccount.setVisibility(View.VISIBLE);
                            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                        }
                        else {

                            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                            getBinding().recyclerview.setVisibility(View.VISIBLE);
                            getBinding().addDtvAccount.setVisibility(View.GONE);
                            dtvAdapter = new DTVAdapter(dtvActivity.this, dtvActivity.this, dtvAccount);
                            getBinding().recyclerview.setAdapter(dtvAdapter);
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
                    dtvActivity.this);
        }



    }

    private void showDialog(String message) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(getResources().getString(R.string.dialog), message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        if(!isAlertDialogShowing) {
            isAlertDialogShowing = true;
            alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
        }
    }

    private void UIinitialization() {
        getBinding().recyclerview.hasFixedSize();
        getBinding().recyclerview.setNestedScrollingEnabled(false);
        getBinding().recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

    }

    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(dtvViewModel.class);
    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);

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

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("onstartCalledDTV","true");
    }

    @Override
    protected void onResume() {
        super.onResume();
        isAlertDialogShowing = false;
        connectionObserver();
    }

    @Override
    public void onClick(String type, String accountNumber) {
        this.dtvAccountNumber = accountNumber;
        if(type.equalsIgnoreCase("Change")){

            new ActivityLauncher(dtvActivity.this).addDtvActivity(dtvActivity.this, addDTVAccountNumberActivity.class, AppLevelConstants.CHANGEFRAGMENT,dtvAccountNumber);
        } else {
            new ActivityLauncher(dtvActivity.this).addDtvActivity(dtvActivity.this, addDTVAccountNumberActivity.class, AppLevelConstants.DELETEFRAGMENT,dtvAccountNumber);
        }
    }

    @Override
    public void onFinishDialog() {

    }
}

package com.astro.sott.activities.changePaymentMethod.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.astro.sott.activities.changePaymentMethod.viewmodel.ChangePaymentMethodViewModel;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.callBacks.commonCallBacks.ChangePaymentMethodCallBack;
import com.astro.sott.R;
import com.astro.sott.activities.changePaymentMethod.adapter.ChangePaymentMethodAdapter;
import com.astro.sott.databinding.ActivityChangePaymentMethodBinding;
import com.astro.sott.fragments.dialog.AlertDialogSingleButtonFragment;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.kaltura.client.types.HouseholdPaymentMethod;

import java.util.List;

public class ChangePaymentMethodActivity extends BaseBindingActivity<ActivityChangePaymentMethodBinding> implements AlertDialogSingleButtonFragment.AlertDialogListener, ChangePaymentMethodCallBack {

    private ChangePaymentMethodAdapter adapter;
    private ChangePaymentMethodViewModel viewModel;
    private String externalIdToCompare = "";
    private int paymentMethodId;


    @Override
    public ActivityChangePaymentMethodBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityChangePaymentMethodBinding.inflate(inflater);


    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connectionObserver();
        setSupportActionBar(getBinding().include.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.payment_method_));
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

    private void connectionValidation(boolean aBoolean) {
        if (aBoolean) {
            getBinding().noConnectionLayout.setVisibility(View.GONE);
            modelCall();
            callChangePaymentMethodListApi();
//            setClick();


        } else {
            noConnectionLayout();
        }
    }

    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(ChangePaymentMethodViewModel.class);
    }

    private void callChangePaymentMethodListApi() {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        viewModel.getHouseholdPaymentMethodList().observe(this, paymentMethodList -> {
            if(paymentMethodList != null){
                if(paymentMethodList.size() > 0){
                    UIInitialization();
                    loadDataFromModel(paymentMethodList);
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                }else {
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    showDialog(getString(R.string.payment_method_not_found));
                }
            }else {
                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                showDialog(getString(R.string.something_went_wrong_try_again));
            }
        });

    }

    private void loadDataFromModel(List<HouseholdPaymentMethod> paymentMethodList) {
        adapter = new ChangePaymentMethodAdapter(ChangePaymentMethodActivity.this,paymentMethodList, ChangePaymentMethodActivity.this);
        getBinding().recyclerview.setAdapter(adapter);
    }


    private void UIInitialization() {
        getBinding().recyclerview.hasFixedSize();
        getBinding().recyclerview.setNestedScrollingEnabled(false);
        getBinding().recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

    }

//    private void loadDataFromModel() {
//        adapter = new ChangePaymentMethodAdapter(ChangePaymentMethodActivity.this);
//        getBinding().recyclerview.setAdapter(adapter);
//    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }

    private void showDialog(String message) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance("", message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        if (fm != null)
            alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
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
    public void onFinishDialog() {
        finish();
    }

    @Override
    public void callBack(int id, String externalId) {
//        paymentMethodId = id;
//        externalIdToCompare = externalId;
//        if (NetworkConnectivity.isOnline(ChangePaymentMethodActivity.this)) {
//            callRemovePaymentApi(id);
//        }else {
//            showDialog(getString(R.string.no_internet_connection));
//        }

    }

    private void callRemovePaymentApi(int id) {
//        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
//        viewModel.callRemoveApi(id).observe(this,changePaymentMethodModel -> {
//            if (changePaymentMethodModel!=null){
//                if (changePaymentMethodModel.isStatus()){
//                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
//                    showDialog(getString(R.string.payment_method_removed));
//                }else {
//                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
//                    if(changePaymentMethodModel.getErrorCode().equalsIgnoreCase("3041")){
//                        new ActivityLauncher(ChangePaymentMethodActivity.this).updatePaymentMethod(ChangePaymentMethodActivity.this, UpdatePaymentMethod.class,externalIdToCompare,paymentMethodId);
//
//                    }else {
//                        showDialog(changePaymentMethodModel.getMessage());
//                    }
//                }
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  connectionObserver();
    }
}

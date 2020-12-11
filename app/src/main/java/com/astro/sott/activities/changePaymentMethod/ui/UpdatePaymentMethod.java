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

import com.astro.sott.activities.changePaymentMethod.adapter.UpdatePaymentMethodAdapter;
import com.astro.sott.activities.changePaymentMethod.viewmodel.ChangePaymentMethodViewModel;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.callBacks.commonCallBacks.UpdateCallBack;
import com.astro.sott.fragments.dialog.AlertDialogSingleButtonFragment;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.R;
import com.astro.sott.databinding.ActivityUpdatePaymentMethodBinding;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.kaltura.client.types.Entitlement;
import com.kaltura.client.types.HouseholdPaymentMethod;
import com.kaltura.client.types.SubscriptionEntitlement;

import java.util.List;

public class UpdatePaymentMethod extends BaseBindingActivity<ActivityUpdatePaymentMethodBinding> implements AlertDialogSingleButtonFragment.AlertDialogListener, UpdateCallBack {

    private UpdatePaymentMethodAdapter adapter;
    private ChangePaymentMethodViewModel viewModel;
    private String externalIdToCompare = "";
    private int paymentMethodId;
    private int count = 0;
    private int size;


    @Override
    public ActivityUpdatePaymentMethodBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityUpdatePaymentMethodBinding.inflate(inflater);


    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connectionObserver();
        setSupportActionBar(getBinding().include.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.change_payment_method));
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
            UIInitialization();
            getIntentValue();
            callChangePaymentMethodListApi();
//            setClick();


        } else {
            noConnectionLayout();
        }
    }

    private void getIntentValue() {
        if (getIntent().getExtras() != null) {
            externalIdToCompare = getIntent().getStringExtra("ExternalId");
            paymentMethodId = getIntent().getIntExtra("paymentMethodId",0);
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
                    for (int i = 0; i<paymentMethodList.size(); i++){
                        if (paymentMethodList.get(i).getExternalId().equalsIgnoreCase(externalIdToCompare)){
                            paymentMethodList.remove(i);
                        }
                    }
                    if (paymentMethodList.size()>0) {
                        loadDataFromModel(paymentMethodList);
                        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    }else {
                        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                        showDialog(getString(R.string.payment_method_not_found));
                    }
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
        adapter = new UpdatePaymentMethodAdapter(UpdatePaymentMethod.this,paymentMethodList, UpdatePaymentMethod.this);
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
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(getResources().getString(R.string.dialog), message, getResources().getString(R.string.ok));
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
    public void callBack(int newPaymentMethodId, String externalId) {
        if (NetworkConnectivity.isOnline(UpdatePaymentMethod.this)) {
            callEntitlemeListApi(newPaymentMethodId);
        }else {
            showDialog(getString(R.string.no_internet_connection));
        }

    }

    private void callEntitlemeListApi(int newPaymentMethodId) {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        viewModel.getEntitlementList().observe(this, entitlements -> {

            try {
                if(entitlements != null){
                    if(entitlements.size() > 0){
                        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                        callPaymentMethodUpdateApi(newPaymentMethodId,entitlements);
                    }else {
                        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                        showDialog(getString(R.string.no_data_found));
                    }
                }else {
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    showDialog(getString(R.string.something_went_wrong_try_again));
                }
            } catch (Exception exc) {
                PrintLogging.printLog("MyPlansActivity", exc.getMessage());
            }
        });
    }

    private void callPaymentMethodUpdateApi(int newPaymentMethodId, List<Entitlement> entitlements) {
        size = entitlements.size();
        for (int i = 0; i<entitlements.size(); i++) {
            count++;
            if (((SubscriptionEntitlement) entitlements.get(i)).getPaymentMethodId() != null) {
                int id = ((SubscriptionEntitlement) entitlements.get(i)).getId();
                getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
                viewModel.updatePaymentMethod(id, newPaymentMethodId).observe(this, updatePaymentMethodModel -> {
                    if (updatePaymentMethodModel != null) {
                        if (updatePaymentMethodModel.isStatus()) {
                            if (count == size) {
                                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                                callRemovePaymentApi(paymentMethodId);
                            }

                        } else {
                            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                            showDialog(updatePaymentMethodModel.getMessage());
                        }
                    } else {
                        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                        showDialog(getString(R.string.something_went_wrong_try_again));
                    }
                });
            }
        }
    }

    private void callRemovePaymentApi(int id) {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        viewModel.callRemoveApi(id).observe(this,changePaymentMethodModel -> {
            if (changePaymentMethodModel!=null){
                if (changePaymentMethodModel.isStatus()){
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    showDialog(getString(R.string.payment_method_update));
                }else {
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    showDialog(changePaymentMethodModel.getMessage());
//                    if(changePaymentMethodModel.getErrorCode().equalsIgnoreCase("3041")){
//
//                    }else {
//                        showDialog(changePaymentMethodModel.getMessage());
//                    }
                }
            }
        });
    }
}


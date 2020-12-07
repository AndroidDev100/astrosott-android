package com.dialog.dialoggo.activities.SelectAccount.UI;

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
import com.dialog.dialoggo.activities.SelectAccount.SelectAccountModel.BillingAccountTypeModel;
import com.dialog.dialoggo.activities.SelectAccount.SelectAccountModel.DetailListItem;
import com.dialog.dialoggo.activities.SelectAccount.SelectAccountModel.DtvMbbHbbModel;
import com.dialog.dialoggo.activities.SelectAccount.SelectAccountModel.Response;
import com.dialog.dialoggo.activities.SelectAccount.adapter.SelectAccountAdapter;
import com.dialog.dialoggo.activities.SelectAccount.viewModel.SelectDtvViewModel;
import com.dialog.dialoggo.activities.home.HomeActivity;
import com.dialog.dialoggo.baseModel.BaseBindingActivity;
import com.dialog.dialoggo.callBacks.DTVItemClickListner;
import com.dialog.dialoggo.databinding.ActivitySelectDtvAccountBinding;
import com.dialog.dialoggo.fragments.dialog.AlertDialogFragment;
import com.dialog.dialoggo.fragments.dialog.AlertDialogSingleButtonFragment;
import com.dialog.dialoggo.fragments.dialog.LoginAlertDialogSingleButtonFragment;
import com.dialog.dialoggo.utils.helpers.ActivityLauncher;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.helpers.NetworkConnectivity;
import com.dialog.dialoggo.utils.helpers.PrintLogging;
import com.dialog.dialoggo.utils.helpers.ToastHandler;
import com.dialog.dialoggo.utils.ksPreferenceKey.KsPreferenceKey;
import com.google.gson.Gson;
import com.kaltura.client.types.InboxMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SelectDtvAccountActivity extends BaseBindingActivity<ActivitySelectDtvAccountBinding> implements AlertDialogFragment.AlertDialogListener, DTVItemClickListner, AlertDialogSingleButtonFragment.AlertDialogListener {
    private SelectDtvViewModel viewModel;
    private SelectAccountAdapter adapter;
    private List<InboxMessage> list;
    private int position;
    private String dtvAccountNumber = "";
    private List<BillingAccountTypeModel> detailListItems;
    private List<DetailListItem> dtvList;
    private String phoneNumber = "";
    private String message = "";
    private List<String> headerList;
    private List<DtvMbbHbbModel> dtvMbbHbbModels;
    private LoginAlertDialogSingleButtonFragment alertDialog;

    @Override
    public ActivitySelectDtvAccountBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivitySelectDtvAccountBinding.inflate(inflater);


    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailListItems = new ArrayList<>();
        dtvMbbHbbModels = new ArrayList<>();
        if (getIntent() != null) {
            phoneNumber = getIntent().getStringExtra("PhoneNumber");

        }

        connectionObserver();
//        new ToolBarHandler(this).setNotificationAction(getBinding().toolbar);


        setSupportActionBar(getBinding().include.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.select_your_account));
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
            setClick();


        } else {
            noConnectionLayout();
        }


    }

    private void setClick() {
        getBinding().change.setOnClickListener(view -> {

            //addDtvAccount(dtvAccountNumber);
            hitAllApiInSequence();
        });
    }

    private void hitAllApiInSequence() {
        message = getString(R.string.success_register_user);
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        if (getApplicationContext() != null && NetworkConnectivity.isOnline(SelectDtvAccountActivity.this)) {

            viewModel.updateDtvMbbHbbAccount(dtvMbbHbbModels).observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    if (s != null) {
                        // getDtvAccountDetails(getBinding().etPhoneNo.getText().toString());
                        KsPreferenceKey.getInstance(getApplicationContext()).setUserActive(true);
                        KsPreferenceKey.getInstance(getApplicationContext()).setParentalActive(true);
                        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                        new ActivityLauncher(SelectDtvAccountActivity.this).homeScreen(SelectDtvAccountActivity.this, HomeActivity.class);
                        ToastHandler.show(message, getApplicationContext());

                    } else {
                        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                        showDialog(getResources().getString(R.string.something_went_wrong_try_again));

                        //showDialog(getResources().getString(R.string.something_went_wrong));
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
        alertDialog.setAlertDialogCallBack(this);
        if (fm != null)
            alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

    private void loadDataFromModel() {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        viewModel.getConnectionDetails(phoneNumber).observe(this, new Observer<Response>() {
            @Override
            public void onChanged(@Nullable Response response) {

                if (response!=null && response.getData()!=null && response.getData().getDetailList() != null) {
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    getBinding().recyclerview.setVisibility(View.VISIBLE);
                    getBinding().nodataLayout.setVisibility(View.GONE);
                    setUIComponent(response.getData().getDetailList());

                } else {
                    message = getString(R.string.success_register_user);
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    getBinding().recyclerview.setVisibility(View.GONE);
                    getBinding().nodataLayout.setVisibility(View.GONE);
                    KsPreferenceKey.getInstance(getApplicationContext()).setUserActive(true);
                    KsPreferenceKey.getInstance(getApplicationContext()).setParentalActive(true);
                    new ActivityLauncher(SelectDtvAccountActivity.this).homeScreen(SelectDtvAccountActivity.this, HomeActivity.class);
                    ToastHandler.show(message, getApplicationContext());
                }
            }
        });


    }

    private void setUIComponent(List<DetailListItem> data) {
        try {
            headerList = new ArrayList<>();
            for (int headerLoop = 0; headerLoop < data.size(); headerLoop++) {
                if(data.get(headerLoop).getLob()!=null) {
//|| data.get(headerLoop).getLob().equalsIgnoreCase(AppLevelConstants.VOLTE)
                    if (data.get(headerLoop).getLob().equalsIgnoreCase(AppLevelConstants.DTV) || data.get(headerLoop).getLob().equalsIgnoreCase(AppLevelConstants.MBB)) {

                        if (headerList.isEmpty()) {
                            headerList.add(data.get(headerLoop).getLob());
                        } else {
                            if (!headerList.contains(data.get(headerLoop).getLob())) {
                                headerList.add(data.get(headerLoop).getLob());
                            }
                        }
                    }
                }

            }


            if (headerList.size() > 0) {
                for (int j = 0; j < headerList.size(); j++) {
                    BillingAccountTypeModel listItem = new BillingAccountTypeModel();
                    dtvList = new ArrayList<>();
                    for (int i = 0; i < data.size(); i++) {
                        DetailListItem detailListItem = new DetailListItem();
                       // String lob = data.get(i).getLob().trim();
                        if(data.get(i).getLob()!=null) {
                            if (data.get(i).getLob().equalsIgnoreCase(headerList.get(j))) {
                                detailListItem.setLob(data.get(i).getLob());
                                detailListItem.setRefAccount(data.get(i).getRefAccount());
                                dtvList.add(detailListItem);
                            }
                        }
                    }
                    listItem.setViewType(headerList.get(j));
                    listItem.setDetailListItems(dtvList);
                    detailListItems.add(listItem);
                }
            }

            setHeaderType(detailListItems);

           // PrintLogging.printLog("", detailListItems + "");
            Log.d("ArrayValueIs",new Gson().toJson(detailListItems));




            if (detailListItems.size() > 0) {
                sortArrayToPopulateData();
                fillModelToCallApi();
                getBinding().change.setVisibility(View.VISIBLE);
                adapter = new SelectAccountAdapter(SelectDtvAccountActivity.this, detailListItems, SelectDtvAccountActivity.this);
                getBinding().recyclerview.setAdapter(adapter);
            } else {
                message = getString(R.string.success_register_user);
                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                getBinding().recyclerview.setVisibility(View.GONE);
                getBinding().nodataLayout.setVisibility(View.GONE);
                KsPreferenceKey.getInstance(getApplicationContext()).setUserActive(true);
                KsPreferenceKey.getInstance(getApplicationContext()).setParentalActive(true);
                new ActivityLauncher(SelectDtvAccountActivity.this).homeScreen(SelectDtvAccountActivity.this, HomeActivity.class);
                ToastHandler.show(message, getApplicationContext());

            }
        }catch (Exception e){
            Log.d("ExceptionIs",e.toString());
        }


    }

    private void setHeaderType(List<BillingAccountTypeModel> detailListItems) {
        if(detailListItems.size()>0) {
            for (int j = 0; j < detailListItems.size(); j++) {

//                if (detailListItems.get(j).getViewType().equalsIgnoreCase(AppLevelConstants.VOLTE)) {
//                    detailListItems.get(j).setViewType(AppLevelConstants.BROAD_BAND_HBB_ACCOUNT);
//
//                } else

                if (detailListItems.get(j).getViewType().equalsIgnoreCase(AppLevelConstants.MBB)) {
                    detailListItems.get(j).setViewType(AppLevelConstants.MOBILE_ACCOUNT);

                } else if (detailListItems.get(j).getViewType().equalsIgnoreCase(AppLevelConstants.DTV)) {
                    detailListItems.get(j).setViewType(AppLevelConstants.DIALOG_TV);

                }
            }
        }

    }

    private void sortArrayToPopulateData() {
        Collections.sort(detailListItems, new Comparator<BillingAccountTypeModel>() {
            @Override
            public int compare(BillingAccountTypeModel dtvMbbHbbModel, BillingAccountTypeModel t1) {
                return dtvMbbHbbModel.getViewType().compareTo(t1.getViewType());
            }
        });
        Log.d("ArrayValueIs",new Gson().toJson(detailListItems));
    }

    private void fillModelToCallApi() {
        int size = detailListItems.size();
        for (int i = 0; i < size; i++) {


//            if (detailListItems.get(i).getViewType().equalsIgnoreCase(AppLevelConstants.VOLTE)) {
//
//                commonModel.setKey(AppLevelConstants.HBB);
//                commonModel.setValue(detailListItems.get(i).getDetailListItems().get(0).getRefAccount());
//                commonModel.setDescription(AppLevelConstants.HBB_ACCOUNT_DESCRIPTION);
//            } else

            DtvMbbHbbModel commonModel = new DtvMbbHbbModel();
            if (detailListItems.get(i).getViewType().equalsIgnoreCase(AppLevelConstants.MBB)) {

                commonModel.setKey(detailListItems.get(i).getDetailListItems().get(0).getLob());
                commonModel.setValue(detailListItems.get(i).getDetailListItems().get(0).getRefAccount());
                commonModel.setDescription(AppLevelConstants.MBB_ACCOUNT_DESCRIPTION);
            } else {

                commonModel.setKey(detailListItems.get(i).getDetailListItems().get(0).getLob());
                commonModel.setValue(detailListItems.get(i).getDetailListItems().get(0).getRefAccount());
                commonModel.setDescription(AppLevelConstants.DTV_ACCOUNT_DESCRIPTION);
            }
            dtvMbbHbbModels.add(commonModel);
        }


    }


    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(SelectDtvViewModel.class);
    }

    private void UIinitialization() {

        getBinding().recyclerview.hasFixedSize();
        getBinding().recyclerview.setNestedScrollingEnabled(false);
        getBinding().recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));


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

    private void showAlertDialog(String msg) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogFragment alertDialog = AlertDialogFragment.newInstance(getResources().getString(R.string.dialog), msg, getResources().getString(R.string.yes), getResources().getString(R.string.no));
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }


    @Override
    public void onFinishDialog() {

    }


    private void addDtvAccount(String account) {
        message = getString(R.string.success_register_user);
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        if (getApplicationContext() != null && NetworkConnectivity.isOnline(SelectDtvAccountActivity.this)) {

            viewModel.updateDTVAccountData(account).observe(this, new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {
                    if (s != null) {
                        // getDtvAccountDetails(getBinding().etPhoneNo.getText().toString());
                        KsPreferenceKey.getInstance(getApplicationContext()).setUserActive(true);
                        KsPreferenceKey.getInstance(getApplicationContext()).setParentalActive(true);
                        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                        new ActivityLauncher(SelectDtvAccountActivity.this).homeScreen(SelectDtvAccountActivity.this, HomeActivity.class);
                        ToastHandler.show(message, getApplicationContext());

                    } else {

                        //TODO Add Popup for Error

                        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }
            });

        } else {
            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();

        }
    }


    @Override
    public void onClick(String key, String value) {
        updateModelOnClick(key, value);
    }

    private void updateModelOnClick(String key, String value) {
        if (dtvMbbHbbModels.size() > 0) {
            int arraySize = dtvMbbHbbModels.size();

            for (int i = 0; i < arraySize; i++) {
                if (dtvMbbHbbModels.get(i).getKey().equalsIgnoreCase(key)) {
                    dtvMbbHbbModels.get(i).setValue(value);
                    break;
                }
            }
        }

    }

}



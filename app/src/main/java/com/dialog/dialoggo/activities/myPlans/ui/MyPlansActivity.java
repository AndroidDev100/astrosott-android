package com.dialog.dialoggo.activities.myPlans.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.myPlans.adapter.MyPlanAdapter;
import com.dialog.dialoggo.activities.myPlans.models.SubscriptionPlanPackageModel;
import com.dialog.dialoggo.activities.myPlans.viewmodel.MySubscriptionViewModel;
import com.dialog.dialoggo.activities.subscription.callback.BottomSheetFragmentListener;
import com.dialog.dialoggo.activities.subscription.fragment.ViewAllBottomSheetFragment;
import com.dialog.dialoggo.activities.subscription.manager.AllChannelManager;
import com.dialog.dialoggo.baseModel.BaseBindingActivity;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonBean;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.beanModel.subscriptionmodel.SubscriptionModel;
import com.dialog.dialoggo.databinding.ActivityPlansBinding;
import com.dialog.dialoggo.fragments.dialog.AlertDialogSingleButtonFragment;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.helpers.NetworkConnectivity;
import com.dialog.dialoggo.utils.helpers.PrintLogging;
import com.google.gson.Gson;
import com.kaltura.client.types.Entitlement;
import com.kaltura.client.types.Subscription;
import com.kaltura.client.types.SubscriptionEntitlement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyPlansActivity extends BaseBindingActivity<ActivityPlansBinding> implements MyPlanAdapter.PlanAdapterListener , AlertDialogSingleButtonFragment.AlertDialogListener{

    private MyPlanAdapter adapter;
    private List<SubscriptionPlanPackageModel> subscriptionPlanPackageModelList = new ArrayList<>();
    private MySubscriptionViewModel viewModel;
    private String productId;
    private Long renewalDate;
    private boolean isRenewableForPurchase;
    private boolean isSheetOpen = false;
    private List<RailCommonData> mAllChannelList = new ArrayList<>();
    private List<SubscriptionModel> mSubscriptionPlanModelList = new ArrayList<>();
    private String channelId = "";
    private int count = 0;
    private int size;
    ViewAllBottomSheetFragment viewAllBottomSheetFragment;

    @Override
    public ActivityPlansBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityPlansBinding.inflate(inflater);


    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connectionObserver();
        setSupportActionBar(getBinding().include.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.my_plans));
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
            callEntitlementListApi();
//            callMySubscriptionListApi();

//            setClick();


        } else {
            noConnectionLayout();
        }
    }

    private void loadDataFromModel() {
        Collections.sort(mSubscriptionPlanModelList, new Comparator<SubscriptionModel>() {
            @Override
            public int compare(SubscriptionModel dtvMbbHbbModel, SubscriptionModel t1) {
                return dtvMbbHbbModel.getSubscription().getPrice().getPrice().getAmount().compareTo(t1.getSubscription().getPrice().getPrice().getAmount());
            }
        });

//        for (int i = 0; i<mSubscriptionPlanModelList.size(); i++){
//            if (mSubscriptionPlanModelList.get(i).getSubscription().getPrice().getPrice().getAmount().toString().equalsIgnoreCase("0.0") || mSubscriptionPlanModelList.get(i).getSubscription().getPrice().getPrice().getAmount().toString().equalsIgnoreCase("0")){
//                mSubscriptionPlanModelList.remove(mSubscriptionPlanModelList.get(i));
//            }
//        }
//        if (mSubscriptionPlanModelList.size()>0) {
            adapter = new MyPlanAdapter(mSubscriptionPlanModelList, this, getApplicationContext());
            getBinding().recyclerview.setAdapter(adapter);
//        }
//        else {
//            if (count == size){
//                if (mSubscriptionPlanModelList.size()>0)
//                    showDialog(getString(R.string.no_active_plan));
//            }
//
//        }
    }

    private void UIInitialization() {
        getBinding().recyclerview.hasFixedSize();
        getBinding().recyclerview.setNestedScrollingEnabled(false);
        getBinding().recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }

    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(MySubscriptionViewModel.class);
    }

    private void callEntitlementListApi() {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        viewModel.getEntitlementList().observe(this, entitlements -> {

            try {
                if(entitlements != null){
                    if(entitlements.size() > 0){
                        size = entitlements.size();

                        for (int i =0; i<entitlements.size(); i++){
                            productId = entitlements.get(i).getProductId();
                            isRenewableForPurchase = ((SubscriptionEntitlement) entitlements.get(i)).getIsRenewableForPurchase();

                            if(isRenewableForPurchase) {
                                renewalDate = ((SubscriptionEntitlement) entitlements.get(i)).getNextRenewalDate();
                            }else {
                                renewalDate = ((SubscriptionEntitlement) entitlements.get(i)).getEndDate();
                            }


                            callMySubscriptionListApi(productId,renewalDate,isRenewableForPurchase);
                        }
                      //  callMySubscriptionListApi(entitlements);




                    }else {
                        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                        showDialog(getString(R.string.no_active_plan));
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


    private void callMySubscriptionListApi(String productId, Long renewalDate, boolean isRenewableForPurchase) {

        viewModel.getMySubscriptionPackageList(productId).observe(this, subscriptionList -> {
            count++;
                if (subscriptionList != null) {
                    if (subscriptionList.size() > 0) {
                        if (channelId.equalsIgnoreCase("")) {
                            try {
                                channelId = String.valueOf(subscriptionList.get(0).getChannels().get(0).getId());
                            }catch (Exception e){

                            }

                        }
                        for (Subscription subscription : subscriptionList) {
                            if (subscription.getPrice().getPrice().getAmount().toString().equalsIgnoreCase("0.0") || subscription.getPrice().getPrice().getAmount().toString().equalsIgnoreCase("0")) {

                            }else {
                                mSubscriptionPlanModelList.add(new SubscriptionModel(false, subscription, renewalDate, isRenewableForPurchase));
                            }
                        }

                        if (count == size) {
                            UIInitialization();
                            if (mSubscriptionPlanModelList.size()>0) {
                                loadDataFromModel();
                            }else {
                                showDialog(getString(R.string.no_active_plan));
                            }
                            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);

                        }

                    } else {
                        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                       // showDialog(getString(R.string.no_active_plan));
                    }
                } else {
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    showDialog(getString(R.string.something_went_wrong_try_again));
                }
            });
//        }
    }


    private void getAllLiveChannels(int counter) {
        viewModel.getAllChannelList(channelId,counter).observe(this, assetCommonBeans -> {
            if(assetCommonBeans != null && assetCommonBeans.size() > 0) {
                setUiComponent(assetCommonBeans);
            } else {
                isSheetOpen = false;
                if(AllChannelManager.getInstance().getDataUpdateListener() != null) {
                    AllChannelManager.getInstance().getDataUpdateListener().noDataFound();
                }
                if (counter==1){
                    viewAllBottomSheetFragment.openDialougeFornoData(MyPlansActivity.this);
                }
            }
        });
    }


    private void setUiComponent(List<AssetCommonBean> assetCommonBeans) {
        if(!isSheetOpen) {
            mAllChannelList.addAll(assetCommonBeans.get(0).getRailAssetList());
            AllChannelManager.getInstance().setRailCommonDataList(mAllChannelList);
            openBottomSheet();
        } else {
            AllChannelManager.getInstance().getDataUpdateListener().addDataToChannelList(assetCommonBeans.get(0).getRailAssetList());
        }
    }

    private void openBottomSheet() {
         viewAllBottomSheetFragment = new ViewAllBottomSheetFragment("My Plans");
        viewAllBottomSheetFragment.setBottomSheetFragmentListener(bottomSheetFragmentListener);
        viewAllBottomSheetFragment.show(getSupportFragmentManager(),viewAllBottomSheetFragment.getTag());
    }


    private BottomSheetFragmentListener bottomSheetFragmentListener = new BottomSheetFragmentListener() {
        @Override
        public void onSheetClosed() {
            isSheetOpen = false;
            AllChannelManager.getInstance().clearTempAllChannelsData();
        }

        @Override
        public void loadMoreChannel(int counter) {
            if (NetworkConnectivity.isOnline(MyPlansActivity.this)) {
                getAllLiveChannels(counter);
            } else {
                showDialog(getString(R.string.no_internet_connection));
            }
        }
    };


    @Override
    public void openBottomSheet(boolean open) {
        if(open){
            if (!isSheetOpen) {
                isSheetOpen = true;
                if (NetworkConnectivity.isOnline(this)) {
                    getAllLiveChannels(1);
                    openBottomSheet();
                } else {
                    showDialog(getString(R.string.no_internet_connection));
                }
            }
        }

    }

    @Override
    public void cancelSubscription(boolean close, String id) {

        if(close){
            showDialogcustom(id);

        }

    }

    private void callCancelSubscriptionApi(String id) {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        viewModel.callCancelSubscription(id).observe(this, commonResponse -> {
            if(commonResponse.getStatus()){
                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                showCanceSubscriptionSuccessDialog();
               // finish();
            }else {
                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
               showDialog(commonResponse.getMessage());
               // Toast.makeText(this,commonResponse.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCanceSubscriptionSuccessDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.cancel_success_subscription, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialog.setCancelable(false);
        deleteDialog.show();
        deleteDialogView.findViewById(R.id.continue_Text).setOnClickListener(view -> {
            deleteDialog.dismiss();
            finish();
        });
    }

    private void showDialogcustom(String id) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.cancel_subscription, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialog.setCancelable(false);
        deleteDialog.show();
        deleteDialogView.findViewById(R.id.change).setOnClickListener(view -> {
            deleteDialog.dismiss();
            callCancelSubscriptionApi(id);
        });
        deleteDialog.findViewById(R.id.remove).setOnClickListener(view -> {
            deleteDialog.dismiss();
        });
    }


//    private void prepareData(List<Subscription> subscriptionList) {
//        subscriptionPlanPackageModelList.add(new SubscriptionPlanPackageModel(SubscriptionPlanPackageModel.SUBSCRIPTION_TITLE_TYPE,getResources().getString(R.string.your_subscription_plan),"","","","","","","",""));
//        subscriptionPlanPackageModelList.add(new SubscriptionPlanPackageModel(SubscriptionPlanPackageModel.SUBSCRIPTION_DESC_TYPE,"",subscriptionList.get(0).getName(),subscriptionList.get(0).getPrice().getPrice().getCurrency(),String.valueOf(subscriptionList.get(0).getPrice().getPrice().getAmount()),"","","","",""));
//        subscriptionPlanPackageModelList.add(new SubscriptionPlanPackageModel(SubscriptionPlanPackageModel.SUBSCRIPTION_TITLE_TYPE,getResources().getString(R.string.added_channel),"","","","","","","",""));
//        subscriptionPlanPackageModelList.add(new SubscriptionPlanPackageModel(SubscriptionPlanPackageModel.SUBSCRIPTION_CHANNEL_TYPE,"","","","","","","",getString(R.string.utv),getString(R.string.lkr_per_month)));
//        subscriptionPlanPackageModelList.add(new SubscriptionPlanPackageModel(SubscriptionPlanPackageModel.SUBSCRIPTION_CHANNEL_TYPE,"","","","","","","",getString(R.string.ada),getString(R.string.lkr_per_month)));
//    }

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
        FragmentManager fm = (MyPlansActivity.this).getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(getResources().getString(R.string.dialog), message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        if (fm != null)
            alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

    @Override
    public void onFinishDialog() {
        finish();
    }


}
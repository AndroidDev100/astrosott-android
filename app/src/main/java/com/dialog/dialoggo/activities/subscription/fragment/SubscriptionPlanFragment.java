package com.dialog.dialoggo.activities.subscription.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.SelectAccount.SelectAccountModel.DtvMbbHbbModel;
import com.dialog.dialoggo.activities.subscription.adapter.PlanAdapter;
import com.dialog.dialoggo.activities.subscription.callback.BottomSheetFragmentListener;
import com.dialog.dialoggo.activities.subscription.callback.SubscriptionActivityCallBack;
import com.dialog.dialoggo.activities.subscription.manager.AllChannelManager;
import com.dialog.dialoggo.activities.subscription.manager.PaymentItemDetail;
import com.dialog.dialoggo.activities.subscription.model.PlanModel;
import com.dialog.dialoggo.activities.subscription.viewmodel.SubscriptionViewModel;
import com.dialog.dialoggo.baseModel.BaseBindingFragment;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonBean;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.beanModel.subscriptionmodel.SubscriptionModel;
import com.dialog.dialoggo.databinding.FragmentSubscriptionPlanBinding;
import com.dialog.dialoggo.fragments.dialog.AlertDialogSingleButtonFragment;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.helpers.AssetContent;
import com.dialog.dialoggo.utils.helpers.NetworkConnectivity;
import com.dialog.dialoggo.utils.helpers.ToastHandler;
import com.dialog.dialoggo.utils.ksPreferenceKey.KsPreferenceKey;
import com.google.gson.Gson;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.DoubleValue;
import com.kaltura.client.types.Subscription;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class SubscriptionPlanFragment extends BaseBindingFragment<FragmentSubscriptionPlanBinding> implements PlanAdapter.PlanAdapterListener, AlertDialogSingleButtonFragment.AlertDialogListener {

    private static final String TAG = "SubscriptionPlanFragmen";
    private boolean isSheetOpen = false;
    private SubscriptionViewModel viewModel;
    private SubscriptionActivityCallBack mListener;
    private List<RailCommonData> mAllChannelList = new ArrayList<>();
    private List<SubscriptionModel> mSubscriptionPlanModelList = new ArrayList<>();
    private String id = "";
    private Long channelId;
    ViewAllBottomSheetFragment viewAllBottomSheetFragment;
    private String baseId = "";

    public SubscriptionPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public FragmentSubscriptionPlanBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentSubscriptionPlanBinding.inflate(inflater);
    }


    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof SubscriptionActivityCallBack) {
            mListener = (SubscriptionActivityCallBack) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SubscriptionActivityCallBack");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListener.setToolBarTitle(getString(R.string.subscription_plan));
        mListener.showToolBar(true);
        id = AllChannelManager.getInstance().getChannelId();
        modelCall();
        checknetworkConnectivity();

        setClickListeners();
    }

    private void checknetworkConnectivity() {
        if(getActivity() == null){
            return;
        }
        if (NetworkConnectivity.isOnline(getActivity())) {
            if (KsPreferenceKey.getInstance(getActivity()).getSubscriptionOffer()!=null) {
                callAssetListApiForSubscription(KsPreferenceKey.getInstance(getActivity()).getSubscriptionOffer());
            }

        } else {
            showDialog(getString(R.string.no_internet_connection));
        }
    }

    private void callAssetListApiForSubscription(String subscriptionOffer) {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        viewModel.getAssetList(subscriptionOffer).observe(this, new Observer<List<Asset>>() {
            @Override
            public void onChanged(List<Asset> assets) {
                if (assets!=null){
                    if (assets.size()>0){
                        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                        AssetContent.saveIdAndReasonCode(assets);
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < assets.size(); i++) {

                            if (AssetContent.getBaseId(assets.get(i).getMetas())!="")
                            stringBuilder.append(AssetContent.getBaseId(assets.get(i).getMetas())).append(",");

                        }
                        if (stringBuilder.length() > 0) {
                            baseId = stringBuilder.toString();
                            baseId = baseId.substring(0, baseId.length() - 1);
                        }

                        callSubscriptionPackageListApi(baseId);

                    }else {
                        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                        showDialog(getString(R.string.something_went_wrong_try_again));
                    }

                }else {
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    showDialog(getString(R.string.something_went_wrong_try_again));
                }
            }
        });
    }

    private void callSubscriptionPackageListApi(String baseId) {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        viewModel.getSubscriptionPackageList(baseId).observe(this, subscriptionList -> {

            if(subscriptionList != null){
                if(subscriptionList.size() > 0){
                    Collections.sort(subscriptionList, new Comparator<Subscription>() {
                        @Override
                        public int compare(Subscription dtvMbbHbbModel, Subscription t1) {
                            return dtvMbbHbbModel.getPrice().getPrice().getAmount().compareTo(t1.getPrice().getPrice().getAmount());
                        }
                    });

                    for(int i = 0; i<subscriptionList.size(); i++) {
                        if (i == 0){
                            try {
                                channelId = subscriptionList.get(i).getChannels().get(i).getId();
                            }catch (Exception e){

                            }
                            mSubscriptionPlanModelList.add(new SubscriptionModel(true, subscriptionList.get(i),0L,false));
                        } else {
                            mSubscriptionPlanModelList.add(new SubscriptionModel(false, subscriptionList.get(i),0L,false));
                        }
                    }


                    PlanAdapter planAdapter = new PlanAdapter(mSubscriptionPlanModelList, this);
//                    Log.e(TAG, "callSubscriptionPackageListApi: " +mSubscriptionPlanModelList.size());
                    getBinding().packageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    getBinding().packageRecyclerView.setAdapter(planAdapter);
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                }else {
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    showDialog(getString(R.string.something_went_wrong_try_again));
                }
            }else {
                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                showDialog(getString(R.string.something_went_wrong_try_again));
            }
        });
    }

    private void showDialog(String message) {
        FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(getResources().getString(R.string.dialog), message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        if (fm != null)
            alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

    private void setClickListeners() {
        getBinding().btnContinue.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_subscriptionPlanFragment_to_billPaymentFragment,null));
    }

    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(SubscriptionViewModel.class);
    }

    private void getAllLiveChannels(int counter) {

        if(getActivity() == null && mSubscriptionPlanModelList==null && mSubscriptionPlanModelList.size()==0){
            return;
        }
        if (NetworkConnectivity.isOnline(getActivity())) {

            viewModel.getAllChannelList(String.valueOf(channelId), counter).observe(this, assetCommonBeans -> {
                if (assetCommonBeans != null && assetCommonBeans.size() > 0) {
                    setUiComponent(assetCommonBeans);
                } else {
                    isSheetOpen = false;
                    if (AllChannelManager.getInstance().getDataUpdateListener() != null) {
                        AllChannelManager.getInstance().getDataUpdateListener().noDataFound();
                    }
                    if (counter==1){
                        viewAllBottomSheetFragment.openDialougeFornoData(getActivity());
                    }

                   // showDialog(getResources().getString(R.string.something_went_wrong));
                }
            });
        }else {
            showDialog(getString(R.string.no_internet_connection));
        }
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
         viewAllBottomSheetFragment = new ViewAllBottomSheetFragment("VOD");
        viewAllBottomSheetFragment.setBottomSheetFragmentListener(bottomSheetFragmentListener);
        viewAllBottomSheetFragment.show(getChildFragmentManager(),viewAllBottomSheetFragment.getTag());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mSubscriptionPlanModelList !=  null) {
            mSubscriptionPlanModelList.clear();
        }
    }


    private BottomSheetFragmentListener bottomSheetFragmentListener = new BottomSheetFragmentListener() {
        @Override
        public void onSheetClosed() {
            isSheetOpen = false;
            AllChannelManager.getInstance().clearTempAllChannelsData();
        }

        @Override
        public void loadMoreChannel(int counter) {
            if(getActivity() == null){
                return;
            }
            if (NetworkConnectivity.isOnline(getActivity())) {
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
                if(getActivity() == null){
                    return;
                }
                if (NetworkConnectivity.isOnline(getActivity())) {
                    getAllLiveChannels(1);
                    openBottomSheet();
                } else {
                    showDialog(getString(R.string.no_internet_connection));
                }

            }
        }

    }

    @Override
    public void onFinishDialog() {
        if(getActivity() == null){
            return;
        }
        getActivity().finish();
    }


}
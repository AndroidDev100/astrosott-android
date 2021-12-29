package com.astro.sott.activities.subscription.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.astro.sott.activities.subscription.adapter.LiveChannelPlanAdapter;
import com.astro.sott.activities.subscription.callback.SubscriptionActivityCallBack;
import com.astro.sott.activities.subscription.manager.AllChannelManager;
import com.astro.sott.beanModel.subscriptionmodel.SubscriptionModel;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.R;
import com.astro.sott.activities.subscription.callback.BottomSheetFragmentListener;
import com.astro.sott.activities.subscription.viewmodel.SubscriptionViewModel;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.databinding.FragmentLiveChannelSubcriptionBinding;
import com.astro.sott.fragments.dialog.AlertDialogSingleButtonFragment;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.StringBuilderHolder;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.Subscription;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class LiveChannelSubcriptionFragment extends BaseBindingFragment<FragmentLiveChannelSubcriptionBinding> implements LiveChannelPlanAdapter.PlanAdapterListener, AlertDialogSingleButtonFragment.AlertDialogListener{

    private static final String TAG = "LiveChannelSubcriptionF";
    private SubscriptionActivityCallBack mListener;
    private SubscriptionViewModel viewModel;
    private RailCommonData railCommonData;
    private Map<String, MultilingualStringValueArray> map;
    private boolean isDataLoaded = false;
    private List<Subscription> subscriptionList = new ArrayList<>();
    private boolean isSheetOpen = false;
    private List<RailCommonData> mAllChannelList = new ArrayList<>();
    private List<SubscriptionModel> mSubscriptionPlanModelList = new ArrayList<>();
    private Asset asset;
    private String fileId = "";
    private Long id;
    ViewAllBottomSheetFragment viewAllBottomSheetFragment;
    private String baseId = "";

    public LiveChannelSubcriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public FragmentLiveChannelSubcriptionBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentLiveChannelSubcriptionBinding.inflate(inflater);
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof SubscriptionActivityCallBack) {
            mListener = (SubscriptionActivityCallBack) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListener.setToolBarTitle("Subscription");
        mListener.showToolBar(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(SubscriptionViewModel.class);

        railCommonData = AllChannelManager.getInstance().getRailCommonData();
        map = railCommonData.getObject().getTags();
        fileId = AllChannelManager.getInstance().getChannelId();
        //setMetas();
        setUiComponent(mSubscriptionPlanModelList);
        checknetworkConnectivity();
    }

    private void checknetworkConnectivity() {
        if(getActivity() == null){
            return;
        }
        if (NetworkConnectivity.isOnline(getActivity())) {
            if(!isDataLoaded) {
                if (KsPreferenceKey.getInstance(getActivity()).getSubscriptionOffer()!=null) {
                    callAssetListApiForSubscription(KsPreferenceKey.getInstance(getActivity()).getSubscriptionOffer());
                }
            }
        } else {
            showDialog(getString(R.string.no_internet_connection));
        }
    }

    private void callAssetListApiForSubscription(String subscriptionOffer) {
        getBinding().progressLay.progressHeart.setVisibility(View.VISIBLE);
        viewModel.getAssetList(subscriptionOffer).observe(getViewLifecycleOwner(), new Observer<List<Asset>>() {
            @Override
            public void onChanged(List<Asset> assets) {
                if (assets!=null){
                    if (assets.size()>0){
                        getBinding().progressLay.progressHeart.setVisibility(View.GONE);
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
                        getBinding().progressLay.progressHeart.setVisibility(View.GONE);
                        showDialog(getString(R.string.something_went_wrong_try_again));
                    }

                }else {
                    getBinding().progressLay.progressHeart.setVisibility(View.GONE);
                    showDialog(getString(R.string.something_went_wrong_try_again));
                }
            }
        });
    }


    private void callSubscriptionPackageListApi(String baseId) {
        getBinding().progressLay.progressHeart.setVisibility(View.VISIBLE);
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
                                id = subscriptionList.get(i).getChannels().get(i).getId();
                            }catch (Exception e){

                            }

                            mSubscriptionPlanModelList.add(new SubscriptionModel(true, subscriptionList.get(i),0L,false));
                        } else {
                            mSubscriptionPlanModelList.add(new SubscriptionModel(false, subscriptionList.get(i),0L,false));
                        }
                    }
                    setUiComponent(mSubscriptionPlanModelList);
                    getBinding().progressLay.progressHeart.setVisibility(View.GONE);

                }else {
                    showDialog(getString(R.string.something_went_wrong_try_again));
                    getBinding().progressLay.progressHeart.setVisibility(View.GONE);
                }
            }else {
                showDialog(getString(R.string.something_went_wrong_try_again));
                getBinding().progressLay.progressHeart.setVisibility(View.GONE);
            }


        });
    }

    private void setUiComponent(List<SubscriptionModel> subscriptions) {
        getBinding().txtChannelTitle.setText(railCommonData.getObject().getName());
        getBinding().txtChannelDesc.setText(railCommonData.getObject().getDescription());
        AppCommonMethods.setImages(railCommonData, getContext(), getBinding().imvChannelLogo);

        if(subscriptions == null || subscriptions.size() == 0) {
            return;
        }

//        Collections.sort(mSubscriptionPlanModelList, new Comparator<SubscriptionModel>() {
//            @Override
//            public int compare(SubscriptionModel dtvMbbHbbModel, SubscriptionModel t1) {
//                return dtvMbbHbbModel.getSubscription().getPrice().getPrice().getAmount().compareTo(t1.getSubscription().getPrice().getPrice().getAmount());
//            }
//        });
        LiveChannelPlanAdapter planAdapter = new LiveChannelPlanAdapter(mSubscriptionPlanModelList, this);
        getBinding().packageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        getBinding().packageRecyclerView.setAdapter(planAdapter);

        isDataLoaded = true;

        getBinding().btnBuy.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_liveChannelSubcriptionFragment_to_billPaymentFragment,null));
    }

    private void setMetas() {
        viewModel.getGenreLivedata(map).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (!TextUtils.isEmpty(s)) {
                    getBinding().txtChannelGenre.setText(s);
                    if(s != null){
                        getBinding().txtChannelGenre.setVisibility(View.VISIBLE);
                    }
                    else {
                        getBinding().txtChannelGenre.setVisibility(View.GONE);
                    }
                    StringBuilderHolder.getInstance().append(s.trim());
                    StringBuilderHolder.getInstance().append(" | ");
                }
            }
        });
    }

    private void getAllLiveChannels(int counter) {
        if(getActivity() == null && mSubscriptionPlanModelList==null && mSubscriptionPlanModelList.size()==0){
            return;
        }
        if (NetworkConnectivity.isOnline(getActivity())) {
            viewModel.getAllChannelList(String.valueOf(id),counter).observe(this, assetCommonBeans -> {
                if(assetCommonBeans != null && assetCommonBeans.size() > 0) {
                    setAllChannelUiComponent(assetCommonBeans);
                } else {
                    isSheetOpen = false;
                    if(AllChannelManager.getInstance().getDataUpdateListener() != null) {
                        AllChannelManager.getInstance().getDataUpdateListener().noDataFound();
                    }
                    if (counter==1){
                        viewAllBottomSheetFragment.openDialougeFornoData(getActivity());
                    }
                   // showDialog(getResources().getString(R.string.something_went_wrong));
                }
            });
        } else {
            showDialog(getString(R.string.no_internet_connection));
        }

    }


    private void setAllChannelUiComponent(List<AssetCommonBean> assetCommonBeans) {
        if(!isSheetOpen) {
            mAllChannelList.addAll(assetCommonBeans.get(0).getRailAssetList());
            AllChannelManager.getInstance().setRailCommonDataList(mAllChannelList);
            openBottomSheet();
        } else {
            AllChannelManager.getInstance().getDataUpdateListener().addDataToChannelList(assetCommonBeans.get(0).getRailAssetList());
        }
    }

    private void openBottomSheet() {
         viewAllBottomSheetFragment = new ViewAllBottomSheetFragment("Live");
        viewAllBottomSheetFragment.setBottomSheetFragmentListener(bottomSheetFragmentListener);
        viewAllBottomSheetFragment.show(getChildFragmentManager(),viewAllBottomSheetFragment.getTag());
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
        isDataLoaded = false;
        if(mSubscriptionPlanModelList !=  null) {
            mSubscriptionPlanModelList.clear();
        }
    }

    private void showDialog(String message) {
        FragmentManager fm = getFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(getResources().getString(R.string.dialog), message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        if (fm != null)
            alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }


    @Override
    public void onFinishDialog() {
        if(getActivity() == null){
            return;
        }
        getActivity().finish();
    }

}

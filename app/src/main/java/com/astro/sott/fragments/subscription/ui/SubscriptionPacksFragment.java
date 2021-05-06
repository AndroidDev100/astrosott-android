package com.astro.sott.fragments.subscription.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astro.sott.R;
import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.activities.subscriptionActivity.ui.SubscriptionDetailActivity;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.callBacks.commonCallBacks.CardCLickedCallBack;
import com.astro.sott.databinding.FragmentSubscriptionPacksBinding;
import com.astro.sott.fragments.subscription.adapter.SubscriptionAdapter;
import com.astro.sott.fragments.subscription.vieModel.SubscriptionViewModel;
import com.astro.sott.modelClasses.InApp.PackDetail;
import com.astro.sott.networking.refreshToken.EvergentRefreshToken;
import com.astro.sott.usermanagment.modelClasses.getProducts.ProductsResponseMessageItem;
import com.astro.sott.utils.billing.SkuDetails;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.userInfo.UserInfo;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubscriptionPacksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubscriptionPacksFragment extends BaseBindingFragment<FragmentSubscriptionPacksBinding> implements CardCLickedCallBack {
    private SubscriptionViewModel subscriptionViewModel;
    private SkuDetails skuDetails;
    private List<PackDetail> packDetailList;
    private List<String> productList;
    private String[] subscriptionIds;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String from;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SubscriptionPacksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubscriptionPacksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubscriptionPacksFragment newInstance(String param1, String param2) {
        SubscriptionPacksFragment fragment = new SubscriptionPacksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            from = getArguments().getString("from");
        }

        if (getArguments().getSerializable(AppLevelConstants.SUBSCRIPTION_ID_KEY) != null)
            subscriptionIds = (String[]) getArguments().getSerializable(AppLevelConstants.SUBSCRIPTION_ID_KEY);


        productList = (ArrayList<String>) getArguments().getSerializable("productList");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        UIinitialization();
        modelCall();
        getProducts();

    }


    private void modelCall() {
        subscriptionViewModel = ViewModelProviders.of(this).get(SubscriptionViewModel.class);
    }

    private void loadDataFromModel(List<PackDetail> productsResponseMessage) {
        SubscriptionAdapter adapter = new SubscriptionAdapter(getActivity(), productsResponseMessage, productList);
        getBinding().recyclerView.setAdapter(adapter);

    }

    private void getProducts() {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        if (!UserInfo.getInstance(getActivity()).isActive()) {
            getProductsForLogout();
        } else {
            if (subscriptionIds != null) {
                JsonArray jsonArray = new JsonArray();
                for (String id : subscriptionIds) {
                    jsonArray.add(id);
                }
                subscriptionViewModel.getProductForLogin(UserInfo.getInstance(getActivity()).getAccessToken(), jsonArray).observe(this, evergentCommonResponse -> {
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    if (evergentCommonResponse.isStatus()) {
                        if (evergentCommonResponse.getGetProductResponse() != null && evergentCommonResponse.getGetProductResponse().getGetProductsResponseMessage() != null && evergentCommonResponse.getGetProductResponse().getGetProductsResponseMessage().getProductsResponseMessage() != null && evergentCommonResponse.getGetProductResponse().getGetProductsResponseMessage().getProductsResponseMessage().size() > 0) {
                            checkIfDetailAvailableOnPlaystore(evergentCommonResponse.getGetProductResponse().getGetProductsResponseMessage().getProductsResponseMessage());
                        }
                    } else {
                        if (evergentCommonResponse.getErrorCode().equalsIgnoreCase("eV2124") || evergentCommonResponse.getErrorCode().equals("111111111")) {
                            EvergentRefreshToken.refreshToken(getActivity(), UserInfo.getInstance(getActivity()).getRefreshToken()).observe(this, evergentCommonResponse1 -> {
                                if (evergentCommonResponse.isStatus()) {
                                    getProducts();
                                } else {
                                    AppCommonMethods.removeUserPrerences(getActivity());
                                }
                            });
                        } else {

                        }
                    }
                });
            } else {
                getProductsForLogout();
            }
        }
    }

    private void getProductsForLogout() {
        subscriptionViewModel.getProduct().observe(this, evergentCommonResponse -> {
            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
            if (evergentCommonResponse.isStatus()) {
                if (evergentCommonResponse.getGetProductResponse() != null && evergentCommonResponse.getGetProductResponse().getGetProductsResponseMessage() != null && evergentCommonResponse.getGetProductResponse().getGetProductsResponseMessage().getProductsResponseMessage() != null && evergentCommonResponse.getGetProductResponse().getGetProductsResponseMessage().getProductsResponseMessage().size() > 0) {
                    checkIfDetailAvailableOnPlaystore(evergentCommonResponse.getGetProductResponse().getGetProductsResponseMessage().getProductsResponseMessage());

                }
            } else {

            }
        });
    }

    private void checkIfDetailAvailableOnPlaystore(List<ProductsResponseMessageItem> productsResponseMessage) {
        packDetailList = new ArrayList<>();
        for (ProductsResponseMessageItem responseMessageItem : productsResponseMessage) {
            if (responseMessageItem.getAppChannels() != null && responseMessageItem.getAppChannels().get(0) != null && responseMessageItem.getAppChannels().get(0).getAppChannel() != null && responseMessageItem.getAppChannels().get(0).getAppChannel().equalsIgnoreCase("Google Wallet") && responseMessageItem.getAppChannels().get(0).getAppID() != null) {
                Log.w("avname", getActivity().getClass().getName() + "");
                if (getActivity() instanceof HomeActivity) {
                    skuDetails = ((HomeActivity) getActivity()).getSubscriptionDetail(responseMessageItem.getAppChannels().get(0).getAppID());
                } else if (getActivity() instanceof SubscriptionDetailActivity) {
                    if (responseMessageItem.getServiceType().equalsIgnoreCase("ppv")) {
                        skuDetails = ((SubscriptionDetailActivity) getActivity()).getPurchaseDetail(responseMessageItem.getAppChannels().get(0).getAppID());
                    } else {
                        skuDetails = ((SubscriptionDetailActivity) getActivity()).getSubscriptionDetail(responseMessageItem.getAppChannels().get(0).getAppID());

                    }
                }
                if (skuDetails != null) {
                    PackDetail packDetail = new PackDetail();
                    packDetail.setSkuDetails(skuDetails);
                    packDetail.setProductsResponseMessageItem(responseMessageItem);
                    packDetailList.add(packDetail);
                }
            }
        }
        if (packDetailList.size() > 0)
            loadDataFromModel(packDetailList);
    }

    private void UIinitialization() {
        if (from.equalsIgnoreCase("detail")) {
            getBinding().toolbar.setVisibility(View.GONE);
            getBinding().closeIcon.setVisibility(View.VISIBLE);
        }
        getBinding().recyclerView.hasFixedSize();
        getBinding().recyclerView.setNestedScrollingEnabled(false);
        getBinding().recyclerView.hasFixedSize();
        getBinding().recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        getBinding().closeIcon.setOnClickListener(v -> {
            if (getActivity() != null)
                getActivity().onBackPressed();
        });
    }

    @Override
    protected FragmentSubscriptionPacksBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentSubscriptionPacksBinding.inflate(inflater);
    }


    @Override
    public void onCardClicked(String productId, String serviceType) {

    }
}
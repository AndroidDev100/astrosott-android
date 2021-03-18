package com.astro.sott.fragments.manageSubscription.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;

import com.astro.sott.R;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.callBacks.commonCallBacks.ChangePlanCallBack;
import com.astro.sott.databinding.FragmentManageSubscriptionBinding;
import com.astro.sott.fragments.manageSubscription.adapter.ManageSubscriptionAdapter;
import com.astro.sott.fragments.subscription.ui.SubscriptionLandingFragment;
import com.astro.sott.fragments.subscription.vieModel.SubscriptionViewModel;
import com.astro.sott.usermanagment.modelClasses.activeSubscription.AccountServiceMessageItem;
import com.astro.sott.utils.userInfo.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageSubscriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageSubscriptionFragment extends BaseBindingFragment<FragmentManageSubscriptionBinding> implements ChangePlanCallBack {
    private SubscriptionViewModel subscriptionViewModel;
    private ArrayList<String> productIdList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ManageSubscriptionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageSubscriptionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageSubscriptionFragment newInstance(String param1, String param2) {
        ManageSubscriptionFragment fragment = new ManageSubscriptionFragment();
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
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolBar();
        getBinding().planRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        modelCall();
        getActiveSubscription();
    }

    private void getActiveSubscription() {
        getBinding().progressBar.setVisibility(View.VISIBLE);
        subscriptionViewModel.getActiveSubscription(UserInfo.getInstance(getActivity()).getAccessToken()).observe(this, evergentCommonResponse -> {
            getBinding().progressBar.setVisibility(View.GONE);
            if (evergentCommonResponse.isStatus()) {
                if (evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage() != null && evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage().getAccountServiceMessage() != null && evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage().getAccountServiceMessage().size() > 0) {
                    getListofActivePacks(evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage().getAccountServiceMessage());
                    loadData(evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage().getAccountServiceMessage());
                } else {
                    getBinding().nodataLayout.setVisibility(View.VISIBLE);
                }
            } else {
                getBinding().nodataLayout.setVisibility(View.VISIBLE);

            }
        });
    }

    private void getListofActivePacks(List<AccountServiceMessageItem> accountServiceMessage) {
        productIdList = new ArrayList<>();
        for (AccountServiceMessageItem accountServiceMessageItem : accountServiceMessage) {
            if (accountServiceMessageItem.getStatus().equalsIgnoreCase("ACTIVE")) {
                if (accountServiceMessageItem.getServiceID() != null)
                    productIdList.add(accountServiceMessageItem.getServiceID());

            }
        }
    }

    private void modelCall() {
        subscriptionViewModel = ViewModelProviders.of(this).get(SubscriptionViewModel.class);
    }

    private void loadData(List<AccountServiceMessageItem> accountServiceMessage) {
        ManageSubscriptionAdapter manageSubscriptionAdapter = new ManageSubscriptionAdapter(accountServiceMessage, getActivity(), this);
        getBinding().planRecycler.setAdapter(manageSubscriptionAdapter);
    }

    private void setToolBar() {
        getBinding().toolbar.title.setText(getResources().getString(R.string.manage_subscription));
    }

    @Override
    protected FragmentManageSubscriptionBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentManageSubscriptionBinding.inflate(inflater);
    }


    @Override
    public void onClick() {
        SubscriptionLandingFragment someFragment = new SubscriptionLandingFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("productList", productIdList);
        someFragment.setArguments(bundle);
        transaction.replace(R.id.content_frame, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
package com.astro.sott.fragments.manageSubscription.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.astro.sott.R;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.callBacks.commonCallBacks.ChangePlanCallBack;
import com.astro.sott.databinding.FragmentManageSubscriptionBinding;
import com.astro.sott.fragments.dialog.PlaylistDialogFragment;
import com.astro.sott.fragments.manageSubscription.adapter.ManageSubscriptionAdapter;
import com.astro.sott.fragments.subscription.ui.NewSubscriptionPacksFragment;
import com.astro.sott.fragments.subscription.ui.SubscriptionLandingFragment;
import com.astro.sott.fragments.subscription.vieModel.SubscriptionViewModel;
import com.astro.sott.networking.refreshToken.EvergentRefreshToken;
import com.astro.sott.usermanagment.modelClasses.activeSubscription.AccountServiceMessageItem;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.userInfo.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageSubscriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageSubscriptionFragment extends BaseBindingFragment<FragmentManageSubscriptionBinding> implements ChangePlanCallBack, CancelDialogFragment.EditDialogListener, PlanNotUpdated.PlanUpdatedListener {
    private SubscriptionViewModel subscriptionViewModel;
    private ArrayList<String> productIdList;
    private String cancelId;
    private List<AccountServiceMessageItem> accountServiceMessage;
    private List<AccountServiceMessageItem> freemiumFilterationList;
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


        getBinding().toolbar.backButton.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
    }

    private void getActiveSubscription() {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        subscriptionViewModel.getActiveSubscription(UserInfo.getInstance(getActivity()).getAccessToken(), "").observe(this, evergentCommonResponse -> {
            if (evergentCommonResponse.isStatus()) {
                if (evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage() != null && evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage().getAccountServiceMessage() != null && evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage().getAccountServiceMessage().size() > 0) {
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    getListofActivePacks(evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage().getAccountServiceMessage());
                    checkForFreemium(evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage().getAccountServiceMessage());
                } else {
                    getLastSubscription();
                }
            } else {

                if (evergentCommonResponse.getErrorCode().equalsIgnoreCase("eV2124") || evergentCommonResponse.getErrorCode().equals("111111111")) {
                    EvergentRefreshToken.refreshToken(getActivity(), UserInfo.getInstance(getActivity()).getRefreshToken()).observe(this, evergentCommonResponse1 -> {
                        if (evergentCommonResponse.isStatus()) {
                            getActiveSubscription();
                        } else {
                            AppCommonMethods.removeUserPrerences(getActivity());
                        }
                    });
                } else {
                    getLastSubscription();
                }
            }
        });
    }


    private void checkForFreemium(List<AccountServiceMessageItem> accountServiceMessage) {
        freemiumFilterationList = new ArrayList<>();
        for (AccountServiceMessageItem accountServiceMessageItem : accountServiceMessage) {
            if (!accountServiceMessageItem.isFreemium()) {
                freemiumFilterationList.add(accountServiceMessageItem);
            }
        }
        loadData(freemiumFilterationList);
    }

    private void getLastSubscription() {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        subscriptionViewModel.getLastSubscription(UserInfo.getInstance(getActivity()).getAccessToken()).observe(this, evergentCommonResponse -> {
            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
            if (evergentCommonResponse.isStatus()) {
                if (evergentCommonResponse.getResponse().getGetLastSubscriptionsResponseMessage() != null && evergentCommonResponse.getResponse().getGetLastSubscriptionsResponseMessage().getAccountServiceMessage() != null) {
                    accountServiceMessage = new ArrayList<>();
                    accountServiceMessage.add(evergentCommonResponse.getResponse().getGetLastSubscriptionsResponseMessage().getAccountServiceMessage());
                    loadData(accountServiceMessage);
                } else {
                    getBinding().nodataLayout.setVisibility(View.VISIBLE);
                }
            } else {

                if (evergentCommonResponse.getErrorCode().equalsIgnoreCase("eV2124") || evergentCommonResponse.getErrorCode().equals("111111111")) {
                    EvergentRefreshToken.refreshToken(getActivity(), UserInfo.getInstance(getActivity()).getRefreshToken()).observe(this, evergentCommonResponse1 -> {
                        if (evergentCommonResponse.isStatus()) {
                            getLastSubscription();
                        } else {
                            AppCommonMethods.removeUserPrerences(getActivity());
                        }
                    });
                } else {
                    getBinding().nodataLayout.setVisibility(View.VISIBLE);

                }

            }
        });
    }

    private void getListofActivePacks(List<AccountServiceMessageItem> accountServiceMessage) {
        productIdList = new ArrayList<>();
        for (AccountServiceMessageItem accountServiceMessageItem : accountServiceMessage) {
            if (accountServiceMessageItem.getStatus().equalsIgnoreCase("ACTIVE") && !accountServiceMessageItem.isFreemium()) {
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
    public void onClick(String paymentType) {
        if (paymentType.equalsIgnoreCase(AppLevelConstants.GOOGLE_WALLET)) {
            NewSubscriptionPacksFragment someFragment = new NewSubscriptionPacksFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putSerializable("productList", productIdList);
            someFragment.setArguments(bundle);
            transaction.replace(R.id.content_frame, someFragment,"SubscriptionFragment");
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            FragmentManager fm = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
            PlanNotUpdated planNotUpdated = PlanNotUpdated.newInstance(getActivity().getResources().getString(R.string.plan_with_different_payment), "");
            planNotUpdated.setEditDialogCallBack(ManageSubscriptionFragment.this);
            planNotUpdated.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
        }
    }

    @Override
    public void onCancel(String serviceId, String paymentType, String date) {
        if (paymentType.equalsIgnoreCase(AppLevelConstants.GOOGLE_WALLET)) {
            cancelId = serviceId;
            FragmentManager fm = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
            CancelDialogFragment cancelDialogFragment = CancelDialogFragment.newInstance(getActivity().getResources().getString(R.string.create_playlist_name_title), date);
            cancelDialogFragment.setEditDialogCallBack(ManageSubscriptionFragment.this);
            cancelDialogFragment.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
        } else {
            FragmentManager fm = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
            PlanNotUpdated planNotUpdated = PlanNotUpdated.newInstance(getActivity().getResources().getString(R.string.cancel_plan_with_different_payment), "");
            planNotUpdated.setEditDialogCallBack(ManageSubscriptionFragment.this);
            planNotUpdated.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
        }
    }

    @Override
    public void onFinishEditDialog() {
        removeSubscription();
    }

    private void removeSubscription() {
        subscriptionViewModel.removeSubscription(UserInfo.getInstance(getActivity()).getAccessToken(), cancelId).observe(this, evergentCommonResponse -> {
            if (evergentCommonResponse.isStatus()) {
                getActiveSubscription();
                Toast.makeText(getActivity(), "Subscription Successfully Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                if (evergentCommonResponse.getErrorCode().equalsIgnoreCase("eV2124") || evergentCommonResponse.getErrorCode().equalsIgnoreCase("111111111")) {
                    EvergentRefreshToken.refreshToken(getActivity(), UserInfo.getInstance(getActivity()).getRefreshToken()).observe(this, evergentResponse1 -> {
                        if (evergentResponse1.isStatus()) {
                            removeSubscription();
                        } else {
                            AppCommonMethods.removeUserPrerences(getActivity());
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    public void onPlanUpdated() {


    }
}
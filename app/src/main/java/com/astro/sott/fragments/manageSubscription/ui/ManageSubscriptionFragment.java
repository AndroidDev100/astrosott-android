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
import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.callBacks.commonCallBacks.ChangePlanCallBack;
import com.astro.sott.databinding.FragmentManageSubscriptionBinding;
import com.astro.sott.fragments.dialog.PlaylistDialogFragment;
import com.astro.sott.fragments.manageSubscription.adapter.ManageSubscriptionAdapter;
import com.astro.sott.fragments.subscription.ui.NewSubscriptionPacksFragment;
import com.astro.sott.fragments.subscription.ui.SubscriptionLandingFragment;
import com.astro.sott.fragments.subscription.vieModel.SubscriptionViewModel;
import com.astro.sott.networking.refreshToken.EvergentRefreshToken;
import com.astro.sott.thirdParty.fcm.FirebaseEventManager;
import com.astro.sott.usermanagment.modelClasses.activeSubscription.AccountServiceMessageItem;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.userInfo.UserInfo;

import java.util.ArrayList;
import java.util.List;


public class ManageSubscriptionFragment extends BaseBindingActivity<FragmentManageSubscriptionBinding> implements ChangePlanCallBack, CancelDialogFragment.EditDialogListener, PlanNotUpdated.PlanUpdatedListener {
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolBar();
        getBinding().planRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        modelCall();

        FirebaseEventManager.getFirebaseInstance(this).trackScreenName(FirebaseEventManager.MANAGE_SUBSCRIPTION);

        getBinding().toolbar.backButton.setOnClickListener(v -> {
            onBackPressed();
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        getActiveSubscription();
    }

    private void getActiveSubscription() {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        subscriptionViewModel.getActiveSubscription(UserInfo.getInstance(this).getAccessToken(), "").observe(this, evergentCommonResponse -> {
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
                    EvergentRefreshToken.refreshToken(this, UserInfo.getInstance(this).getRefreshToken()).observe(this, evergentCommonResponse1 -> {
                        if (evergentCommonResponse.isStatus()) {
                            getActiveSubscription();
                        } else {
                            AppCommonMethods.removeUserPrerences(this);
                        }
                    });
                } else {
                    getLastSubscription();
                }
            }
        });
    }

    private AccountServiceMessageItem accountServiceItem;
    private List<AccountServiceMessageItem> accountServiceMessageItemList;

    private void checkForFreemium(List<AccountServiceMessageItem> accountServiceMessage) {
        freemiumFilterationList = new ArrayList<>();
        accountServiceMessageItemList = new ArrayList<>();
        for (AccountServiceMessageItem accountServiceMessageItem : accountServiceMessage) {
            if (!accountServiceMessageItem.isFreemium()) {
                accountServiceItem = accountServiceMessageItem;
            }
        }
        accountServiceMessageItemList.add(accountServiceItem);
        loadData(accountServiceMessageItemList);
    }


    private void getLastSubscription() {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        subscriptionViewModel.getLastSubscription(UserInfo.getInstance(this).getAccessToken()).observe(this, evergentCommonResponse -> {
            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
            if (evergentCommonResponse.isStatus()) {
                if (evergentCommonResponse.getResponse().getGetLastSubscriptionResponseMessage() != null && evergentCommonResponse.getResponse().getGetLastSubscriptionResponseMessage().getAccountServiceMessage() != null) {
                    accountServiceMessage = new ArrayList<>();
                    //accountServiceMessage.add(evergentCommonResponse.getResponse().getGetLastSubscriptionResponseMessage().getAccountServiceMessage());
                    loadData(accountServiceMessage);
                } else {
                    getBinding().nodataLayout.setVisibility(View.VISIBLE);
                }
            } else {

                if (evergentCommonResponse.getErrorCode().equalsIgnoreCase("eV2124") || evergentCommonResponse.getErrorCode().equals("111111111")) {
                    EvergentRefreshToken.refreshToken(this, UserInfo.getInstance(this).getRefreshToken()).observe(this, evergentCommonResponse1 -> {
                        if (evergentCommonResponse.isStatus()) {
                            getLastSubscription();
                        } else {
                            AppCommonMethods.removeUserPrerences(this);
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
        ManageSubscriptionAdapter manageSubscriptionAdapter = new ManageSubscriptionAdapter(accountServiceMessage, this, this);
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
            new ActivityLauncher(this).profileSubscription("Profile");
        } else {
            FragmentManager fm = ((AppCompatActivity) this).getSupportFragmentManager();
            PlanNotUpdated planNotUpdated = PlanNotUpdated.newInstance(getResources().getString(R.string.plan_with_different_payment), "");
            planNotUpdated.setEditDialogCallBack(ManageSubscriptionFragment.this);
            planNotUpdated.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
        }
    }

    @Override
    public void onCancel(String serviceId, String paymentType, String date) {
        if (paymentType.equalsIgnoreCase(AppLevelConstants.GOOGLE_WALLET)) {
            cancelId = serviceId;
            FragmentManager fm = ((AppCompatActivity) this).getSupportFragmentManager();
            CancelDialogFragment cancelDialogFragment = CancelDialogFragment.newInstance(getResources().getString(R.string.create_playlist_name_title), date);
            cancelDialogFragment.setEditDialogCallBack(ManageSubscriptionFragment.this);
            cancelDialogFragment.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
        } else {
            FragmentManager fm = ((AppCompatActivity) this).getSupportFragmentManager();
            PlanNotUpdated planNotUpdated = PlanNotUpdated.newInstance(getResources().getString(R.string.cancel_plan_with_different_payment), "");
            planNotUpdated.setEditDialogCallBack(ManageSubscriptionFragment.this);
            planNotUpdated.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
        }
    }

    @Override
    public void onFinishEditDialog() {
        removeSubscription();
    }

    private void removeSubscription() {
        subscriptionViewModel.removeSubscription(UserInfo.getInstance(this).getAccessToken(), cancelId).observe(this, evergentCommonResponse -> {
            if (evergentCommonResponse.isStatus()) {
                getActiveSubscription();
                Toast.makeText(this, "Subscription Successfully Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                if (evergentCommonResponse.getErrorCode().equalsIgnoreCase("eV2124") || evergentCommonResponse.getErrorCode().equalsIgnoreCase("111111111")) {
                    EvergentRefreshToken.refreshToken(this, UserInfo.getInstance(this).getRefreshToken()).observe(this, evergentResponse1 -> {
                        if (evergentResponse1.isStatus()) {
                            removeSubscription();
                        } else {
                            AppCommonMethods.removeUserPrerences(this);
                        }
                    });
                } else {
                    Toast.makeText(this, evergentCommonResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    public void onPlanUpdated() {


    }

}
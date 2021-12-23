package com.astro.sott.fragments.moreTab.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;

import com.astro.sott.BuildConfig;
import com.astro.sott.R;
import com.astro.sott.activities.forgotPassword.ui.IsThisYouActivity;
import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.activities.language.ui.LanguageSettingsActivity;
import com.astro.sott.activities.loginActivity.ui.AstrLoginActivity;
import com.astro.sott.activities.manageDevice.ui.ManageDeviceActivity;
import com.astro.sott.activities.profile.ui.ChangeEmailConfirmation;
import com.astro.sott.activities.profile.ui.EditEmailActivity;
import com.astro.sott.activities.profile.ui.EditProfileActivity;
import com.astro.sott.activities.signUp.ui.SignUpActivity;
import com.astro.sott.activities.subscriptionActivity.ui.ProfileSubscriptionActivity;
import com.astro.sott.activities.webview.ui.WebViewActivity;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.databinding.FragmentMoreLayoutBinding;
import com.astro.sott.fragments.dialog.AlertDialogFragment;
import com.astro.sott.fragments.dialog.MaxisEditRestrictionPop;
import com.astro.sott.fragments.manageSubscription.ui.CancelDialogFragment;
import com.astro.sott.fragments.manageSubscription.ui.ManageSubscriptionFragment;
import com.astro.sott.fragments.manageSubscription.ui.PlanNotUpdated;
import com.astro.sott.fragments.subscription.ui.NewSubscriptionPacksFragment;
import com.astro.sott.fragments.subscription.ui.SubscriptionLandingFragment;
import com.astro.sott.fragments.subscription.vieModel.SubscriptionViewModel;
import com.astro.sott.fragments.transactionhistory.ui.TransactionHistory;
import com.astro.sott.networking.refreshToken.EvergentRefreshToken;
import com.astro.sott.thirdParty.CleverTapManager.CleverTapManager;
import com.astro.sott.thirdParty.fcm.FirebaseEventManager;
import com.astro.sott.usermanagment.modelClasses.activeSubscription.AccountServiceMessageItem;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.utils.userInfo.UserInfo;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoreNewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreNewFragment extends BaseBindingFragment<FragmentMoreLayoutBinding> implements AlertDialogFragment.AlertDialogListener, PlanNotUpdated.PlanUpdatedListener, CancelDialogFragment.EditDialogListener {
    private SubscriptionViewModel subscriptionViewModel;
    private String displayName = "", paymentMethod = "";
    private boolean isRenewal = false;
    private int activePackListSize;
    private List<AccountServiceMessageItem> accountServiceMessageItemList;
    Long validTill;
    private AccountServiceMessageItem lastActiveItem, downgradeActive, lastExpiredItem;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentMoreLayoutBinding mBinding;
    private String oldLang, newLang;
    BottomNavigationView navBar;

    public MoreNewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoreNewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoreNewFragment newInstance(String param1, String param2) {
        MoreNewFragment fragment = new MoreNewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    //    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//
//    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        oldLang = new KsPreferenceKey(getActivity()).getAppLangName();
        navBar = getActivity().findViewById(R.id.navigation);
        modelCall();
        //  checkForLoginLogout();
        UIinitialization();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public void checkForLoginLogout() {
        if (UserInfo.getInstance(getActivity()).isActive()) {
            displayName = "";
            getActiveSubscription();
        } else {
            setUiForLogout();
        }
    }

    private void UIinitialization() {
        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
        getBinding().toolbar.setVisibility(View.VISIBLE);
        getBinding().tvVersion.setText("Version " + BuildConfig.VERSION_NAME);
        setClicks();
    }

    private void modelCall() {
        subscriptionViewModel = ViewModelProviders.of(this).get(SubscriptionViewModel.class);
    }

    private void setClicks() {
        getBinding().changePlan.setOnClickListener(v -> {
            if (UserInfo.getInstance(getActivity()).isMaxis()) {
                maxisRestrictionPopUp(getResources().getString(R.string.maxis_upgrade_downgrade_restriction_description));
            } else {
                if (lastActiveItem.getPaymentMethod().equalsIgnoreCase(AppLevelConstants.GOOGLE_WALLET)) {
                    new ActivityLauncher(getActivity()).profileSubscription("Profile");
                } else {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    PlanNotUpdated planNotUpdated = PlanNotUpdated.newInstance(getResources().getString(R.string.plan_with_different_payment), "");
                    planNotUpdated.setEditDialogCallBack(MoreNewFragment.this);
                    planNotUpdated.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
                }
            }
        });
        getBinding().cancelPlan.setOnClickListener(v -> {
            if (UserInfo.getInstance(getActivity()).isMaxis()) {
                maxisRestrictionPopUp(getResources().getString(R.string.maxis_upgrade_downgrade_restriction_description));
            } else {
                if (lastActiveItem.isRenewal() && lastActiveItem.getServiceID() != null && lastActiveItem.getValidityTill() != null) {
                    if (lastActiveItem.getPaymentMethod().equalsIgnoreCase(AppLevelConstants.GOOGLE_WALLET)) {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        CancelDialogFragment cancelDialogFragment = CancelDialogFragment.newInstance(getResources().getString(R.string.create_playlist_name_title), AppCommonMethods.getDateFromTimeStamp(lastActiveItem.getValidityTill()));
                        cancelDialogFragment.setEditDialogCallBack(MoreNewFragment.this);
                        cancelDialogFragment.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
                    } else {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        PlanNotUpdated planNotUpdated = PlanNotUpdated.newInstance(getResources().getString(R.string.cancel_plan_with_different_payment), "");
                        planNotUpdated.setEditDialogCallBack(MoreNewFragment.this);
                        planNotUpdated.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
                    }
                }
            }
        });
        getBinding().circularImageViewMore.setOnClickListener(v -> {
           /* Intent intent = new Intent(getActivity(), EditEmailActivity.class);
            startActivity(intent);*/
        });
        getBinding().edit.setOnClickListener(v -> {
            new ActivityLauncher(getActivity()).profileActivity(getActivity());
        });

        getBinding().loginSignupMore.setOnClickListener(view -> {
            FirebaseEventManager.getFirebaseInstance(getActivity()).subscribeClicked = false;
            FirebaseEventManager.getFirebaseInstance(getActivity()).itemListEvent(FirebaseEventManager.PROFILE, "Sign Up/ Sign In", FirebaseEventManager.BTN_CLICK);
            new ActivityLauncher(getActivity()).signupActivity(getActivity(), SignUpActivity.class, "Profile");

        });
        getBinding().rlManagePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), IsThisYouActivity.class);
                startActivity(intent);
            }
        });
        getBinding().subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseEventManager.getFirebaseInstance(getActivity()).itemListEvent(FirebaseEventManager.PROFILE, getBinding().subscribe.getText().toString(), FirebaseEventManager.BTN_CLICK);
                if (UserInfo.getInstance(getActivity()).isActive()) {
                    navBar.setVisibility(View.GONE);
                    new ActivityLauncher(getActivity()).profileSubscription("Profile");
                } else {
                    FirebaseEventManager.getFirebaseInstance(getActivity()).subscribeClicked = true;
                    new ActivityLauncher(getActivity()).signupActivity(getActivity(), SignUpActivity.class, "Profile");
                }
            }
        });
        getBinding().rlLinkedAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navBar.setVisibility(View.GONE);
                ChangeEmailConfirmation someFragment = new ChangeEmailConfirmation();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, someFragment); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });
        getBinding().rlTransactionHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navBar.setVisibility(View.GONE);
                if (UserInfo.getInstance(getActivity()).isActive()) {
                    startActivity(new Intent(getActivity(), TransactionHistory.class));

                } else {
                    FirebaseEventManager.getFirebaseInstance(getActivity()).subscribeClicked = false;
                    new ActivityLauncher(getActivity()).signupActivity(getActivity(), SignUpActivity.class, "Profile");
                }
            }
        });
        getBinding().rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseEventManager.getFirebaseInstance(getActivity()).itemListEvent(FirebaseEventManager.PROFILE, FirebaseEventManager.LOGOUT, FirebaseEventManager.BTN_CLICK);
                showAlertDialog(getResources().getString(R.string.logout), getResources().getString(R.string.logout_confirmation_message_new));
            }
        });
        getBinding().rlManageDevice.setOnClickListener(view -> {
            if (UserInfo.getInstance(getActivity()).isActive()) {
                Intent manageDeviceIntent = new Intent(getActivity(), ManageDeviceActivity.class);
                manageDeviceIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(manageDeviceIntent);
            } else {
                FirebaseEventManager.getFirebaseInstance(getActivity()).subscribeClicked = false;
                new ActivityLauncher(getActivity()).signupActivity(getActivity(), SignUpActivity.class, "Profile");

            }


        });
        getBinding().rlHelp.setOnClickListener(v -> {
            FirebaseEventManager.getFirebaseInstance(getActivity()).itemListEvent(FirebaseEventManager.PROFILE, FirebaseEventManager.HELP, FirebaseEventManager.BTN_CLICK);
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra(AppLevelConstants.WEBVIEW, AppLevelConstants.HELP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        getBinding().rlTerms.setOnClickListener(v -> {
            new ActivityLauncher(getActivity()).termAndCondition(getActivity());
        });

        getBinding().rlPrivacy.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra(AppLevelConstants.WEBVIEW, "Privacy Policies");
            startActivity(intent);
        });

        getBinding().rlLanguageSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), LanguageSettingsActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);


            }
        });
    }

    private void showAlertDialog(String title, String msg) {
        FragmentManager fm = getFragmentManager();
        AlertDialogFragment alertDialog = AlertDialogFragment.newInstance(title, msg, getResources().getString(R.string.yes), getResources().getString(R.string.oops_no));
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(Objects.requireNonNull(fm), "fragment_alert");
    }

    private void removeSubscription() {
        subscriptionViewModel.removeSubscription(UserInfo.getInstance(getActivity()).getAccessToken(), lastActiveItem.getServiceID()).observe(this, evergentCommonResponse -> {
            if (evergentCommonResponse.isStatus()) {
                getActiveSubscription();
                ToastHandler.show("Subscription Successfully Cancelled", getActivity());
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
                    ToastHandler.show(evergentCommonResponse.getErrorMessage(), getActivity());
                }
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (navBar.getVisibility() != View.VISIBLE) {
            navBar.setVisibility(View.VISIBLE);
        }
        FirebaseEventManager.getFirebaseInstance(getActivity()).trackScreenName(FirebaseEventManager.PROFILE);
        checkForLoginLogout();
        updateLang();
        if (UserInfo.getInstance(getActivity()).isActive()) {
            getBinding().loginUi.setVisibility(View.VISIBLE);
            getBinding().loginSignupMore.setVisibility(View.GONE);
            getBinding().rlLogout.setVisibility(View.VISIBLE);
            if (!UserInfo.getInstance(getActivity()).getFirstName().equalsIgnoreCase("")) {
                getBinding().tvName.setText(UserInfo.getInstance(getActivity()).getFirstName());
            } else {
                getBinding().tvName.setText(getResources().getString(R.string.sooka_superstar));

            }
            if (!UserInfo.getInstance(getActivity()).getEmail().equalsIgnoreCase("")) {
                getBinding().tvEmail.setText(AppCommonMethods.maskedEmail(getActivity()));
            } else {
                getBinding().tvEmail.setText(AppCommonMethods.maskedMobile(getActivity()));
            }

            getBinding().edit.setVisibility(View.VISIBLE);
        } else {
            getBinding().loginUi.setVisibility(View.GONE);
            getBinding().loginSignupMore.setVisibility(View.VISIBLE);
            getBinding().rlLogout.setVisibility(View.GONE);
            getBinding().edit.setVisibility(View.GONE);


        }
    }

    public void setUiForLogout() {
        if (UserInfo.getInstance(getActivity()).isActive()) {
            getBinding().tvVIPUser.setText(getResources().getString(R.string.free_sooka));
        } else {
            getBinding().tvVIPUser.setText(getResources().getString(R.string.free_sooka));
            getBinding().edit.setVisibility(View.GONE);
            getBinding().rlLogout.setVisibility(View.GONE);
            getBinding().loginSignupMore.setVisibility(View.VISIBLE);
            getBinding().loginUi.setVisibility(View.GONE);
        }
        getBinding().changePlan.setVisibility(View.GONE);
        getBinding().cancelPlan.setVisibility(View.GONE);
        getBinding().partnerBillingText.setVisibility(View.GONE);
        getBinding().tvBilling.setVisibility(View.GONE);
        getBinding().tvSubscribeNow.setVisibility(View.VISIBLE);
        getBinding().subscribe.setBackground(getResources().getDrawable(R.drawable.gradient_svod));
        getBinding().tvSubscribeNow.setText(getResources().getString(R.string.subscription_description));
        getBinding().subscribe.setText(getResources().getString(R.string.become_vip));
        getBinding().subscribe.setVisibility(View.VISIBLE);


    }


    private void getActiveSubscription() {
        displayName = "";
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        subscriptionViewModel.getActiveSubscription(UserInfo.getInstance(getActivity()).getAccessToken(), "profile").observe(this, evergentCommonResponse -> {
            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
            if (evergentCommonResponse.isStatus()) {
                if (evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage() != null && evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage().getAccountServiceMessage() != null && evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage().getAccountServiceMessage().size() > 0) {
                    removeFreemium(evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage().getAccountServiceMessage());
                    activePackListSize = accountServiceMessageItemList.size();
                    if (activePackListSize > 0) {
                        getActivePacks(accountServiceMessageItemList);
                    } else {
                        getLastSubscription();
                    }
                } else {
                    if (FirebaseEventManager.getFirebaseInstance(getActivity()).subscribeClicked)
                        new ActivityLauncher(getActivity()).profileSubscription("Profile");
                    setUiForLogout();
                }
            } else {
                if (evergentCommonResponse.getErrorCode().equalsIgnoreCase("eV2124") || evergentCommonResponse.getErrorCode().equals("111111111")) {
                    EvergentRefreshToken.refreshToken(getActivity(), UserInfo.getInstance(getActivity()).getRefreshToken()).observe(this, evergentCommonResponse1 -> {
                        if (evergentCommonResponse.isStatus()) {
                            getActiveSubscription();
                        } else {
                            AppCommonMethods.removeUserPrerences(getActivity());
                            setUiForLogout();
                        }
                    });
                } else {
                    setUiForLogout();
                }
            }
        });
    }

    private void removeFreemium(List<AccountServiceMessageItem> accountServiceMessage) {
        accountServiceMessageItemList = new ArrayList<>();
        for (AccountServiceMessageItem accountServiceMessageItem : accountServiceMessage) {
            if (!accountServiceMessageItem.isFreemium()) {
                accountServiceMessageItemList.add(accountServiceMessageItem);
            }
        }

    }

    private void getActivePacks(List<AccountServiceMessageItem> accountServiceMessageItemList) {
        if (accountServiceMessageItemList.get(activePackListSize - 1) != null) {
            if (activePackListSize > 1) {
                checkForDowngrade(accountServiceMessageItemList);
            } else {
                try {
                    if (accountServiceMessageItemList.get(activePackListSize - 1).isCurrentPlan() != null && accountServiceMessageItemList.get(activePackListSize - 1).isCurrentPlan()) {
                        if (accountServiceMessageItemList.get(activePackListSize - 1).getStatus().equalsIgnoreCase("ACTIVE")) {
                            lastActiveItem = accountServiceMessageItemList.get(activePackListSize - 1);
                        } else if (accountServiceMessageItemList.get(activePackListSize - 1).getStatus().equalsIgnoreCase("FINAL BILL")) {
                            lastActiveItem = accountServiceMessageItemList.get(activePackListSize - 1);
                        }
                    }
                    if (lastActiveItem != null) {
                        UserInfo.getInstance(getActivity()).setVip(true);
                        UserInfo.getInstance(getActivity()).setMaxis(lastActiveItem.getPaymentMethod().equalsIgnoreCase(AppLevelConstants.MAXIS_BILLING));
                        getBinding().tvVIPUser.setText(lastActiveItem.getDisplayName());
                        if (!lastActiveItem.isRenewal()) {
                            getBinding().tvSubscribeNow.setVisibility(View.GONE);
                        } else {
                            getBinding().tvSubscribeNow.setVisibility(View.VISIBLE);
                            if (!lastActiveItem.getStatus().equalsIgnoreCase("FINAL BILL")) {
                                getBinding().tvSubscribeNow.setText("Renew on " + AppCommonMethods.getRenewDate(lastActiveItem.getValidityTill()));
                            } else {
                                getBinding().tvSubscribeNow.setText("Ends on " + AppCommonMethods.getRenewDate(lastActiveItem.getValidityTill()));
                            }
                        }
                        getBinding().partnerBillingText.setVisibility(View.VISIBLE);
                        if (lastActiveItem.getPaymentMethod() != null && !lastActiveItem.getPaymentMethod().equalsIgnoreCase("")) {
                            if (UserInfo.getInstance(getActivity()).isMaxis()) {
                                getBinding().partnerBillingText.setText(lastActiveItem.getPaymentMethod());
                            } else {
                                getBinding().partnerBillingText.setText(lastActiveItem.getPaymentMethod() + ": " + lastActiveItem.getCurrencyCode() + lastActiveItem.getRetailPrice() + "/month");
                            }
                        }
                        getBinding().subscribe.setVisibility(View.GONE);
                        if (lastActiveItem.getStatus().equalsIgnoreCase("ACTIVE")) {
                            getBinding().cancelPlan.setVisibility(View.VISIBLE);
                            getBinding().changePlan.setVisibility(View.VISIBLE);
                        } else {
                            getBinding().cancelPlan.setVisibility(View.GONE);
                            getBinding().changePlan.setVisibility(View.GONE);
                        }
                    } else {
                        getLastSubscription();
                    }
                } catch (Exception e) {
                }

            }
        }


    }

    private void checkForDowngrade(List<AccountServiceMessageItem> accountServiceMessageItemList) {
        for (AccountServiceMessageItem accountServiceMessageItem : accountServiceMessageItemList) {
            if (accountServiceMessageItem.getStatus().equalsIgnoreCase("FINAL BILL") && accountServiceMessageItem.isCurrentPlan() != null && accountServiceMessageItem.isCurrentPlan() && AppCommonMethods.getCurrentTimeStampLong() < accountServiceMessageItem.getValidityTill()) {
                lastActiveItem = accountServiceMessageItem;
            } else if (accountServiceMessageItem.getStatus().equalsIgnoreCase("ACTIVE")) {
                downgradeActive = accountServiceMessageItem;
            }
        }
        if (lastActiveItem != null) {
            UserInfo.getInstance(getActivity()).setVip(true);
            UserInfo.getInstance(getActivity()).setMaxis(lastActiveItem.getPaymentMethod().equalsIgnoreCase(AppLevelConstants.MAXIS_BILLING));
            getBinding().tvVIPUser.setText(lastActiveItem.getDisplayName());
            if (!lastActiveItem.isRenewal()) {
                getBinding().tvSubscribeNow.setVisibility(View.GONE);
            } else {
                getBinding().tvSubscribeNow.setVisibility(View.VISIBLE);
                getBinding().tvSubscribeNow.setText(getResources().getString(R.string.new_plan) + " " + downgradeActive.getDisplayName() + " " + getResources().getString(R.string.downgrade_billing_description) + " " + AppCommonMethods.getRenewDate(lastActiveItem.getValidityTill()));
            }
            getBinding().partnerBillingText.setVisibility(View.VISIBLE);
            if (lastActiveItem.getPaymentMethod() != null && !lastActiveItem.getPaymentMethod().equalsIgnoreCase("")) {
                getBinding().partnerBillingText.setText(lastActiveItem.getPaymentMethod() + ": " + lastActiveItem.getCurrencyCode() + lastActiveItem.getRetailPrice() + "/month");
            }
            getBinding().subscribe.setVisibility(View.GONE);
            if (lastActiveItem.getStatus().equalsIgnoreCase("ACTIVE")) {
                getBinding().cancelPlan.setVisibility(View.VISIBLE);
                getBinding().changePlan.setVisibility(View.VISIBLE);
            }
        } else {
            getLastSubscription();
        }
    }

    private void maxisRestrictionPopUp(String message) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        MaxisEditRestrictionPop cancelDialogFragment = MaxisEditRestrictionPop.newInstance(getResources().getString(R.string.maxis_edit_restriction_title), message, getResources().getString(R.string.ok_understand));
        cancelDialogFragment.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

    private void getLastSubscription() {
        subscriptionViewModel.getLastSubscription(UserInfo.getInstance(getActivity()).getAccessToken()).observe(this, evergentCommonResponse -> {
            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
            if (evergentCommonResponse.isStatus()) {
                if (evergentCommonResponse.getResponse().getGetLastSubscriptionResponseMessage() != null && evergentCommonResponse.getResponse().getGetLastSubscriptionResponseMessage().getAccountServiceMessage() != null) {
                    if (evergentCommonResponse.getResponse().getGetLastSubscriptionResponseMessage().getAccountServiceMessage().getStatus().equalsIgnoreCase("EXPIRED")) {
                        getBinding().tvVIPUser.setText(evergentCommonResponse.getResponse().getGetLastSubscriptionResponseMessage().getAccountServiceMessage().getDisplayName());
                        getBinding().tvSubscribeNow.setVisibility(View.VISIBLE);
                        getBinding().subscribe.setVisibility(View.VISIBLE);
                        getBinding().subscribe.setText(getResources().getString(R.string.become_vip));
                        getBinding().partnerBillingText.setVisibility(View.VISIBLE);
                        getBinding().changePlan.setVisibility(View.GONE);
                        getBinding().cancelPlan.setVisibility(View.GONE);
                        if (evergentCommonResponse.getResponse().getGetLastSubscriptionResponseMessage().getAccountServiceMessage().getPaymentMethod() != null && !evergentCommonResponse.getResponse().getGetLastSubscriptionResponseMessage().getAccountServiceMessage().getPaymentMethod().equalsIgnoreCase("")) {
                            if (evergentCommonResponse.getResponse().getGetLastSubscriptionResponseMessage().getAccountServiceMessage().getPaymentMethod().equalsIgnoreCase(AppLevelConstants.MAXIS_BILLING)) {
                                getBinding().partnerBillingText.setText(evergentCommonResponse.getResponse().getGetLastSubscriptionResponseMessage().getAccountServiceMessage().getPaymentMethod());
                            } else {
                                getBinding().partnerBillingText.setText(evergentCommonResponse.getResponse().getGetLastSubscriptionResponseMessage().getAccountServiceMessage().getPaymentMethod() + ": " + evergentCommonResponse.getResponse().getGetLastSubscriptionResponseMessage().getAccountServiceMessage().getCurrencyCode() + evergentCommonResponse.getResponse().getGetLastSubscriptionResponseMessage().getAccountServiceMessage().getRetailPrice() + "/month");
                            }
                        }
                        if (evergentCommonResponse.getResponse().getGetLastSubscriptionResponseMessage().getAccountServiceMessage().getValidityTill() != null) {
                            getBinding().tvSubscribeNow.setText("Ended on " + AppCommonMethods.getRenewDate(evergentCommonResponse.getResponse().getGetLastSubscriptionResponseMessage().getAccountServiceMessage().getValidityTill()));
                        }
                    } else {
                        if (FirebaseEventManager.getFirebaseInstance(getActivity()).subscribeClicked)
                            new ActivityLauncher(getActivity()).profileSubscription("Profile");
                        setUiForLogout();
                    }
                } else {
                    if (FirebaseEventManager.getFirebaseInstance(getActivity()).subscribeClicked)
                        new ActivityLauncher(getActivity()).profileSubscription("Profile");
                    setUiForLogout();
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
                    if (FirebaseEventManager.getFirebaseInstance(getActivity()).subscribeClicked)
                        new ActivityLauncher(getActivity()).profileSubscription("Profile");
                    setUiForLogout();

                }

            }
        });
    }

    @Override
    public FragmentMoreLayoutBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentMoreLayoutBinding.inflate(inflater);
    }

    private void updateLang() {
        newLang = new KsPreferenceKey(getActivity()).getAppLangName();
        if (!oldLang.equals(newLang)) {
            oldLang = newLang;
            if (newLang.equalsIgnoreCase("ms")) {
                getBinding().loginSignupMore.setText("Log masuk / Daftar");
                getBinding().tvVIPUser.setText("Pengguna tetamu");
                getBinding().subscribe.setText("Langgan");
                getBinding().tvManagePayments.setText("Urus Pembayaran");
                getBinding().tvTranscHistory.setText("sejarah transaksi");
                getBinding().tvLanguageSettings.setText("Pemilihan Bahasa");
                getBinding().tvDownloads.setText("Muat turun");
                getBinding().tvManageDevices.setText("Urus Peranti");
                getBinding().tvContentPrefrence.setText("Keutamaan Kandung");
                getBinding().tvCoupanRedemption.setText("Penebusan Kupon");
                getBinding().tvHelp.setText("Tolonglah");
                getBinding().tvLogout.setText("Log keluar");
                getBinding().tvVersion.setText("Versi" + BuildConfig.VERSION_NAME);
            } else {
                getBinding().loginSignupMore.setText("Sign In/Sign Up");
                getBinding().tvManagePayments.setText("Manage Payments");
                getBinding().tvTranscHistory.setText("Transaction History");
                getBinding().tvLanguageSettings.setText("Language Selection");
                getBinding().tvDownloads.setText("Downloads");
                getBinding().tvManageDevices.setText("Manage Device");
                getBinding().tvContentPrefrence.setText("Content Preferences");
                getBinding().tvCoupanRedemption.setText("Coupon Redemption");
                getBinding().tvHelp.setText("Help");
                getBinding().tvLogout.setText("Logout");
                getBinding().tvVersion.setText("Version" + BuildConfig.VERSION_NAME);
            }


        }
    }


    @Override
    public void onFinishDialog() {
        logoutApi();
        AppCommonMethods.removeUserPrerences(getActivity());
        ToastHandler.show(getActivity().getResources().getString(R.string.logout_success), getActivity());
        setUiForLogout();

        LoginManager.getInstance().logOut();
        CleverTapManager.getInstance().setSignOutEvent(getActivity());

        new ActivityLauncher(getActivity()).homeScreen(getActivity(), HomeActivity.class);

    }

    private void logoutApi() {
        subscriptionViewModel.logoutUser(UserInfo.getInstance(getActivity()).getAccessToken(), UserInfo.getInstance(getActivity()).getExternalSessionToken()).observe(this, logoutExternalResponseEvergentCommonResponse -> {

        });
    }

    @Override
    public void onPlanUpdated() {

    }

    @Override
    public void onFinishEditDialog() {
        removeSubscription();
    }
}
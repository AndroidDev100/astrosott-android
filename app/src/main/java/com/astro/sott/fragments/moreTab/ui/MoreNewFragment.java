package com.astro.sott.fragments.moreTab.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;

import com.astro.sott.BuildConfig;
import com.astro.sott.R;
import com.astro.sott.activities.forgotPassword.ui.IsThisYouActivity;
import com.astro.sott.activities.language.ui.LanguageSettingsActivity;
import com.astro.sott.activities.loginActivity.ui.AstrLoginActivity;
import com.astro.sott.activities.manageDevice.ui.ManageDeviceActivity;
import com.astro.sott.activities.profile.ui.ChangeEmailConfirmation;
import com.astro.sott.activities.profile.ui.EditEmailActivity;
import com.astro.sott.activities.profile.ui.EditProfileActivity;
import com.astro.sott.activities.webview.ui.WebViewActivity;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.databinding.FragmentMoreLayoutBinding;
import com.astro.sott.fragments.dialog.AlertDialogFragment;
import com.astro.sott.fragments.manageSubscription.ui.ManageSubscriptionFragment;
import com.astro.sott.fragments.subscription.ui.SubscriptionLandingFragment;
import com.astro.sott.fragments.subscription.vieModel.SubscriptionViewModel;
import com.astro.sott.fragments.transactionhistory.ui.TransactionHistory;
import com.astro.sott.networking.refreshToken.EvergentRefreshToken;
import com.astro.sott.usermanagment.modelClasses.activeSubscription.AccountServiceMessageItem;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.utils.userInfo.UserInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoreNewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreNewFragment extends BaseBindingFragment<FragmentMoreLayoutBinding> implements AlertDialogFragment.AlertDialogListener {
    private SubscriptionViewModel subscriptionViewModel;

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
        checkForLoginLogout();
        UIinitialization();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public void checkForLoginLogout() {
        if (UserInfo.getInstance(getActivity()).isActive()) {
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
        getBinding().circularImageViewMore.setOnClickListener(v -> {
           /* Intent intent = new Intent(getActivity(), EditEmailActivity.class);
            startActivity(intent);*/
        });
        getBinding().edit.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        getBinding().loginSignupMore.setOnClickListener(view -> {

            new ActivityLauncher(getActivity()).astrLoginActivity(getActivity(), AstrLoginActivity.class, "profile");

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

                if (getBinding().subscribe.getText().toString().equalsIgnoreCase("subscribe")) {
                    navBar.setVisibility(View.GONE);
                    SubscriptionLandingFragment someFragment = new SubscriptionLandingFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("productList", new ArrayList<String>());
                    someFragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_frame, someFragment); // give your fragment container id in first parameter
                    transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                    transaction.commit();
                } else {
                    navBar.setVisibility(View.GONE);
                    ManageSubscriptionFragment manageSubscriptionFragment = new ManageSubscriptionFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_frame, manageSubscriptionFragment); // give your fragment container id in first parameter
                    transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                    transaction.commit();
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
                    TransactionHistory transactionHistory = new TransactionHistory();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_frame, transactionHistory); // give your fragment container id in first parameter
                    transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                    transaction.commit();
                } else {
                    new ActivityLauncher(getActivity()).astrLoginActivity(getActivity(), AstrLoginActivity.class, "profile");

                }
//                QuickSearchGenre quickSearchGenre = new QuickSearchGenre();
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.content_frame, quickSearchGenre); // give your fragment container id in first parameter
//                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
//                transaction.commit();
            }
        });
        getBinding().rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog(getResources().getString(R.string.logout), getResources().getString(R.string.logout_confirmation_message_new));
            }
        });
        getBinding().rlContentPreference.setOnClickListener(v -> {
           /* HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
            profileUpdate.put("Name", UserInfo.getInstance(getActivity()).getFirstName());                  // String
            profileUpdate.put("Identity", UserInfo.getInstance(getActivity()).getCpCustomerId() + "_ProfileA");                    // String or number
            profileUpdate.put("Email", UserInfo.getInstance(getActivity()).getEmail());               // Email address of the user
            profileUpdate.put("Phone", "+14155551234");                 // Phone (with the country code, starting with +)
            profileUpdate.put("Gender", "M");                           // Can be either M or F
            profileUpdate.put("DOB", new Date());                       // Date of Birth. Set the Date object to the appropriate value first
            profileUpdate.put("Photo", "www.foobar.com/image.jpeg");    // URL to the Image

// optional fields. controls whether the user will be sent email, push etc.
            profileUpdate.put("MSG-email", false);                      // Disable email notifications
            profileUpdate.put("MSG-push", true);                        // Enable push notifications
            profileUpdate.put("MSG-sms", false);                        // Disable SMS notifications
            profileUpdate.put("MSG-whatsapp", true);                    // Enable WhatsApp notifications

            ArrayList<String> stuff = new ArrayList<String>();
            stuff.add("bag");
            stuff.add("shoes");
            profileUpdate.put("MyStuff", stuff);                        //ArrayList of Strings

            String[] otherStuff = {"Jeans", "Perfume"};
            profileUpdate.put("MyStuff", otherStuff);                   //String Array

            CleverTapAPI.getDefaultInstance(getActivity()).pushProfile(profileUpdate);
            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();*/

        });

        getBinding().rlCoupanRedem.setOnClickListener(v -> {
           /* HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
            profileUpdate.put("Name", UserInfo.getInstance(getActivity()).getFirstName());                  // String
            profileUpdate.put("Identity", UserInfo.getInstance(getActivity()).getCpCustomerId() + "_ProfileB");                    // String or number
            profileUpdate.put("Email", UserInfo.getInstance(getActivity()).getEmail());               // Email address of the user
            profileUpdate.put("Phone", "+14155551234");                 // Phone (with the country code, starting with +)
            profileUpdate.put("Gender", "M");                           // Can be either M or F
            profileUpdate.put("DOB", new Date());                       // Date of Birth. Set the Date object to the appropriate value first
            profileUpdate.put("Photo", "www.foobar.com/image.jpeg");    // URL to the Image

// optional fields. controls whether the user will be sent email, push etc.
            profileUpdate.put("MSG-email", false);                      // Disable email notifications
            profileUpdate.put("MSG-push", true);                        // Enable push notifications
            profileUpdate.put("MSG-sms", false);                        // Disable SMS notifications
            profileUpdate.put("MSG-whatsapp", true);                    // Enable WhatsApp notifications

            ArrayList<String> stuff = new ArrayList<String>();
            stuff.add("bag");
            stuff.add("shoes");
            profileUpdate.put("MyStuff", stuff);                        //ArrayList of Strings

            String[] otherStuff = {"Jeans", "Perfume"};
            profileUpdate.put("MyStuff", otherStuff);                   //String Array

            CleverTapAPI.getDefaultInstance(getActivity()).pushProfile(profileUpdate);
            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();*/

        });
        getBinding().rlManageDevice.setOnClickListener(view -> {
            if (UserInfo.getInstance(getActivity()).isActive()) {
                Intent manageDeviceIntent = new Intent(getActivity(), ManageDeviceActivity.class);
                manageDeviceIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(manageDeviceIntent);
            } else {
                new ActivityLauncher(getActivity()).astrLoginActivity(getActivity(), AstrLoginActivity.class, "profile");

            }


        });
        getBinding().rlHelp.setOnClickListener(v -> {
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
            intent.putExtra(AppLevelConstants.WEBVIEW, "PRIVACY POLICIES");
            startActivity(intent);
        });
//
//       getBinding().subscribe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mBinding.subscribe.setVisibility(View.GONE);
//                mBinding.manageSubscriptionMore.setVisibility(View.VISIBLE);
//                mBinding.tvBilling.setVisibility(View.VISIBLE);
//                mBinding.tvVIPUser.setText("VIP User");
//
//            }
//        });
//        getBinding().rlLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mBinding.tvLogout.setVisibility(View.GONE);
//                mBinding.loginSignupMore.setVisibility(View.VISIBLE);
//                mBinding.tvEmail.setVisibility(View.GONE);
//                mBinding.tvName.setVisibility(View.GONE);
//                mBinding.edit.setVisibility(View.GONE);
//            }
//        });
//        mBinding.rlPartnerBilling.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        mBinding.rlTransactionHistory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        getBinding().rlLanguageSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), LanguageSettingsActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);


            }
        });
//        mBinding.rlDownloadsMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        mBinding.rlManageDevice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        mBinding.rlContentPreference.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        mBinding.rlCoupanRedem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        mBinding.rlHelp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

    private void showAlertDialog(String title, String msg) {
        FragmentManager fm = getFragmentManager();
        AlertDialogFragment alertDialog = AlertDialogFragment.newInstance(title, msg, getResources().getString(R.string.yes), getResources().getString(R.string.oops_no));
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(Objects.requireNonNull(fm), "fragment_alert");
    }


    @Override
    public void onResume() {
        super.onResume();
        if (navBar.getVisibility() != View.VISIBLE) {
            navBar.setVisibility(View.VISIBLE);
        }

        checkForLoginLogout();
        updateLang();
        if (UserInfo.getInstance(getActivity()).isActive()) {
            getBinding().loginUi.setVisibility(View.VISIBLE);
            getBinding().loginSignupMore.setVisibility(View.GONE);
            getBinding().rlLogout.setVisibility(View.VISIBLE);
            if (!UserInfo.getInstance(getActivity()).getFirstName().equalsIgnoreCase("")) {
                getBinding().tvName.setText(UserInfo.getInstance(getActivity()).getFirstName());
            } else {
                getBinding().tvName.setText("Sooka Superstar");

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
            getBinding().tvVIPUser.setText(getResources().getString(R.string.free_sooka) + " User");
        } else {
            getBinding().tvVIPUser.setText(getResources().getString(R.string.guest_user));

        }
        getBinding().tvBilling.setVisibility(View.GONE);
        getBinding().tvSubscribeNow.setVisibility(View.VISIBLE);
        getBinding().tvSubscribeNow.setText(getResources().getString(R.string.subscibe_now));
        getBinding().subscribe.setText(getResources().getString(R.string.subscribe_more));
        getBinding().subscribe.setVisibility(View.VISIBLE);
        getBinding().productCategory.setVisibility(View.GONE);


    }

    private void getActiveSubscription() {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        subscriptionViewModel.getActiveSubscription(UserInfo.getInstance(getActivity()).getAccessToken()).observe(this, evergentCommonResponse -> {
            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
            if (evergentCommonResponse.isStatus()) {
                if (evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage() != null && evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage().getAccountServiceMessage() != null && evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage().getAccountServiceMessage().size() > 0) {
                    for (AccountServiceMessageItem accountServiceMessageItem : evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage().getAccountServiceMessage()) {
                        if (accountServiceMessageItem.getStatus().equalsIgnoreCase("ACTIVE")) {
                            if (accountServiceMessageItem.getDisplayName() != null)
                                getBinding().tvVIPUser.setText(accountServiceMessageItem.getDisplayName() + " User");
                            if (!accountServiceMessageItem.isRenewal()) {
                                getBinding().tvSubscribeNow.setVisibility(View.GONE);
                            } else {
                                getBinding().tvSubscribeNow.setVisibility(View.VISIBLE);
                                getBinding().tvSubscribeNow.setText("Renew on " + AppCommonMethods.getDateFromTimeStamp(accountServiceMessageItem.getValidityTill()));
                            }
                            if (accountServiceMessageItem.getPaymentMethod() != null && !accountServiceMessageItem.getPaymentMethod().equalsIgnoreCase("")) {
                                getBinding().productCategory.setText(accountServiceMessageItem.getPaymentMethod());
                                getBinding().productCategory.setVisibility(View.VISIBLE);
                            }
                            getBinding().subscribe.setVisibility(View.VISIBLE);
                            getBinding().subscribe.setText(getResources().getString(R.string.manage_subscription));
                            break;
                        }
                    }
                } else {
                    setUiForLogout();
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
        AppCommonMethods.removeUserPrerences(getActivity());
        setUiForLogout();
        getBinding().rlLogout.setVisibility(View.GONE);
        getBinding().loginSignupMore.setVisibility(View.VISIBLE);
        getBinding().loginUi.setVisibility(View.GONE);
        getBinding().edit.setVisibility(View.GONE);

    }
}
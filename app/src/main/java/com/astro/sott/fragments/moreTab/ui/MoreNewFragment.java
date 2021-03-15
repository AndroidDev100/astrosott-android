package com.astro.sott.fragments.moreTab.ui;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.astro.sott.BuildConfig;
import com.astro.sott.R;
import com.astro.sott.activities.forgotPassword.ui.IsThisYouActivity;
import com.astro.sott.activities.language.ui.LanguageSettingsActivity;
import com.astro.sott.activities.loginActivity.ui.AstrLoginActivity;
import com.astro.sott.activities.manageDevice.ui.ManageDeviceActivity;
import com.astro.sott.activities.profile.ui.ChangeEmailConfirmation;
import com.astro.sott.activities.search.ui.QuickSearchGenre;
import com.astro.sott.activities.webview.ui.WebViewActivity;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.databinding.FragmentMoreLayoutBinding;
import com.astro.sott.fragments.subscription.ui.SubscriptionLandingFragment;
import com.astro.sott.fragments.transactionhistory.ui.TransactionHistory;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.utils.userInfo.UserInfo;
import com.clevertap.android.sdk.CleverTapAPI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoreNewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreNewFragment extends BaseBindingFragment<FragmentMoreLayoutBinding> {

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
        UIinitialization();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private void UIinitialization() {
        getBinding().toolbar.setVisibility(View.VISIBLE);
        getBinding().tvVersion.setText("Version " + BuildConfig.VERSION_NAME);
        setClicks();
    }

    private void setClicks() {

        getBinding().loginSignupMore.setOnClickListener(view -> {

            new ActivityLauncher(getActivity()).astrLoginActivity(getActivity(), AstrLoginActivity.class);

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
//                 SubscriptionLandingFragment fragment =new SubscriptionLandingFragment();
//                  FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                  transaction.replace(R.id.relative_layout, fragment).commit();

                navBar.setVisibility(View.GONE);
                SubscriptionLandingFragment someFragment = new SubscriptionLandingFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, someFragment); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
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
                }else {
                    new ActivityLauncher(getActivity()).astrLoginActivity(getActivity(), AstrLoginActivity.class);

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

                AppCommonMethods.removeUserPrerences(getActivity());
                getBinding().rlLogout.setVisibility(View.GONE);
                getBinding().loginSignupMore.setVisibility(View.VISIBLE);
                getBinding().loginUi.setVisibility(View.GONE);
                getBinding().edit.setVisibility(View.GONE);

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
                startActivity(manageDeviceIntent);
            } else {
                new ActivityLauncher(getActivity()).astrLoginActivity(getActivity(), AstrLoginActivity.class);

            }

        });
        getBinding().rlHelp.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra(AppLevelConstants.WEBVIEW, AppLevelConstants.HELP);
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

    @Override
    public void onResume() {
        super.onResume();
        if (navBar.getVisibility() != View.VISIBLE) {
            navBar.setVisibility(View.VISIBLE);
        }


        updateLang();
        if (UserInfo.getInstance(getActivity()).isActive()) {
            getBinding().loginUi.setVisibility(View.VISIBLE);
            getBinding().loginSignupMore.setVisibility(View.GONE);
            getBinding().rlLogout.setVisibility(View.VISIBLE);
            getBinding().tvName.setText(UserInfo.getInstance(getActivity()).getFirstName() + " " + UserInfo.getInstance(getActivity()).getLastName());
            getBinding().tvEmail.setText(UserInfo.getInstance(getActivity()).getEmail());
            getBinding().edit.setVisibility(View.VISIBLE);
        } else {
            getBinding().loginUi.setVisibility(View.GONE);
            getBinding().loginSignupMore.setVisibility(View.VISIBLE);
            getBinding().rlLogout.setVisibility(View.GONE);
            getBinding().edit.setVisibility(View.GONE);


        }
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
                getBinding().tvBilling.setText("Bil Rakan Kongsi");
                getBinding().tvRenewDate.setText("Diperbaharui pada 22/2/21");
                getBinding().subscribe.setText("Langgan");
                getBinding().manageSubscriptionMore.setText("Urus Langganan");
                getBinding().tvManagePayments.setText("Urus Pembayaran");
                getBinding().tvTranscHistory.setText("sejarah transaksi");
                getBinding().tvLanguageSettings.setText("Pemilihan Bahasa");
                getBinding().tvDownloads.setText("Muat turun");
                getBinding().tvManageDevices.setText("Urus Peranti");
                getBinding().tvContentPrefrence.setText("Keutamaan Kandung");
                getBinding().tvCoupanRedemption.setText("Penebusan Kupon");
                getBinding().tvHelp.setText("Tolonglah");
                getBinding().tvLogout.setText("Log keluar");
                getBinding().tvVersion.setText("Versi1.045b");
            } else {
                getBinding().loginSignupMore.setText("Sign In/Sign Up");
                getBinding().tvVIPUser.setText("Guest user");
                getBinding().tvBilling.setText("Partner Billing");
                getBinding().tvRenewDate.setText("Renew on 22/2/21");
                getBinding().subscribe.setText("Subscribe");
                getBinding().manageSubscriptionMore.setText("Manage Subscription");
                getBinding().tvManagePayments.setText("Manage Payments");
                getBinding().tvTranscHistory.setText("Transaction History");
                getBinding().tvLanguageSettings.setText("Language Selection");
                getBinding().tvDownloads.setText("Downloads");
                getBinding().tvManageDevices.setText("Manage Device");
                getBinding().tvContentPrefrence.setText("Content Preferences");
                getBinding().tvCoupanRedemption.setText("Coupon Redemption");
                getBinding().tvHelp.setText("Help");
                getBinding().tvLogout.setText("Logout");
                getBinding().tvVersion.setText("Version1.045b");
            }


        }
    }


}
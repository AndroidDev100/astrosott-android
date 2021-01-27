package com.astro.sott.fragments.moreTab.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astro.sott.R;
import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.activities.language.ui.ChangeLanguageActivity;
import com.astro.sott.activities.language.ui.LanguageSettingsActivity;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.databinding.FragmentMoreLayoutBinding;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoreNewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreNewFragment  extends BaseBindingFragment<FragmentMoreLayoutBinding> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private  FragmentMoreLayoutBinding mBinding;
    private String oldLang, newLang;

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

        UIinitialization();
    }

    private void UIinitialization() {
        setClicks();
    }

    private void setClicks() {
//       getBinding().loginSignupMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                mBinding.loginSignupMore.setVisibility(View.GONE);
//                mBinding.tvEmail.setVisibility(View.VISIBLE);
//                mBinding.tvName.setVisibility(View.VISIBLE);
//                mBinding.edit.setVisibility(View.VISIBLE);
//                mBinding.tvLogout.setVisibility(View.VISIBLE);
//            }
//        });
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
//        mBinding.tvLogout.setOnClickListener(new View.OnClickListener() {
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
        updateLang();
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
            }else {
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